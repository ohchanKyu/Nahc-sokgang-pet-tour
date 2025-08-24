package kr.ac.dankook.SokGangPetTour.service.auth;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.facade.ChatRoomJoinFacade;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import kr.ac.dankook.SokGangPetTour.repository.chat.ChatRoomParticipantRepository;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotHistoryRepository;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotRoomRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatBotRoomRepository chatBotRoomRepository;
    private final ChatBotHistoryRepository chatBotHistoryRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatRoomJoinFacade chatRoomJoinFacade;

    public Member getCurrentMember(String userId){
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void updatePassword(Member member, String newPassword){
        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    @Transactional
    public boolean editMemberPassword(Member member, String originalPassword, String newPassword){
        if (!passwordEncoder.matches(originalPassword, member.getPassword())) return false;
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedNewPassword);
        memberRepository.save(member);
        return true;
    }

    @Transactional
    public void editMemberInfo(Member member,String name,String email){
        member.updateMemberInfo(name,email);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Member member) throws InterruptedException {
        List<ChatBotRoom> chatBotRooms = chatBotRoomRepository.findChatBotRoomByMember(member);
        // 챗봇 삭제
        for(ChatBotRoom chatBotRoom : chatBotRooms){
            chatBotHistoryRepository.deleteBySessionId(EncryptionUtil.encrypt(chatBotRoom.getId()));
            chatBotRoomRepository.delete(chatBotRoom);
        }
        // 채팅 참가자 정보 삭제
        List<ChatRoomParticipant> participants =  chatRoomParticipantRepository.findByMemberWithFetchJoin(member);
        for(ChatRoomParticipant p : participants){
            chatRoomJoinFacade.leaveChatRoom(p.getChatRoom().getId(),member);
        }
        member.convertToDeletedMember();
        memberRepository.save(member);
    }
}
