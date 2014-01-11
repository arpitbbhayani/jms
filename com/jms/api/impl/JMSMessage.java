package com.jms.api.impl;

import com.jms.api.Message;

import java.util.HashMap;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class JMSMessage implements Message {

    /*
     *  Message header should be like
     *
     *  Action SEND/RECEIVE
     *  ADDRESS CLIENT_NAME
     */

    private StringBuffer header = new StringBuffer();
    private StringBuffer property = new StringBuffer();
    private StringBuffer body = new StringBuffer();

    @Override
    public void addHeader(String key, String value) {
        if ( key == null || value == null ) {
            // TODO: Throw exception
        }

        header.append( key + " " + value + "\n");
    }

    @Override
    public void addProperty(String key, String value) {
        if ( key == null || value == null ) {
            // TODO: Throw exception
        }

        property.append( key + " " + value + "\n");
    }

    @Override
    public void addBody(Object content) {

        /*
         *  Message is text assumed
         */
        String messageContent = (String) content;
        body.append(messageContent);

    }

    @Override
    public HashMap<String, Object> parseMessage() {

        //System.out.println("Parsing Message Header : " + this.header);
        HashMap<String , Object> map = new HashMap<String, Object>();
        HashMap<String, String> headerMap = new HashMap<String, String>();
        HashMap<String, String> propertyMap = new HashMap<String, String>();

        String header = this.header.toString();
        String property = this.header.toString();
        Object body = this.body;

        String[] headerKeyValuePairs = header.split("\n");
        for ( int i = 0 ; i < headerKeyValuePairs.length ; i++ ) {
            String[] keyValue = headerKeyValuePairs[i].split(" ");
            headerMap.put(keyValue[0] , keyValue[1]);
        }

        String[] propertyKeyValuePairs = property.split("\n");
        for ( int i = 0 ; i < propertyKeyValuePairs.length ; i++ ) {
            String[] keyValue = propertyKeyValuePairs[i].split(" ");
            propertyMap.put(keyValue[0] , keyValue[1]);
        }

        map.put("header" , headerMap);
        map.put("property" , propertyMap);
        map.put("body" , body);

        return map;
    }


}
