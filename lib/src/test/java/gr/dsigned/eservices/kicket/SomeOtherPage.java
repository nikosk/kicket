package gr.dsigned.eservices.kicket;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import gr.dsigned.eservices.kicket.annotations.AfterInit;
import gr.dsigned.eservices.kicket.annotations.ComponentById;

public class SomeOtherPage extends WebPage {

    @ComponentById("form")
    private Form<?> form;

    @AfterInit
    public void onInit() {
        form.add(new AttributeAppender("class", new Model<String>("test"), " "));
    }
}
