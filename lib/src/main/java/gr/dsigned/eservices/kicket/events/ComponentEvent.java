package gr.dsigned.eservices.kicket.events;

import java.io.Serializable;

public interface ComponentEvent<T> extends Serializable, IEvent {

    T getComponent();
}
