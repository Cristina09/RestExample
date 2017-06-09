package com.example.rest.server.service.xmldeserializer.sql.statement;

import org.simpleframework.xml.Attribute;

/**
 * Created by Cristina on 5/30/2017.
 */
public class Statement {

        @Attribute(name = "name", required = true)
        private String name;

        @Attribute(name = "sqlString", required = true)
        private String sqlString;

        public Statement() { }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSqlString() {
            return sqlString;
        }

        public void setSqlString(String sqlString) {
            this.sqlString = sqlString;
        }
    }

