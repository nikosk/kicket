package gr.dsigned.eservices.kicket.example;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import gr.dsigned.eservices.kicket.PageScanner;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/11/14 Time: 11:01 PM
 */
public class ExampleApplication extends WebApplication {

    public ExampleApplication() {
        for (PageScanner.MappedPageHolder p : PageScanner.instance()
                                                         .getMappings()) {
            mountBookmarkablePage(p.getMapping(), p.getExtended());
        }
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return PageScanner.instance().getHomePageMapping().getExtended();
    }
}
