package edu.java.scrapper.domain.jpa;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.model.request.AddChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long>, ChatRepository {

    @Override
    default void add(Long id, AddChatRequest chatRequest) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setCreatedBy(chatRequest.createdBy());
        this.saveAndFlush(chat);
    }

    @Override
    default void remove(Long id) {
        this.deleteById(id);
    }

    @Override
    default boolean exists(Long chatId) {
        return this.existsById(chatId);
    }
}
