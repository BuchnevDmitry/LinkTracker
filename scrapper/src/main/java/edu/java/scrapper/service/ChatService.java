package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private static final String CHAT_NOT_EXIST = "Чат не существует";

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    public void register(Long id, AddChatRequest chat) {
        if (!exists(id)) {
            chatRepository.add(id, chat);
        } else {
            throw new ResourceAlreadyExistsException("Чат уже зарегистрирован");
        }
    }

    @Transactional
    public void unregister(Long id) {
        if (exists(id)) {
            chatRepository.remove(id);
        } else {
            throw new NotFoundException(CHAT_NOT_EXIST);
        }
    }

    public boolean exists(Long id) {
        return chatRepository.exists(id);
    }

    public Chat getChat(Long id) {
        return chatRepository.findChatById(id).orElseThrow(() -> new NotFoundException(CHAT_NOT_EXIST));
    }
}
