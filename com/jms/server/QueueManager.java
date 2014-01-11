package com.jms.server;

import com.jms.api.Message;
import com.jms.api.impl.JMSMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class QueueManager {

    HashMap<String , Queue<String>> map = new HashMap<String, Queue<String>>();

    /**
     * Returns the last message of the Queue : queueName
     * and if queue is empty it returns NULL
     * @param msg
     * @return
     */
    public Message getMessage ( Message msg ) {

        HashMap<String,String> headerMap = (HashMap<String, String>) msg.parseMessage().get("header");
        String queueName = headerMap.get("ADDRESS");

        String message = dequeue(queueName);

        if ( message == null )
            return null;

        Message message1 = new JMSMessage();
        message1.addHeader("ACTION" , "RECEIVE");
        message1.addHeader("ADDRESS" , queueName);
        message1.addBody(message);

        return message1;
    }

    public boolean addQueue( String queueName ) {
        /**
         * Adds new queue.
         */
        map.put(queueName , new LinkedList<String>());
        return true;
    }

    public boolean dispatch( Message message ) {
        /**
         * Sends this message to appropriate queue
         * Destination name will come from the message header.
         */

        HashMap<String,Object> parsedMessage = message.parseMessage();
        HashMap<String,String> headerMap = (HashMap<String,String>) parsedMessage.get("header");
        String message1 = parsedMessage.get("body").toString();

        String queueName = headerMap.get("ADDRESS");

        enqueue( null , queueName , message1 );
        return true;
    }



    private void enqueue ( String senderAddress , String address , String message ) {

        Queue<String> q = map.get(address);

        if ( q == null ) {

            /*
             *  Client address given is not yet connected to server and
             *  a new entry is to be made
             *  So a new Queue will be created for this new client.
             */
            addQueue(address);
        }
        /*
         *  Adding the message to the queue of the client
         */

        map.get(address).add(message);

    }

    private String dequeue ( String address ) {

        /*
         *  RECEIVE message implies that the client who has sent the request
         *  needs the message from its queue.
         *  i.e The message needs to be removed and returned in its queue.
         */

        if ( map == null )
            return null;

        Queue<String> q = map.get(address);

        if ( q == null )
            return null;

        String message = q.poll();

        if ( message == null ) {
            //System.out.println("No message");
        }
        else {
            //System.out.println("Message in queue for address " + address + " : " + message);
        }

        return message;
    }

    public void printData() {

        Iterator<String> iterator = map.keySet().iterator();

        System.out.println("DATA : ");

        while ( iterator.hasNext() ) {
            String ip = iterator.next();

            Iterator<String> itr = map.get(ip).iterator();

            while ( itr.hasNext() ) {

                String msg = itr.next();
                System.out.println("ip : " + ip + " and msg : " + msg);

            }

        }

    }
}
