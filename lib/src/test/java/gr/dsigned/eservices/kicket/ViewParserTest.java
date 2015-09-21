package gr.dsigned.eservices.kicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/10/14 Time: 11:16 PM
 */
public class ViewParserTest {

    WicketTester tester;

    @Before
    public void setUp() {
        tester = new WicketTester();
    }

    @Test
    public void testScan() throws Exception {
        final Class<? extends WebPage> page = PageTransformer.extend(TestPage.class);
        tester.startPage(page.newInstance());
        tester.assertComponent("form", Form.class);
        tester.assertComponent("form:name", TextField.class);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("name", "test");
        formTester.submit();
        tester.assertModelValue("form:name", "test");
    }

    @Test
    public void testSomeOtherPage() throws Exception {
        final Class<? extends WebPage> page = PageTransformer.extend(SomeOtherPage.class);
        tester.startPage(page.newInstance());
        WebPage newInstance = page.newInstance();
        tester.startPage(newInstance);
        tester.assertComponent("form", Form.class);
    }
}
