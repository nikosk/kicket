package gr.dsigned.eservices.kicket;

import org.apache.wicket.markup.html.form.Form;

import gr.dsigned.eservices.kicket.events.form.FormSubmitEvent;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/23/14 Time: 10:22 PM
 */
public class EventPublishingForm extends Form {

    public EventPublishingForm(String id) {
        super(id);
    }

    @Override
    protected void onSubmit() {
        ((EventBusProvider)getPage()).provide().publish(new FormSubmitEvent(this));
    }
}
