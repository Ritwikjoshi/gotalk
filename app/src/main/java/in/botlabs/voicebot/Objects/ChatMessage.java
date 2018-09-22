package in.botlabs.voicebot.Objects;

/**
 * Created by yashkothari on 12/03/18.
 */

public class ChatMessage {
    public boolean left;
    public String message;
    public String imageUrl,openapp;

    public ChatMessage(boolean left, String message, String imageUrl,String openapp) {
        super();
        this.left = left;
        this.message = message;
        this.imageUrl = imageUrl;
        this.openapp = openapp;
    }
}

