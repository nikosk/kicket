package gr.dsigned.eservices.kicket.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/23/14 Time: 10:59 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD})
public @interface BackingModel {

}
