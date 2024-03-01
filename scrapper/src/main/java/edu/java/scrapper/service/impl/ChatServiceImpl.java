package edu.java.scrapper.service.impl;

import edu.java.scrapper.api.exception.AlreadyExistsException;
import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.service.ChatService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final Map<Long, Long> chats = new HashMap<>();

    @Override
    public void registerChat(Long id) {
        if (!existChat(id)) {
            chats.put(id, id);
        } else {
            throw new AlreadyExistsException("Чат уже зарегистрирован");
        }
    }

    @Override
    public void deleteChat(Long id) {
        if (existChat(id)) {
            chats.remove(id);
        } else {
            throw new NotFoundException("Чат не существует");
        }
    }

    @Override
    public boolean existChat(Long id) {
        return chats.containsKey(id);
    }
}
