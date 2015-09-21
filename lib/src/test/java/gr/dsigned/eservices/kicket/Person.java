package gr.dsigned.eservices.kicket;

import java.io.Serializable;

/**
* Created by IntelliJ IDEA. User: nk Date: 3/23/14 Time: 2:30 PM
*/
@SuppressWarnings("unused")
public class Person implements Serializable {

    private String name;

    private Double amount;

    private Person managedBy;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Person getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(Person managedBy) {
        this.managedBy = managedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
