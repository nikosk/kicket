package gr.dsigned.eservices.kicket;

import gr.dsigned.eservices.kicket.events.EventBus;
import org.apache.wicket.markup.html.WebPage;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/23/14 Time: 9:43 PM
 */
public class APage extends WebPage implements EventBusProvider {

	private EventBus eventBus;

	@Override
	public EventBus provide() {
		if (eventBus == null) {
			eventBus = new EventBus();
		}
		return eventBus;
	}
}
