package edu.java.bot.api.controller;

import edu.java.bot.api.model.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotController {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void addUpdate(@RequestBody @Valid LinkUpdateRequest request) {
    }
}
