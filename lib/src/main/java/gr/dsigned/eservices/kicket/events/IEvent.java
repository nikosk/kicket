package gr.dsigned.eservices.kicket.events;

/**
 * Generified interface for classes that want to participate in the event publish/subcribe event system.
 *
 * NOTE: Implementers of this interface must be concrete non-generic classes.
 *
 * NOTE2: If the type of the event is a generified collection the user should consider implementing this interface instead of subclassing inline.
 * Class IEvent<List<?>> does not have the same type as IEvent<List> and class literals cannot contain type information (i.e. List<String>.class)
 * which could lead to hard to find issues.
 *
 * @param <T> the raw type of the event
 */
public interface IEvent {

}
