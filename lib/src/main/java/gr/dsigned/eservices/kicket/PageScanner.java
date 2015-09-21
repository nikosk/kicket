package gr.dsigned.eservices.kicket;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import gr.dsigned.eservices.kicket.annotations.HomePage;
import gr.dsigned.eservices.kicket.annotations.PageMapping;
import org.apache.wicket.markup.html.WebPage;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/12/14 Time: 12:29 AM
 */
@SuppressWarnings("unchecked")
public class PageScanner {

    private static PageScanner instance = null;
    private final LinkedHashMap<String, MappedPageHolder> MAPPING_CACHE =
        new LinkedHashMap<String, MappedPageHolder>();

    private Reflections reflections;

    private PageScanner() {
        init();
    }

    public static PageScanner instance() {
        if (instance == null) {
            instance = new PageScanner();
        }
        return instance;
    }

    public MappedPageHolder getHomePageMapping() {
        return MAPPING_CACHE.get("/");
    }

    public Collection<MappedPageHolder> getMappings() {
        final Collection<MappedPageHolder> filtered = Collections2.filter(MAPPING_CACHE.values(), new Predicate<MappedPageHolder>() {
            @Override
            public boolean apply(@Nullable MappedPageHolder input) {
                return !input.getMapping().equals("/");
            }
        });
        return filtered;
    }

    public Class<? extends WebPage> getPage(String mapping) {
        return MAPPING_CACHE.get(mapping).getExtended();
    }

    private void init() {
        //FIXME: Classpath scanning should be parameterized
        reflections = new Reflections("gr");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(PageMapping.class);
        for (Class<?> p : classes) {
            final PageMapping annotation = p.getAnnotation(PageMapping.class);
            MAPPING_CACHE.put(annotation.value(), new MappedPageHolder(annotation.value(), (Class<? extends WebPage>) p,
                                                                       PageTransformer.extend((Class<? extends WebPage>) p)));
        }
        classes = reflections.getTypesAnnotatedWith(HomePage.class);
        for (Class<?> p : classes) {
            MAPPING_CACHE.put("/", new MappedPageHolder("/", (Class<? extends WebPage>) p,
                                                        PageTransformer.extend((Class<? extends WebPage>) p)));
        }
    }

    public static class MappedPageHolder {

        private String mapping;

        private Class<? extends WebPage> original;

        private Class<? extends WebPage> extended;

        private MappedPageHolder(String mapping, Class<? extends WebPage> original, Class<? extends WebPage> extended) {
            this.mapping = mapping;
            this.original = original;
            this.extended = extended;
        }

        public Class<? extends WebPage> getExtended() {
            return extended;
        }

        public void setExtended(Class<? extends WebPage> extended) {
            this.extended = extended;
        }

        public String getMapping() {
            return mapping;
        }

        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        public Class<? extends WebPage> getOriginal() {
            return original;
        }

        public void setOriginal(Class<? extends WebPage> original) {
            this.original = original;
        }
    }
}
