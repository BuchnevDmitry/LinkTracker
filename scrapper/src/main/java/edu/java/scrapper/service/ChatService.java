package edu.java.scrapper.service;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    @Qualifier("jdbcChatRepository") private final ChatRepository chatRepository;

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
            throw new NotFoundException("Чат не существует");
        }
    }

    public boolean exists(Long id) {
        return chatRepository.exists(id);
    }
}
