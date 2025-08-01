package kr.ac.dankook.SokGangPetTour.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest.ChatBotCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest.ChatBotRoomCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotHistoryResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotRoomResponse;
import kr.ac.dankook.SokGangPetTour.service.chatBot.ChatBotHistoryService;
import kr.ac.dankook.SokGangPetTour.service.chatBot.ChatBotRoomService;
import kr.ac.dankook.SokGangPetTour.util.DecryptId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("")
    public ResponseEntity<ApiMessageResponse> saveAllChatHistory(
            @RequestBody @Valid ChatBotCreateRequest chatBotCreateRequest){
        chatBotHistoryService.saveChatBotHistory(chatBotCreateRequest);
        return ResponseEntity.status(201).body(new ApiMessageResponse(true,201,
                "챗봇 메시지 저장을 완료하였습니다."));
    }

    @GetMapping("/rooms/{memberId}")
    public ResponseEntity<ApiResponse<List<ChatBotRoomResponse>>> getAllChatBotRoomsByMember(
            @PathVariable @DecryptId Long memberId
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatBotRoomService.getAllChatBotRoomByMember(memberId)));
    }

    @PostMapping("/room")
    public ResponseEntity<ApiResponse<ChatBotRoomResponse>> saveNewChatBotRoom(
            @RequestBody @Valid ChatBotRoomCreateRequest chatBotRoomCreateRequest
    ){
        return ResponseEntity.status(201).body(new ApiResponse<>(true,201,
                chatBotRoomService.saveNewChatBotRoom(chatBotRoomCreateRequest)));
    }

    @DeleteMapping("/room/{sessionId}")
    public ResponseEntity<ApiMessageResponse> deleteChatBotRoom(
            @PathVariable String sessionId
    ){
        chatBotRoomService.deleteChatBotRoom(sessionId);
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "챗봇방 삭제를 완료하였습니다."));
    }
}
