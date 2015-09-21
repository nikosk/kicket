package gr.dsigned.eservices.kicket.example.page.models;

import java.io.Serializable;
import java.util.List;

/**
* Created by IntelliJ IDEA. User: nk Date: 3/23/14 Time: 1:31 PM
*/
public class OtherPageModel implements Serializable {

    private String title;

    private List<String> results;

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
