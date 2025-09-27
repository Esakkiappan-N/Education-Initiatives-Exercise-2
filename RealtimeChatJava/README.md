# Realtime Chat Application (Java)

A simple real-time chat application using Java sockets that demonstrates the Observer, Singleton, and Adapter design patterns.

## Features
- Create or join chat rooms by room ID
- Real-time message broadcasting to all users in a room
- Active user listing (`/users`)
- Optional private messaging (`/pm <username> <message>`)
- Message history (sent when joining a room)

## Design Patterns
- **Observer**: `ChatRoom` notifies client handlers of new messages.
- **Singleton**: `ChatRoomManager` manages chat rooms.
- **Adapter**: `TransportAdapter` abstracts transport; `SocketTransportAdapter` implements it for TCP sockets. `WebSocketTransportAdapter` skeleton included.

## Getting Started
1. Compile: `javac -d out src/server/transport/*.java src/server/*.java src/client/*.java`
2. Start server: `java -cp out server.ChatServer 9000`
3. Start a client: `java -cp out client.ChatClient localhost 9000`
4. In client, join a room: `/join Room123 Alice`

## Commands
- `/join <roomId> <username>` — join or create a room
- `/users` — list active users in the room
- `/pm <username> <message>` — private message
- `/leave` — leave current room

## Notes and Extensions
- To add WebSocket support, implement `WebSocketTransportAdapter` with a server container (e.g., Jetty/Tyrus/Spring Boot) and adapt the websocket session to the `TransportAdapter` interface.
- For persistence, replace the in-memory `messageHistory` with a database-backed store.

## License
MIT
