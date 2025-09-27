package server.transport;

import java.io.*;
import java.net.Socket;

public class SocketTransportAdapter implements TransportAdapter {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public SocketTransportAdapter(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public String readLine() throws IOException {
        return in.readLine();
    }

    @Override
    public void sendLine(String line) throws IOException {
        out.println(line);
    }

    @Override
    public void close() throws IOException {
        try { in.close(); } catch (Exception ignored) {}
        try { out.close(); } catch (Exception ignored) {}
        try { socket.close(); } catch (Exception ignored) {}
    }
}
