package kr.ac.dankook.SokGangPetTour.service.chat;

import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatRoomResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomStatus;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.event.EventDeleteChatRoom;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomParticipantRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomJoinService {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    public boolean isJoinChatRoom(Long roomId, Member member){

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        Optional<ChatRoomParticipant> participant = chatRoomParticipantRepository
                .findByChatRoomAndMember(chatRoom,member);

        return participant.isPresent();
    }

    @Transactional
    public ChatRoomResponse joinChatRoom(Long roomId, Member member){

        ChatRoom chatRoom = chatRoomRepository.findByIdWithOptimisticLock(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        if(isJoinChatRoom(roomId,member)){
            throw new CustomException(ErrorCode.ALREADY_JOIN_CHATROOM);
        }
        chatRoom.increaseParticipants();
        chatRoomRepository.saveAndFlush(chatRoom);

        ChatRoomParticipant newParticipant = ChatRoomParticipant.builder()
                .chatRoom(chatRoom).member(member).build();
        chatRoomParticipantRepository.save(newParticipant);
        return new ChatRoomResponse(chatRoom);
    }

    @Transactional
    public void leaveChatRoom(Long roomId, Member member){

        ChatRoom chatRoom = chatRoomRepository.findByIdWithOptimisticLock(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        ChatRoomParticipant participant = chatRoomParticipantRepository
                .findByChatRoomAndMember(chatRoom, member).orElseThrow(() -> new EntityNotFoundException("참고하고 있는 사용자를 찾을 수 없습니다."));
        int currentCnt = chatRoom.decreaseParticipants();
        if (currentCnt <= 0){
            eventPublisher.publishEvent(new EventDeleteChatRoom(chatRoom));
            return;
        }
        if(chatRoom.getMember().getId().equals(member.getId())){
            chatRoom.updateChatRoomStatus(ChatRoomStatus.READ_ONLY);
        }
        chatRoomRepository.save(chatRoom);
        chatRoomParticipantRepository.delete(participant);
    }
}
