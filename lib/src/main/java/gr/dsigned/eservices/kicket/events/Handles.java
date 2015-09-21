package gr.dsigned.eservices.kicket.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * @author kastamoulasn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD})
public @interface Handles {

    /**
     * The type of the event this method wants to handle. If no value is passed the annotated method will be called for every event.
     *
     * @return the type to the event this handler listens to.
     */
    Class<? extends IEvent> value() default IEvent.class;

}
