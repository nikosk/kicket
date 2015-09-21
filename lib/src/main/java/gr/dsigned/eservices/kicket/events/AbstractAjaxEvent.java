package gr.dsigned.eservices.kicket.events;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractAjaxEvent extends AbstractComponentEvent {

    private static final long serialVersionUID = 1L;

    private AjaxRequestTarget target;

    protected AbstractAjaxEvent(Component component, AjaxRequestTarget target) {
        super(component);
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }
}
