package io.pet2share.pet2share.interfaces.loader;

import java.util.ArrayList;

import io.pet2share.pet2share.model.conversation.Conversation;

/**
 * Created by Bausch on 07.11.2016.
 */

public interface ConversationLoaderInterface {
    void loadConversations(ArrayList<Conversation> conversationArrayList);
}
