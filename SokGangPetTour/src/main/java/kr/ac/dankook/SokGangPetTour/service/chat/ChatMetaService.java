package kr.ac.dankook.SokGangPetTour.service.chat;

import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMetaService {

    private final ChatRoomRepository chatRoomRepository;

    @Async
    @Transactional
    public void updateRoomMetadataAsync(ChatMessageRequest request) {
        Long roomId = EncryptionUtil.decrypt(request.getRoomId());
        ChatRoom room = chatRoomRepository.findByIdWithPessimisticLock(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 없습니다"));
        room.updateMessages(request.getMessage());
        chatRoomRepository.save(room);
    }
}
