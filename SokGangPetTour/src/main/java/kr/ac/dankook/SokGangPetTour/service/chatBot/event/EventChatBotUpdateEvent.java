package kr.ac.dankook.SokGangPetTour.service.chatBot.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventChatBotUpdateEvent {

    private final String sessionId;
    private final String message;
}
