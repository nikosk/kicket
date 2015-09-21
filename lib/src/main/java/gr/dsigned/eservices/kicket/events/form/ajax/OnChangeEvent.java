package gr.dsigned.eservices.kicket.events.form.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;

public class OnChangeEvent extends FormAjaxEvent {

    private static final long serialVersionUID = 1L;

    public OnChangeEvent(FormComponent<?> component, AjaxRequestTarget target) {
        super(component, target, "onChange");
    }
}
