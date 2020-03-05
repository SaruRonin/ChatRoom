package org.codeforall.codecadets.preditors;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ChatServer server;
    private PrintWriter output;
    String message;
    String answer;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;

    }

    @Override
    public void run() {

        try {

            output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (!clientSocket.isClosed()) {
                message = input.readLine();

                if ((message.equals("logout"))) {
                    System.out.println("connection closed");
                    close(input);
                } else if (message.equals("shut down")) {
                    server.closeServer();
                }

                System.out.println("Received: " + message);

                server.broadcast(message, getPort());
            }


        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String message) {
        answer = message.toUpperCase();
        output.println(getPort() + ": " + answer);
        System.out.println("sent to " + clientSocket.getPort());
    }

    public void close(BufferedReader input) throws IOException {
        server.removeClient(this);
        output.close();
        input.close();
        clientSocket.close();
        System.out.println("socket closed + " + clientSocket.getPort());
    }

    public int getPort() {
        return clientSocket.getPort();
    }
}
