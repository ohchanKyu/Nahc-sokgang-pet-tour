package kr.ac.dankook.SokGangPetTour.service.chat;

import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatRoomCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatRoomResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomParticipantRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Transactional
    public ChatRoomResponse saveNewChatRoom(Member member,ChatRoomCreateRequest chatRoomCreateRequest) {

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomCreateRequest.getName())
                .description(chatRoomCreateRequest.getDescription())
                .maxParticipants(chatRoomCreateRequest.getMaxParticipants())
                .createdMember(member).build();
        ChatRoom newEntity = chatRoomRepository.save(chatRoom);

        // 채팅방 참여자 생성
        ChatRoomParticipant participant = ChatRoomParticipant.builder()
                .chatRoom(newEntity).member(member).build();
        chatRoomParticipantRepository.save(participant);
        return new ChatRoomResponse(newEntity);
    }

    public List<ChatRoomResponse> getMyChatRoomList(Member member) {
        List<ChatRoomParticipant> chatRooms = chatRoomParticipantRepository
                .findByMemberWithFetchJoin(member);
        return chatRooms.stream().map(ChatRoomResponse::new).toList();
    }

    public Page<ChatRoomResponse> getAllChatRoomList(Pageable pageable){
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
        return chatRooms.map(ChatRoomResponse::new);
    }

    public Page<ChatRoomResponse> getChatRoomListByKeyword(String keyword,Pageable pageable){
        Page<ChatRoom> chatRooms = chatRoomRepository.searchByKeyword(keyword,pageable);
        return chatRooms.map(ChatRoomResponse::new);
    }
}
