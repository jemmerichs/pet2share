package io.pet2share.pet2share.model.conversation;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Bausch on 07.11.2016.
 */

public class Conversation {

    public String key;
    public String offerid;
    public String participant1;
    public String participant2;
    @Exclude
    public ArrayList<Message> messages;

    public Conversation() {
        this.messages = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public HashMap toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("offerid", offerid);
        map.put("participant1", participant1);
        map.put("participant2", participant2);
        map.put("messages", messages);

        return map;
    }
}
