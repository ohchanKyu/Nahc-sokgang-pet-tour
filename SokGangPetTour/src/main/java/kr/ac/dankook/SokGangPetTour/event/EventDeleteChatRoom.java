package kr.ac.dankook.SokGangPetTour.event;

import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventDeleteChatRoom {
    private final ChatRoom targetRoom;
}
