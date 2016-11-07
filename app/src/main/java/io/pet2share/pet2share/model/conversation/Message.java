package io.pet2share.pet2share.model.conversation;

import java.util.HashMap;

/**
 * Created by Bausch on 07.11.2016.
 */

public class Message {
    public String sender;
    public String text;
    public long timestamp;

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HashMap toMap() {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("text",text);
        hashMap.put("timestamp",timestamp);

        return hashMap;
    }
}
