package server.transport;

import java.io.IOException;

// Skeleton adapter for WebSocket (requires container/library)
public class WebSocketTransportAdapter implements TransportAdapter {
    public WebSocketTransportAdapter(Object webSocketSession) {}

    @Override
    public String readLine() throws IOException {
        throw new UnsupportedOperationException("WebSocketTransportAdapter not implemented");
    }

    @Override
    public void sendLine(String line) throws IOException {
        throw new UnsupportedOperationException("WebSocketTransportAdapter not implemented");
    }

    @Override
    public void close() throws IOException {
        // close websocket session
    }
}
