package com.service;

import com.domain.Message;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageManagerImpl implements MessageManager {
    private Map<Long, Message> messages = new HashMap<>();

    @Override
    public Message createMessage(Message msg) {
        Message newMsg = new Message.MessageBuilder().id(messages.size() + 1).text(msg.getText()).build();
        messages.put(newMsg.getId(), newMsg);
        return newMsg;
    }

    @Override
    public Collection<Message> getMessages() {
        return messages.values();
    }

}
