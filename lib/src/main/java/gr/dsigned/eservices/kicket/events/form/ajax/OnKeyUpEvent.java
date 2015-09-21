package gr.dsigned.eservices.kicket.events.form.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * User: kastamoulasn Date: 4/4/2013 Time: 12:37 μμ
 */
public class OnKeyUpEvent extends FormAjaxEvent {

    private static final long serialVersionUID = 1L;

    public OnKeyUpEvent(FormComponent<?> component, AjaxRequestTarget target) {
        super(component, target, "onKeyup");
    }
}
