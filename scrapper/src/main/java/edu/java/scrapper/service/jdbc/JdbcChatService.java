package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class JdbcChatService implements ChatService {

    private final ChatRepository chatRepository;

    public JdbcChatService(@Qualifier("jdbcChatRepository") ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public void register(Long id, AddChatRequest chat) {
        if (!exist(id)) {
            chatRepository.add(id, chat);
        } else {
            throw new ResourceAlreadyExistsException("Чат уже зарегистрирован");
        }
    }

    @Override
    @Transactional
    public void unregister(Long id) {
        if (exist(id)) {
            chatRepository.remove(id);
        } else {
            throw new NotFoundException("Чат не существует");
        }
    }

    @Override
    @Transactional
    public boolean exist(Long id) {
        return chatRepository.exist(id);
    }
}
