package kr.ac.dankook.SokGangPetTour.service.chatBot;

import kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest.ChatBotRoomCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotRoomResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotRoomRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotRoomService {

    private final ChatBotRoomRepository chatBotRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatBotHistoryService chatBotHistoryService;

    @Transactional
    public ChatBotRoomResponse saveNewChatBotRoom(ChatBotRoomCreateRequest request){

        Long decryptId = EncryptionUtil.decrypt(request.getMemberId());
        Member member = memberRepository.findById(decryptId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        ChatBotRoom chatBotRoom = ChatBotRoom.builder()
                .title(request.getTitle()).member(member).build();
        ChatBotRoom newEntity = chatBotRoomRepository.save(chatBotRoom);
        return covertToDtoResponse(newEntity);
    }

    @Transactional
    public List<ChatBotRoomResponse> getAllChatBotRoomByMember(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        List<ChatBotRoom> lists = chatBotRoomRepository.findChatBotRoomByMember(member);
        return lists.stream().map(this::covertToDtoResponse).toList();
    }

    @Transactional
    public void deleteChatBotRoom(String sessionId){
        Long decryptId = EncryptionUtil.decrypt(sessionId);
        chatBotRoomRepository.deleteById(decryptId);
        chatBotHistoryService.deleteChatBotHistory(sessionId);
    }

    private ChatBotRoomResponse covertToDtoResponse(ChatBotRoom chatBotRoom){
        return ChatBotRoomResponse.builder()
                .roomId(EncryptionUtil.encrypt(chatBotRoom.getId()))
                .title(chatBotRoom.getTitle())
                .lastMessage(chatBotRoom.getLastMessage())
                .lastMessageTime(chatBotRoom.getLastMessageTime()).build();
    }
}
