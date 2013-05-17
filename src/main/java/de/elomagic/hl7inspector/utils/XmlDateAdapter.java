/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author carstenrambow
 */
public class XmlDateAdapter extends XmlAdapter<String, Date> {

    private static String pattern = "yyyy.MM.dd";

    @Override
    public Date unmarshal(String value) throws Exception {
        return new SimpleDateFormat(pattern).parse(value);
    }

    @Override
    public String marshal(Date date) throws Exception {
        return new SimpleDateFormat(pattern).format(date);
    }

}
