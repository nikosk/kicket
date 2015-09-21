package gr.dsigned.eservices.kicket;

import com.google.common.base.Stopwatch;
import com.google.common.reflect.Reflection;
import gr.dsigned.eservices.kicket.annotations.AfterInit;
import gr.dsigned.eservices.kicket.annotations.BackingModel;
import gr.dsigned.eservices.kicket.annotations.ComponentById;
import gr.dsigned.eservices.kicket.annotations.PageMapping;
import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ComponentInjector {

    private static final Logger logger = LoggerFactory.getLogger(ComponentInjector.class);

    public ComponentInjector(WebPage o) {
        Stopwatch watch = null;
        if (logger.isDebugEnabled()) {
            watch = Stopwatch.createStarted();
            logger.debug("Begin injection for page {}", o);
        }
        injectModels(o);
        injectFields(o);
        runCallbacks(o);
        ((EventBusProvider) o).provide().subscribe(o);
        if (logger.isDebugEnabled()) {
            logger.debug("Injection for page {} finished in {}ms", o, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    @SuppressWarnings("unchecked")
    private void injectModels(final WebPage page) {
        Class<? extends WebPage> c = (Class<? extends WebPage>) page.getClass()
                                                                    .getSuperclass();
        final PageMapping mapping = c.getAnnotation(PageMapping.class);
        if (mapping != null) {

            final Field modelField = getModelField(page.getClass());
            if (modelField != null) {
                try {
                    final Serializable modelObject = (Serializable) modelField.getType().newInstance();
                    modelField.setAccessible(true);
                    modelField.set(page, modelObject);
                    final IModel<Serializable> model = Reflection.newProxy(IModel.class, new ModelChangingEventPublishingInvocationHandler(
                        new Model<Serializable>(modelObject), page));
                    page.setDefaultModel(model);
                    page.visitChildren(new IVisitor<Component>() {
                        @Override
                        public Object component(Component component) {
                            if (component.getDefaultModel() != null) {
                                String modelAttribute = component.getDefaultModelObjectAsString();
                                component.setDefaultModel(new PropertyModel<Object>(modelObject, modelAttribute));
                            }
                            return CONTINUE_TRAVERSAL;
                        }
                    });
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Field getModelField(Class clazz) {
        if (clazz != WebPage.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getAnnotation(BackingModel.class) != null) {
                    logger.debug("Found field with backing model {}", field);
                    return field;
                }
            }
        } else {
            return null;
        }
        return getModelField(clazz.getSuperclass());
    }


    @SuppressWarnings("unchecked")
    private void injectFields(WebPage o) {
        Class<? extends WebPage> c = (Class<? extends WebPage>) o.getClass()
                                                                 .getSuperclass();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            ComponentById annotation = f.getAnnotation(ComponentById.class);
            if (annotation != null) {
                logger.debug("Found annotation {}", annotation);
                setComponent(annotation.value(), o, f);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void runCallbacks(WebPage o) {
        Class<? extends WebPage> c = (Class<? extends WebPage>) o.getClass()
                                                                 .getSuperclass();
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            AfterInit annotation = m.getAnnotation(AfterInit.class);
            if (annotation != null) {
                try {
                    logger.debug("Found annotation {}", annotation);
                    m.setAccessible(true);
                    m.invoke(o, new Object[]{});
                    logger.debug("Invoked method {} on  {}", m, o);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setComponent(final String id, final WebPage page, final Field field) {
        page.visitChildren(new IVisitor<Component>() {
            @Override
            public Object component(Component component) {
                if (component.getId().endsWith(id)) {
                    field.setAccessible(true);
                    try {
                        field.set(page, component);
                        logger.debug("Found component {}, injecting to field {}", component, field);
                        return STOP_TRAVERSAL;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
                return CONTINUE_TRAVERSAL;
            }
        });
    }

}
