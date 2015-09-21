package gr.dsigned.eservices.kicket.example.page;

import org.apache.wicket.markup.html.WebPage;

import gr.dsigned.eservices.kicket.annotations.HomePage;
import gr.dsigned.eservices.kicket.example.page.models.HomePageModel;


@HomePage(backingModel = HomePageModel.class)
public class Home extends WebPage {


}
