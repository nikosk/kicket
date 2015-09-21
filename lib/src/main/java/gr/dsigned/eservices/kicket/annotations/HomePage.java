package gr.dsigned.eservices.kicket.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by IntelliJ IDEA. User: nk Date: 3/11/14 Time: 11:32 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {TYPE})
public @interface HomePage {

    Class<?> backingModel();
}
