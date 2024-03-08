package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final Map<Long, Long> chats = new HashMap<>();

    public void registerChat(Long id) {
        if (!chatExists(id)) {
            chats.put(id, id);
        } else {
            throw new ResourceAlreadyExistsException("Чат уже зарегистрирован");
        }
    }

    public void deleteChat(Long id) {
        if (chatExists(id)) {
            chats.remove(id);
        } else {
            throw new NotFoundException("Чат не существует");
        }
    }

    public boolean chatExists(Long id) {
        return chats.containsKey(id);
    }
}
