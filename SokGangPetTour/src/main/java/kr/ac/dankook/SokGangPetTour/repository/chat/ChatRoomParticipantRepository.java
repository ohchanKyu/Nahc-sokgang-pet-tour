package kr.ac.dankook.SokGangPetTour.repository.chat;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant,Long> {

    Optional<ChatRoomParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    @Query("select p from ChatRoomParticipant p JOIN FETCH p.chatRoom where p.member = :member")
    List<ChatRoomParticipant> findByMemberWithFetchJoin(@Param("member") Member member);
}
