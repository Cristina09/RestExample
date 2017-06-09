package com.example.rest.client.annotations;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Cristina on 5/31/2017.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireInternal {
}
