package server;

import server.transport.SocketTransportAdapter;
import server.transport.TransportAdapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public ChatServer(int port) { this.port = port; }

    public void start() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);
            while (true) {
                Socket socket = server.accept();
                try {
                    TransportAdapter ta = new SocketTransportAdapter(socket);
                    ClientHandler handler = new ClientHandler(ta);
                    pool.submit(handler);
                } catch (Exception e) {
                    System.err.println("Failed to accept connection: " + e.getMessage());
                    try { socket.close(); } catch (Exception ignored) {}
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9000;
        if (args.length > 0) port = Integer.parseInt(args[0]);
        new ChatServer(port).start();
    }
}
