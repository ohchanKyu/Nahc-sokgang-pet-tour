package kr.ac.dankook.SokGangPetTour.service.chat;

import kr.ac.dankook.SokGangPetTour.config.redis.RedisChatPublisher;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatMessageRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomParticipantRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisChatPublisher redisChatPublisher;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void sendChatMessage(ChatMessageRequest request){
        redisChatPublisher.publish(request);
    }

    @Transactional
    public void updateUnreadMessages(Long roomId, Member member){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatRoomParticipant participant = chatRoomParticipantRepository
                .findByChatRoomAndMember(chatRoom,member)
                .orElseThrow(() -> new EntityNotFoundException("채팅방 참가자를 찾을 수 없습니다."));
        participant.setCurrentReadNumber(chatRoom.getLastMessageNumber());
        chatRoomParticipantRepository.save(participant);
    }

    @Transactional
    public void saveChatMessage(ChatMessageRequest request){

        ChatMessage chatMessage = ChatMessage.builder()
                .memberId(request.getMemberId())
                .roomId(request.getRoomId())
                .content(request.getMessage()).build();
        chatMessageRepository.save(chatMessage);
    }

    public Page<ChatResponse> getAllChats(String roomId, Pageable pageable){
        Page<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId, pageable);
        return chatMessages.map(ChatResponse::new);
    }
}
