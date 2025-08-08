package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.service.auth.MemberService;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatMetaService;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatService;
import kr.ac.dankook.SokGangPetTour.util.DecryptId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ChatMetaService chatMetaService;
    private final MemberService memberService;

    // 사용자 -> WebSocket -> MessageMapping Controller
    // -> RedisPublisher.publish() -> Redis Channel("chatRoom")
    // -> RedisMessageListenerContainer -> RedisSubscriber (MessageListenerAdapter)
    // -> SimpMessageTemplate -> 모든 WebSocket 구독자에게 전송
    @MessageMapping("/chat/message")
    public void sendMessage(
            @Payload ChatMessageRequest request) {
        chatService.saveChatMessage(request);
        chatMetaService.updateRoomMetadataAsync(request);
        chatService.sendChatMessage(request);
    }

    @PatchMapping("/api/v1/chat/count/{roomId}")
    public ResponseEntity<ApiMessageResponse> clearUnreadMessages(
            @PathVariable @DecryptId Long roomId,
            @AuthenticationPrincipal User user
    ){
        Member member = memberService.getCurrentMember(user.getUsername());
        chatService.updateUnreadMessages(roomId,member);
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "읽지 않은 메시지를 초기화하였습니다."));
    }

    @GetMapping("/api/v1/chat/messages/{roomId}")
    public ResponseEntity<ApiResponse<Page<ChatResponse>>> getAllChatMessages(
            @PathVariable String roomId,
            Pageable pageable
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatService.getAllChats(roomId,pageable)));
    }
}
