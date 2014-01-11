package com.jms.server;

import com.jms.api.Message;
import com.jms.api.impl.JMSMessage;
import sun.print.resources.serviceui;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class JMSServer {

    QueueManager queueManager = null;
    int listenPort = 0;

    public JMSServer( int listenPort ) {
        this.listenPort = listenPort;
        queueManager = new QueueManager();
    }

    private void start() {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(listenPort);

            while ( true ) {

                /*
                 *  Waits till someone sends the REQUEST.
                 */



                Socket socket = serverSocket.accept();

                /* This tells which client has made a connection */
                InetAddress clientAddress = socket.getInetAddress();

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                int len = inputStream.readInt();

                //System.out.println("Message Length : " + len);

                StringBuffer message = new StringBuffer();

                for ( int i = 0 ; i < len ; i++ ) {
                    message.append(inputStream.readChar());
                }

                String messageReceived = new String(message);
                //System.out.println("Message received : " + messageReceived);

                String[] messageReceivedSplit = messageReceived.split("\n");
                String action = null;

                Message message1 = new JMSMessage();

                for ( int i = 0 ; i < messageReceivedSplit.length ; i++) {
                    if ( messageReceivedSplit[i].startsWith("ACTION") ) {

                        action = messageReceivedSplit[i].split(" ")[1];
                        message1.addHeader("ACTION" , action );

                    }
                    else if ( messageReceivedSplit[i].startsWith("ADDRESS") ) {

                        message1.addHeader("ADDRESS" , messageReceivedSplit[i].split(" ")[1] );

                    }
                    else {
                        message1.addBody(messageReceivedSplit[i]);
                    }
                }

                if ( action.equals("SEND") ) {
                    queueManager.dispatch(message1);
                }
                else if ( action.equals("RECEIVE") ) {
                    Message messageToSendBack = queueManager.getMessage(message1);
                    String messageToSend = "No messages !";

                    if ( messageToSendBack != null )
                        messageToSend = messageToSendBack.parseMessage().get("body").toString();

                    //System.out.println("Message to send back : " + messageToSend );

                    outputStream.writeInt(messageToSend.length());
                    outputStream.flush();

                    outputStream.writeChars(messageToSend);
                    outputStream.flush();

                }

                //queueManager.printData();

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            close(serverSocket);
        }

    }

    private void close( Object o1 ) {
        try {
            if ( o1 != null )
                ((ServerSocket)o1).close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        JMSServer jmsServer = new JMSServer(8011);
        jmsServer.start();

    }

}
