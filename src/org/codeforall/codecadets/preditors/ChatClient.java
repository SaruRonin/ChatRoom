package org.codeforall.codecadets.preditors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {


    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    String message;
    String answer;
    Socket client;
    Thread t2;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();

        client.start();

    }

    public void start() {

        try {
            client = new Socket(HOST, PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            t2 = new Thread(new Runnable() {


                @Override
                public void run() {

                    BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
                    try {
                        PrintWriter output = new PrintWriter(client.getOutputStream(), true);
                        while (!client.isClosed()) {

                            answer = userIn.readLine();
                            output.println(answer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            t2.start();

            while (!client.isClosed()) {
                message = input.readLine();
                System.out.println(message);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

