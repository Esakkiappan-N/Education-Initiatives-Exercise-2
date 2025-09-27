package server;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {
    private final String id;
    private final List<ClientHandler> observers;
    private final List<String> messageHistory;

    public ChatRoom(String id) {
        this.id = id;
        this.observers = new CopyOnWriteArrayList<>();
        this.messageHistory = Collections.synchronizedList(new ArrayList<>());
    }

    public String getId() { return id; }

    public void register(ClientHandler observer) {
        observers.add(observer);
        broadcastSystem(String.format("%s joined the room. Active users: %d", observer.getUsername(), observers.size()));
        observer.send("-- Previous messages --");
        synchronized (messageHistory) {
            for (String m : messageHistory) observer.send(m);
        }
    }

    public void unregister(ClientHandler observer) {
        observers.remove(observer);
        broadcastSystem(String.format("%s left the room. Active users: %d", observer.getUsername(), observers.size()));
    }

    public void postMessage(String fromUser, String message) {
        String formatted = String.format("[%s]: %s", fromUser, message);
        messageHistory.add(formatted);
        notifyAllObservers(formatted);
    }

    public void broadcastSystem(String text) {
        String formatted = String.format("[system]: %s", text);
        messageHistory.add(formatted);
        notifyAllObservers(formatted);
    }

    private void notifyAllObservers(String message) {
        for (ClientHandler obs : observers) {
            obs.send(message);
        }
    }

    public Optional<ClientHandler> findUser(String username) {
        for (ClientHandler c : observers) {
            if (c.getUsername().equalsIgnoreCase(username)) return Optional.of(c);
        }
        return Optional.empty();
    }

    public List<String> activeUsernames() {
        List<String> list = new ArrayList<>();
        for (ClientHandler c : observers) list.add(c.getUsername());
        return list;
    }

    public boolean isEmpty() { return observers.isEmpty(); }
}
