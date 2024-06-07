package packets;

public class MessagePacket extends Packet {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String message;

    public MessagePacket(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return this.sender;
    }

    public String getMessage() {
        return this.message;
    }
}
