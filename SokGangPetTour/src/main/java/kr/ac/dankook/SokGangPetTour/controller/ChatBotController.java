package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotHistoryResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotRoomResponse;
import kr.ac.dankook.SokGangPetTour.service.chatBot.ChatBotHistoryService;
import kr.ac.dankook.SokGangPetTour.service.chatBot.ChatBotRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatbot")
public class ChatBotController {

    private final ChatBotHistoryService chatBotHistoryService;
    private final ChatBotRoomService chatBotRoomService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<List<ChatBotHistoryResponse>>> getAllChatHistory(
            @PathVariable String sessionId
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatBotHistoryService.getAllChatBotHistory(sessionId)));
    }

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<List<ChatBotRoomResponse>>> getAllChatBotRoomsByMember(
            @AuthenticationPrincipal PrincipalDetails user
        ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatBotRoomService.getAllChatBotRoomByMember(user.getMember())));
    }

    @PostMapping("/room")
    public ResponseEntity<ApiResponse<ChatBotRoomResponse>> saveNewChatBotRoom(
            @RequestParam("title") String title,
            @AuthenticationPrincipal PrincipalDetails user
    ){
        return ResponseEntity.status(201).body(new ApiResponse<>(true,201,
                chatBotRoomService.saveNewChatBotRoom(user.getMember(), title)));
    }

    @DeleteMapping("/room/{sessionId}")
    public ResponseEntity<ApiMessageResponse> deleteChatBotRoom(
            @PathVariable String sessionId
    ){
        chatBotRoomService.deleteChatBotRoom(sessionId);
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "챗봇방 삭제를 완료하였습니다."));
    }

    @PatchMapping("/room/{sessionId}/{title}")
    public ResponseEntity<ApiResponse<Boolean>> updateChatBotRoom(
            @PathVariable String sessionId, @PathVariable String title
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatBotRoomService.updateChatBotRoom(sessionId,title)));
    }
}
