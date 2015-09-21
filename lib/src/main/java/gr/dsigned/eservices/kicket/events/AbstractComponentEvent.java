package gr.dsigned.eservices.kicket.events;

import org.apache.wicket.Component;

/**
 * User: kastamoulasn Date: 4/4/2013 Time: 11:42 πμ
 */
public abstract class AbstractComponentEvent<T extends Component> implements ComponentEvent<T> {

    private static final long serialVersionUID = 1L;

    private T component;

    public AbstractComponentEvent(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }
}
