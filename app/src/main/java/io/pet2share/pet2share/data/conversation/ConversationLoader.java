package io.pet2share.pet2share.data.conversation;

import android.provider.ContactsContract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.pet2share.pet2share.common.FirebaseLoader;
import io.pet2share.pet2share.interfaces.loader.ConversationLoaderInterface;
import io.pet2share.pet2share.model.conversation.Conversation;
import io.pet2share.pet2share.model.conversation.Message;

/**
 * Created by Bausch on 07.11.2016.
 */

public class ConversationLoader extends FirebaseLoader {

    private static ConversationLoader INSTANCE = new ConversationLoader();
    private HashMap<String, Conversation> conversationHashMap;
    private HashMap<String, ValueEventListener> valueEventListenerHashMap;
    private boolean firedUp = false;

    public ConversationLoader() {
        this.conversationHashMap = new HashMap<>();
        this.valueEventListenerHashMap = new HashMap<>();
    }

    public void createConversation(String uid, String offerid) {
        Conversation conversation = new Conversation();
        conversation.offerid = offerid;
        conversation.participant1 = uid;
        conversation.participant2 = getFirebaseAuth().getCurrentUser().getUid();
        HashMap<String, Object> conversationMap = conversation.toMap();
        String key = getFirebaseDatabase().getReference().child("conversations").push().getKey();
        Map<String, Object> update = new HashMap<String, Object>();
        update.put(String.format("conversations/%s", key), conversationMap);

        conversation.key = key;
        getFirebaseDatabase().getReference().updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String, Object> userConversationsMap = new HashMap<>();
                userConversationsMap.put(String.format("userconversations/%s/%s", uid, key), true);
                userConversationsMap.put(String.format("userconversations/%s/%s", getFirebaseAuth().getCurrentUser().getUid(), key), true);
                getFirebaseDatabase().getReference().updateChildren(userConversationsMap);
            }
        });
    }

    public void fireUp() {
        if (!firedUp) {
            getFirebaseDatabase().getReference(String.format("userconversations/%s", getFirebaseAuth().getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot conversationReferenceShot : dataSnapshot.getChildren()) {
                        if (!valueEventListenerHashMap.containsKey(conversationReferenceShot.getKey())) {
                            addValueEventKeyListener((String) conversationReferenceShot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        firedUp = true;
    }

    private void addValueEventKeyListener(String conversationId) {
        valueEventListenerHashMap.put(conversationId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                conversation.key = dataSnapshot.getKey();
                for (DataSnapshot messageSnapshots : dataSnapshot.child("messages").getChildren()) {
                    conversation.messages.add(messageSnapshots.getValue(Message.class));
                }

                conversationHashMap.remove(conversationId);
                conversationHashMap.put(conversationId, conversation);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getFirebaseDatabase().getReference(String.format("conversations/%s/", conversationId)).addValueEventListener(valueEventListenerHashMap.get(conversationId));
    }

    public void sendMessage(String conversationId, String messageText) {
        Message message = new Message();
        message.sender = getFirebaseAuth().getCurrentUser().getUid();
        message.text = messageText;
        message.timestamp = new Date().getTime();

        HashMap<String, Object> messageMap = message.toMap();
        String key = getFirebaseDatabase().getReference().child("conversations").child(conversationId).child("messages").push().getKey();

        Map<String, Object> update = new HashMap<String, Object>();
        update.put(String.format("conversations/%s/messages/%s", conversationId, key), messageMap);


        getFirebaseDatabase().getReference().updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public static ConversationLoader getINSTANCE() {
        return INSTANCE;
    }
}
