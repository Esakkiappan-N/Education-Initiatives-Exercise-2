package server.transport;

import java.io.IOException;

public interface TransportAdapter {
    String readLine() throws IOException;
    void sendLine(String line) throws IOException;
    void close() throws IOException;
}
