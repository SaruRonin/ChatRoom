package org.codeforall.codecadets.preditors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private final static int PORT = 9999;
    private int user = 0;
    private List<ClientHandler> users = new LinkedList<>();
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        ChatServer server = new ChatServer();

        server.connect();
    }


    public void connect() {

        try {
            serverSocket = new ServerSocket(PORT);
            ExecutorService threadpool = Executors.newCachedThreadPool();

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("connected with: " + clientSocket.getInetAddress() + clientSocket.getLocalPort());

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                threadpool.execute(clientHandler);

                synchronized (users) {
                    users.add(clientHandler);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, int port) {

        synchronized (users) {
            for (ClientHandler clientHandler : users) {
                if (port != clientHandler.getPort()) {
                    clientHandler.send(message);
                }
            }
        }
    }

    public void removeClient(ClientHandler client) {
        users.remove(client);
    }

    public void closeServer() throws IOException {
        serverSocket.close();

    }
}
