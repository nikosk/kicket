package gr.dsigned.eservices.kicket;

import com.google.common.reflect.AbstractInvocationHandler;
import gr.dsigned.eservices.kicket.events.ModelChangedEvent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
* Created by IntelliJ IDEA. User: nk Date: 3/27/14 Time: 10:10 AM
*/
class ModelChangingEventPublishingInvocationHandler extends AbstractInvocationHandler implements Serializable {

    private final WebPage page;
    private final IModel model;

    public ModelChangingEventPublishingInvocationHandler(IModel model, WebPage page) {
        this.page = page;
        this.model = model;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("setObject")) {
            ((EventBusProvider) page).provide().publish(
                new ModelChangedEvent(((IModel) model).getObject(), args[0]));
        }
        return method.invoke(model, args);
    }
}
