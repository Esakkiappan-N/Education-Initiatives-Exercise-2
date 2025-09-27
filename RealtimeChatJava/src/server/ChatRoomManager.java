package server;

import java.util.concurrent.ConcurrentHashMap;

public class ChatRoomManager {
    private static ChatRoomManager instance;
    private final ConcurrentHashMap<String, ChatRoom> rooms;

    private ChatRoomManager() {
        rooms = new ConcurrentHashMap<>();
    }

    public static synchronized ChatRoomManager getInstance() {
        if (instance == null) instance = new ChatRoomManager();
        return instance;
    }

    public ChatRoom getOrCreateRoom(String roomId) {
        return rooms.computeIfAbsent(roomId, ChatRoom::new);
    }

    public void removeRoomIfEmpty(String roomId) {
        ChatRoom r = rooms.get(roomId);
        if (r != null && r.isEmpty()) {
            rooms.remove(roomId);
        }
    }
}
