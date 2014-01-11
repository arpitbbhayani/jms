package com.jms.api;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public interface Connection {

    /**
     * Connects the client to a Queue running at Address "address" and at port "port".
     * @param address
     * @param port
     * @return true : if connection successful <br/> false: otherwise
     */
    public boolean connectQueueAt ( String address , int port );

    public boolean send( Message message );

    //public boolean send ( Message message , String destinationName);

    public Message receive ( String queueName );

    //public Message receive ( String queueName , String destinationName );

}
