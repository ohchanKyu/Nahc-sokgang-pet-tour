package kr.ac.dankook.SokGangPetTour.event;

import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
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
public class EventDeleteChatRoomEventListener {

    private final ChatRoomRepository chatRoomRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    // 삭제 후처리
    public void handleDeleteChatRoom(EventDeleteChatRoom eventDeleteChatRoom){

    }
}
