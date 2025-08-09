package kr.ac.dankook.SokGangPetTour.event;

import jakarta.persistence.EntityManager;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatMessageRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomParticipantRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDeleteChatRoomEventListener {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final EntityManager entityManager;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    // 삭제 후처리
    public void handleDeleteChatRoom(EventDeleteChatRoom eventDeleteChatRoom){

        ChatRoom chatRoom = eventDeleteChatRoom.getTargetRoom();
        String encryptId = EncryptionUtil.encrypt(chatRoom.getId());
        // 참가자 모두 삭제
        List<ChatRoomParticipant> participants = chatRoomParticipantRepository
                .findByChatRoom(chatRoom);
        // 삭제 Bulk 연산 진행
        entityManager.flush();
        chatRoomParticipantRepository.deleteAllInBatch(participants);
        entityManager.clear();
        // 채팅 삭제
        List<ChatMessage> messages = chatMessageRepository.findByRoomId(encryptId);
        chatMessageRepository.deleteAll(messages);
        // 채팅방 삭제
        chatRoomRepository.delete(chatRoom);
    }
}
