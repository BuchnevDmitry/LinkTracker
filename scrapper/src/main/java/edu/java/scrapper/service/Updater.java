package edu.java.scrapper.service;

import edu.java.scrapper.model.request.LinkUpdateRequest;

public interface Updater {
    void sendLinkUpdate(LinkUpdateRequest update);
}
