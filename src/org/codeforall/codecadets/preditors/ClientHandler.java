package org.codeforall.codecadets.preditors;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ChatServer server;
    private PrintWriter output;
    private BufferedReader input;
    String message;
    String name;


    public ClientHandler(Socket socket, ChatServer server, String name) {
        this.clientSocket = socket;
        this.server = server;
        this.name = name;
    }

    @Override
    public void run() {

        try {

            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            send( "-----------------------------");
            send("| Welcome to Flint's Server! |");
            send( "-----------------------------\n");
            send("Please enter a new name by typing '/name' followed by your name.");

            while (!clientSocket.isClosed()) {

                message = input.readLine();
                if(message != null) {


                    commands(message);
                }
            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    public void close() throws IOException {
        server.removeClient(this);
        output.close();
        input.close();
        clientSocket.close();
        message = " has logged out";
        server.broadcast(message, name);
    }

    public void send(String message) {
        output.println(message);
        //System.out.println(message);
        System.out.println("sent to " + name);
    }


    public String setName(String message) {
        String newName = message.split(" ")[1];
        if ( newName.length() > 8){
           send("A name can not be longer than 8 digits");
        }
        return name = newName;
    }

    public void shout(String message) {
        message = message.substring(6).toUpperCase();
        server.broadcast(message, name);
    }

    public void list() {
        output.println(server.userList());

    }

    public void whisper(String message) {
        String[] words = message.split(" ");
        String receiverName = words[1];
        String newMessage = "";
        for (int i = 2; i < words.length; i++) {
            newMessage = newMessage + " " + words[i];
        }
        server.personalMessage(newMessage, receiverName, name);
    }

    public String getName() {
        return name;
    }

    public void commands(String message) throws IOException {


            if (!message.startsWith("/")) {
                server.broadcast(message, name);
            }
            String command = message.split(" ")[0];

            switch (command) {
                case "/logout":
                    System.out.println("connection closed");
                    close();
                    break;
                case "/shout":
                    shout(message);
                    break;
                case "/list":
                    list();
                    break;
                case "/name":
                    setName(message);
                    break;
                case "/whisper":
                    whisper(message);
                    break;

            }

        }
    }

