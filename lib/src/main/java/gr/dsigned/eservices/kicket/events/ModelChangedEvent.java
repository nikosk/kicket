package gr.dsigned.eservices.kicket.events;

import com.google.common.base.Objects;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/24/14 Time: 8:39 AM
 */
public class ModelChangedEvent implements IEvent {

    private Object previousValue;
    private Object newValue;

    public ModelChangedEvent(Object previousValue, Object newValue) {
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Object previousValue) {
        this.previousValue = previousValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("previousValue", previousValue)
                      .add("newValue", newValue)
                      .toString();
    }
}
