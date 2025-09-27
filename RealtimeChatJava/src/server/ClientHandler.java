package server;

import server.transport.TransportAdapter;

import java.io.IOException;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
    private final TransportAdapter transport;
    private ChatRoom room;
    private String username = "<unknown>";
    private volatile boolean running = true;

    public ClientHandler(TransportAdapter transport) {
        this.transport = transport;
    }

    public String getUsername() { return username; }

    public void send(String line) {
        try {
            transport.sendLine(line);
        } catch (IOException e) {
            System.err.println("Failed to send to " + username + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            send("Welcome! To join/create a room: /join <roomId> <username>");
            String line;
            while (running && (line = transport.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("/join ")) {
                    handleJoin(line);
                    continue;
                }

                if (room == null) {
                    send("You must join a room first using: /join <roomId> <username>");
                    continue;
                }

                if (line.equalsIgnoreCase("/users")) {
                    send("Active users: " + room.activeUsernames());
                    continue;
                }
                if (line.equalsIgnoreCase("/leave")) {
                    leaveRoom();
                    continue;
                }

                if (line.startsWith("/pm ")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    if (!st.hasMoreTokens()) { send("Usage: /pm <username> <message>"); continue; }
                    String target = st.nextToken();
                    StringBuilder sb = new StringBuilder();
                    while (st.hasMoreTokens()) sb.append(st.nextToken()).append(" ");
                    String msg = sb.toString().trim();
                    room.findUser(target).ifPresentOrElse(
                            ch -> ch.send(String.format("[pm from %s]: %s", username, msg)),
                            () -> send("User not found: " + target)
                    );
                    continue;
                }

                room.postMessage(username, line);
            }
        } catch (IOException e) {
            System.err.println("I/O error for " + username + ": " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handleJoin(String line) {
        try {
            String[] parts = line.split("\s+", 3);
            if (parts.length < 3) { send("Usage: /join <roomId> <username>"); return; }
            String roomId = parts[1];
            String user = parts[2];
            this.username = user;

            if (this.room != null) {
                if (this.room.getId().equals(roomId)) {
                    send("Already in room " + roomId);
                    return;
                }
                leaveRoom();
            }

            ChatRoomManager mgr = ChatRoomManager.getInstance();
            ChatRoom r = mgr.getOrCreateRoom(roomId);
            this.room = r;
            r.register(this);
            send("Joined room: " + roomId);
            send("Type /users to see active users, /leave to leave, /pm <user> <msg> for private message.");
        } catch (Exception e) {
            send("Failed to join: " + e.getMessage());
        }
    }

    private void leaveRoom() {
        if (room != null) {
            String rid = room.getId();
            room.unregister(this);
            ChatRoomManager.getInstance().removeRoomIfEmpty(rid);
            room = null;
            send("You left the room " + rid);
        } else {
            send("Not in a room.");
        }
    }

    private void cleanup() {
        running = false;
        if (room != null) room.unregister(this);
        try { transport.close(); } catch (IOException ignored) {}
    }
}
