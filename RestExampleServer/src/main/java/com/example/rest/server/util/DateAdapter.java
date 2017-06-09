package com.example.rest.server.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cristina on 5/30/2017.
 */
public class DateAdapter extends XmlAdapter<String, Date> {
    private final static String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String marshal(Date date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        return dateFormat.format(date);
    }

    @Override
    public Date unmarshal(String string) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        return dateFormat.parse(string);
    }
}
