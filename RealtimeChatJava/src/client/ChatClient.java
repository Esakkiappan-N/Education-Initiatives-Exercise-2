package client;

import server.transport.SocketTransportAdapter;
import server.transport.TransportAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9000;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port)) {
            TransportAdapter ta = new SocketTransportAdapter(socket);

            Thread reader = new Thread(() -> {
                try {
                    String l;
                    while ((l = ta.readLine()) != null) System.out.println(l);
                } catch (Exception e) {
                    System.out.println("Connection closed: " + e.getMessage());
                }
            });
            reader.setDaemon(true);
            reader.start();

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String input;
            while ((input = console.readLine()) != null) {
                ta.sendLine(input);
            }

        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }
    }
}
