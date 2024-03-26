package edu.java.scrapper.domain;

import edu.java.scrapper.model.request.AddChatRequest;
import edu.java.scrapper.model.response.ChatResponse;
import java.util.List;

public interface ChatRepository {
    List<ChatResponse> findAll();

    void add(Long id, AddChatRequest chat);

    void remove(Long id);

    boolean exists(Long chatId);
}
