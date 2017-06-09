package com.example.rest.server.service.xmldeserializer.sql;

import com.example.rest.server.service.xmldeserializer.sql.statement.Statement;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Cristina on 5/30/2017.
 */
@Root(name = "Sql", strict = false)
public class Sql {

    @ElementList(entry = "Statement", inline = true, required = true)
    private List<Statement> statements;

    public Sql() { }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }
}
