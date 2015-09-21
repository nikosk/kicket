package gr.dsigned.eservices.kicket.example.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import gr.dsigned.eservices.kicket.annotations.AfterInit;
import gr.dsigned.eservices.kicket.annotations.BackingModel;
import gr.dsigned.eservices.kicket.annotations.ComponentById;
import gr.dsigned.eservices.kicket.annotations.PageMapping;
import gr.dsigned.eservices.kicket.events.Handles;
import gr.dsigned.eservices.kicket.events.form.FormSubmitEvent;
import gr.dsigned.eservices.kicket.example.page.models.Person;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/12/14 Time: 12:27 AM
 */
@PageMapping(value = "/test")
public class OtherPage extends WebPage {

    @BackingModel
    Person person;

    @ComponentById("link")
    BookmarkablePageLink<?> link;


    @AfterInit
    public void afterInit() {
        link.setVisible(false);
    }


    @Handles(FormSubmitEvent.class)
    public void onSubmit(FormSubmitEvent event) {
        System.out.println(event.getComponent());
        link.setVisible(true);
        System.out.println(person);
    }
}
