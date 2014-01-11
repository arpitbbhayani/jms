package com.jms.client;

import com.jms.api.Connection;
import com.jms.api.ConnectionFactory;
import com.jms.api.Message;
import com.jms.api.impl.JMSConnection;
import com.jms.api.impl.JMSMessage;

import java.util.Scanner;

/**
 * Created by Arpit Bhayani on 10/1/14.
 */
public class JMSClient {

    public JMSClient() {
    }



    public static void main(String[] args) {

        JMSConnection connection = new JMSConnection();

        Scanner scanner = new Scanner(System.in);

        printMenu();

        while ( true ) {
            int ch = scanner.nextInt();
            //System.out.println("Option Selected : " + ch);

            String address = null;
            String message = null;

            switch (ch) {
                case 1:

                    System.out.flush();

                    System.out.print("Send to whom : ");

                    address = scanner.nextLine();
                    address = scanner.nextLine();

                    System.out.print("Send what : ");

                    message = scanner.nextLine();

                    //System.out.println("Have to send to : " + address + " the message : " + message);

                    Message message1 = new JMSMessage();
                    message1.addHeader("ACTION" , "SEND");
                    message1.addHeader("ADDRESS" , address);

                    message1.addBody(message);
                    connection.connectQueueAt("127.0.0.1" , 8011);
                    connection.send(message1);

                    System.out.println("Message sent successfully to : " + address + " | message : " + message);

                    break;
                case 2:

                    System.out.flush();
                    System.out.print("What is your ID : ");

                    address = scanner.nextLine();
                    address = scanner.nextLine();

                    connection.connectQueueAt("127.0.0.1" , 8011);
                    Message message2 = connection.receive(address);

                    System.out.println("Message received : " + message2.parseMessage().get("body").toString());

                    break;
                default:
                    System.out.println("Quitting ...");
                    return;
            }

            printMenu();
        }


    }

    private static void printMenu() {
        System.out.print("\n1. Send data\n2. Receive data\n0. Quit\nEnter the choice : ");
    }
}
