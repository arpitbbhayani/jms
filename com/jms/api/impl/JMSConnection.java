package com.jms.api.impl;

import com.jms.api.Connection;
import com.jms.api.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class JMSConnection implements Connection {

    public String queueAddress = null;
    public int queuePort = 0;

    @Override
    public boolean connectQueueAt(String address, int port) {
        this.queueAddress = address;
        this.queuePort = port;

        return true;

    }

    @Override
    public boolean send(Message message) {

        Socket socket = null;
        DataOutputStream dataOutputStream = null;

        HashMap<String , Object> map = message.parseMessage();

        HashMap<String,String> headerMap = (HashMap<String,String> )map.get("header");
        String address = headerMap.get("ADDRESS");
        String action = headerMap.get("ACTION");

        if ( address == null ) {
            /*
             * User didn't provide "to" field.
             */
            // TODO: Throw error/exception
            return false;
        }

        try {

            socket = new Socket(queueAddress , queuePort);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // TODO: currently String is being passed ... need to change it to generalize the send.
            // TODO: Serialize the object to send it across the Socket.

            /**
             * messageToQueueManager Structure:
             * ACTION SEND
             * ADDRESS CLIENT_ID
             */

            String messageToQueueManager = "ACTION " + action + "\n" + "ADDRESS " + address + "\n";
            String message1 = message.parseMessage().get("body").toString();

            //System.out.println("Message to Queue manager " + messageToQueueManager);
            //System.out.println("Message : " + message1);
            //System.out.println("Total length : " + (messageToQueueManager.length() + message1.length()));

            dataOutputStream.writeInt(messageToQueueManager.length() + message1.length());
            dataOutputStream.flush();

            dataOutputStream.writeChars(messageToQueueManager + message1);
            dataOutputStream.flush();

        } catch (IOException e) {

            e.printStackTrace();
        }
        finally {

            close(socket , dataOutputStream , null);
        }
        return true;

    }

    @Override
    public Message receive(String queueName) {

        Socket socket = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;

        try {

            socket = new Socket( queueAddress , queuePort);

            dataOutputStream = new DataOutputStream( socket.getOutputStream() );
            dataInputStream = new DataInputStream( socket.getInputStream() );

            /**
             * messageToQueueManager Structure:
             * ACTION SEND
             * ADDRESS CLIENT_ID
             */

            String messageToQueueManager = "ACTION " + "RECEIVE" + "\n" + "ADDRESS " + queueName + "\n";

            //System.out.println("Message to Queue manager " + messageToQueueManager);
            //System.out.println("Total length : " + (messageToQueueManager.length()));

            dataOutputStream.writeInt(messageToQueueManager.length());
            dataOutputStream.flush();

            dataOutputStream.writeChars(messageToQueueManager);
            dataOutputStream.flush();

            int messageLength = dataInputStream.readInt();
            StringBuffer messageBuffer = new StringBuffer();

            //System.out.println("Length of the message : " + messageLength);

            for ( int i = 0 ; i < messageLength ; i++ ) {
                messageBuffer.append(dataInputStream.readChar());
            }

            String message = new String(messageBuffer);
            //System.out.println("Message received : " + message);

            Message message1 = new JMSMessage();
            message1.addHeader("ACTION" , "RECEIVE");
            message1.addHeader("ADDRESS" , "ADDRESS");
            message1.addBody(message);

            return message1;

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            close(socket , dataOutputStream , dataInputStream);
        }

        return null;
    }

    private void close( Object o1 , Object o2 , Object o3 ) {

        /*
         *  o1 -> Socket Type
         *  o2 -> DataOutputStream
         *  o3 -> InputStream
         */

        try {
            if ( o1 != null )
                ((Socket) o1).close();
            if ( o2 != null )
                ((DataOutputStream) o2).close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
