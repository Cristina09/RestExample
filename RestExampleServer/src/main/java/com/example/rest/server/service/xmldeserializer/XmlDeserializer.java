package com.example.rest.server.service.xmldeserializer;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

/**
 * Created by Cristina on 5/30/2017.
 */
public class XmlDeserializer {
    private static final Logger LOGGER = Logger.getLogger(XmlDeserializer.class);

    public XmlDeserializer() {}

    public <T> T work(Class<T> rootClass, InputStream inputStream) {
        Serializer serializer = new Persister();
        T root = null;
        try {
            root = serializer.read(rootClass, inputStream);
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception deserializing XML input into a %s java object: ", rootClass.getName()), exception);
        }
        return root;
    }
}
