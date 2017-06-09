package com.example.rest.server.db.manager;

/**
 * Created by Cristina on 5/30/2017.
 */
public class Query {
    private String name = null;
    private Object[] parameters = null;

    public Query(String name, Object... parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(this.name);
        for (Object parameter: this.parameters) {
            if (null == parameter) {
                text.append(":[null - error?]");
            } else {
                text.append(":").append(parameter.toString());
            }
        }
        return text.toString();
    }
}
