package gr.dsigned.eservices.kicket;

import gr.dsigned.eservices.kicket.annotations.AfterInit;
import gr.dsigned.eservices.kicket.annotations.ComponentById;
import gr.dsigned.eservices.kicket.annotations.PageMapping;
import gr.dsigned.eservices.kicket.events.Handles;
import gr.dsigned.eservices.kicket.events.form.FormSubmitEvent;
import org.apache.wicket.markup.html.form.Form;

@PageMapping(value = "/test")
public class TestPage extends SomeOtherPage {

	@ComponentById("form")
	private Form<?> form;

	private Person person;

	@AfterInit
	public void init() {
	}

	@Handles(FormSubmitEvent.class)
	public void onSubmit(FormSubmitEvent event) {
		System.out.println(event.getComponent());
	}

}
