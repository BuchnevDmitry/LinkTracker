package edu.java.bot.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.service.LinkService;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.util.BotUtil.LINK_MISSING;

@Slf4j
@Component
public class TrackCommand implements Command {

    private final LinkService linkService;

    public TrackCommand(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("Запрос в базу об информации по данной ссылке");
        try {
            return linkService.addLink(update);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return new SendMessage(update.message().chat().id(), LINK_MISSING);
        } catch (URISyntaxException e) {
           throw new RuntimeException(e);
        }
    }
}
