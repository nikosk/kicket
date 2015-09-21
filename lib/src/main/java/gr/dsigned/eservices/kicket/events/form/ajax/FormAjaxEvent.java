package gr.dsigned.eservices.kicket.events.form.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;

import gr.dsigned.eservices.kicket.events.AbstractAjaxEvent;

/**
 * User: kastamoulasn Date: 4/4/2013 Time: 11:40 πμ
 */
public class FormAjaxEvent extends AbstractAjaxEvent {

    private static final long serialVersionUID = 1L;

    private final String eventName;

    private final FormComponent<?> component;

    public FormAjaxEvent(FormComponent<?> component, AjaxRequestTarget target, String eventName) {
        super(component, target);
        this.eventName = eventName;
        this.component = component;
    }

    @Override
    public FormComponent<?> getComponent() {
        return component;
    }

    public String getEventName() {
        return eventName;
    }
}
