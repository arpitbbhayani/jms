package com.jms.api;

import com.jms.api.impl.JMSConnection;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class ConnectionFactory {

    public static Connection connection = null;

    /**
     *  @return com.jms.api.Connection object
     */
    public static Connection getConnection() {
        connection = new JMSConnection();
        return connection;
    }

}
