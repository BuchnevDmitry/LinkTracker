package edu.java.scrapper.service;

import edu.java.scrapper.model.request.AddChatRequest;

public interface ChatService {
    void register(Long id, AddChatRequest chat);

    void unregister(Long id);

    boolean exist(Long id);

}
