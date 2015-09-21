package gr.dsigned.eservices.kicket.events.form;

import org.apache.wicket.markup.html.form.Form;

import gr.dsigned.eservices.kicket.events.AbstractComponentEvent;

public class FormSubmitEvent extends AbstractComponentEvent<Form<?>> {

    private static final long serialVersionUID = 1L;

    public FormSubmitEvent(Form<?> component) {
        super(component);
    }

}
