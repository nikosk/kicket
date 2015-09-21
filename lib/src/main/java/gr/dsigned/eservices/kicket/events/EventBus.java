package gr.dsigned.eservices.kicket.events;

import com.google.common.base.Stopwatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Event bus implmentation that propagates events to handlers registered with this bus. Handlers are plain classes with methods annotated with
 * @Handles. The handler method must have 1 argument that implements the IEvent interface.
 *
 * @author kastamoulasn
 * @see IEvent
 */
public class EventBus implements Serializable {

    private static final Logger logger = LoggerFactory
        .getLogger(EventBus.class);

    private static final long serialVersionUID = 1L;
    private Set<Serializable> handlerClasses = new LinkedHashSet<Serializable>();


    /**
     * Register a method to be called when an event happens. This is an annotations based Observer pattern. The implementation avoids the use of
     * interfaces because one class might need to listen to several types of events which cannot be done by implementing one interface.
     *
     * @param o the instance of the class that contains the listener method.
     */
    public void subscribe(Serializable o) {
        handlerClasses.add(o);
        logger.debug("Registered handler: {}", o);
    }

    /**
     * Publishers use this method to send events to any subscribed handler classes. An exception in one handler stops execution and any subsequent
     * handlers will never be called. <p/>
     *
     * <pre>
     * Handler methods are matched against:
     *
     * a) the event's class vs the eventClass class literal in the @Handles annotation
     * b) the event's generic type vs the type class literal in the @Handles annotation
     *
     * i.e.:
     *
     * 1) Invoke handler when @Handles has eventClass equals to AnyEvent
     *    and type is Void
     *    Example annotation: @Handles (i.e. receive all events whatever the type)
     *
     * 2) Invoke handler when @Handles has eventClass equals to AnyEvent, type is not Void
     *    and the event's generic type equals the annotation's type.
     * 	  Example annotation: @Handles(type=Query.class) (i.e.: receive any event with generic type Query)
     *
     * 3) Invoke handler when @Handles has eventClass other than AnyEvent, type is Void and
     *    the event's class equals to eventClass
     *    Example annotation: @Handles(SearchEvent.class) (i.e.: receive events of type SearchEvent whatever their generic type)
     *
     * 4) Invoke the handler when @Handles has eventClass other that AnyEvent, type is not Void and
     *    the event's generic type equals to type and event is a subclass of eventClass.
     *    Example annotation: @Handles(SearchEvent.class, type = PolicySearchCriteria.class)
     *    (i.e. receive events of type SearchEvent and generic type PolicySearchCriteria)
     *
     * </pre>
     *
     * @param event the event object to pass to handler methods
     * @see Handles
     */
    public void publish(IEvent event) {
        Stopwatch watch = null;
        if (logger.isDebugEnabled()) {
            watch = new Stopwatch().start();
        }
        logger.debug("Event published: {}", event);
        // the broacasted event's generic type (if any)
        for (Serializable handlerClass : handlerClasses) {
            final Method[] declaredMethods = handlerClass.getClass().getMethods();
            for (Method m : declaredMethods) {
                logger.trace("Examining handler method: {}", m);
                Handles handlerAnnotation = m.getAnnotation(Handles.class);
                if (handlerAnnotation != null) {
                    logger.trace("Found annotation: {}", handlerAnnotation);
                    Class<? extends IEvent> registeredEventClass = handlerAnnotation
                        .value();
                    // check handler's registration
                    if (registeredEventClass.equals(event.getClass())
                        || registeredEventClass.isInstance(event)) {
                        invokeHandler(event, handlerClass, m);
                    }

                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Publishing for event {} finished in {}ms", event, watch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private void invokeHandler(
        IEvent event, Serializable handlerClass,
        Method m
    ) {
        try {
            m.invoke(handlerClass, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return an unmodifiable list of this bus' handlers
     */
    public Set<Serializable> getHandlerClasses() {
        return Collections.unmodifiableSet(handlerClasses);
    }

    /**
     * Clear all the handlers registered in this bus. This is mostly useful for testing purposes.
     */
    final void clear() {
        handlerClasses.clear();
    }
}
