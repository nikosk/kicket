package gr.dsigned.eservices.kicket.example.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import gr.dsigned.eservices.kicket.PageTransformer;
import gr.dsigned.eservices.kicket.example.ExampleApplication;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/27/14 Time: 9:56 AM
 */
public class OtherPageTest {

    WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester(new ExampleApplication());
    }

    @Test
    public void testFormExists() throws Exception {
        final Class<? extends WebPage> page = PageTransformer.extend(OtherPage.class);
        tester.startPage(page);
        tester.assertComponent("form", Form.class);
        tester.assertComponent("form:name", TextField.class);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("name", "test");
        formTester.submit();
        tester.assertModelValue("form:name", "test");
    }
}
