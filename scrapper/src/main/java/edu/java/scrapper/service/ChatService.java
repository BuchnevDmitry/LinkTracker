package edu.java.scrapper.service;

public interface ChatService {
    void registerChat(Long id);

    void deleteChat(Long id);

    boolean existChat(Long id);
}
