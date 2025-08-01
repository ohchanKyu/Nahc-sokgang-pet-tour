package kr.ac.dankook.SokGangPetTour.service.chatBot.event;

import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotRoomRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventChatBotUpdateEventListener {

    private final ChatBotRoomRepository chatBotRoomRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventChatBotUpdate(EventChatBotUpdateEvent event){

        Long decryptId = EncryptionUtil.decrypt(event.getSessionId());
        ChatBotRoom chatBotRoom = chatBotRoomRepository.findById(decryptId)
                .orElseThrow(() -> new EntityNotFoundException("챗봇방 정보를 찾을 수 없습니다."));
        chatBotRoom.updateLastMessage(event.getMessage());
        chatBotRoomRepository.save(chatBotRoom);
    }
}
