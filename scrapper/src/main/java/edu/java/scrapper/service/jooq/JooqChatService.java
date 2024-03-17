package edu.java.scrapper.service.jooq;

import edu.java.scrapper.api.exception.NotFoundException;
import edu.java.scrapper.api.exception.ResourceAlreadyExistsException;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.service.ChatService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JooqChatService implements ChatService {
    private final ChatRepository chatRepository;

    public JooqChatService(@Qualifier("jooqChatRepository") ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(Long id, AddChatRequest chat) {
        if (!exist(id)) {
            chatRepository.add(id, chat);
        } else {
            throw new ResourceAlreadyExistsException("Чат уже зарегистрирован");
        }
    }

    @Override
    public void unregister(Long id) {
        if (exist(id)) {
            chatRepository.remove(id);
        } else {
            throw new NotFoundException("Чат не существует");
        }
    }

    @Override
    public boolean exist(Long id) {
        return chatRepository.exist(id);
    }
}
