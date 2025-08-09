package kr.ac.dankook.SokGangPetTour.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatRoomCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatRoomResponse;
import kr.ac.dankook.SokGangPetTour.facade.ChatRoomJoinFacade;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatRoomJoinService;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatRoomService;
import kr.ac.dankook.SokGangPetTour.util.DecryptId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatRooms")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomJoinService chatRoomJoinService;
    private final ChatRoomJoinFacade chatRoomJoinFacade;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createNewChatRoom(
            @RequestBody @Valid ChatRoomCreateRequest request,
            @AuthenticationPrincipal PrincipalDetails user
    )
    {
        return ResponseEntity.status(201).body(new ApiResponse<>(true,201,
                chatRoomService.saveNewChatRoom(user.getMember(),request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChatRoomResponse>>> getAllChatRooms(
            Pageable pageable) {
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatRoomService.getAllChatRoomList(pageable)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ChatRoomResponse>>> getMyChatRooms(
            @AuthenticationPrincipal PrincipalDetails user)
    {
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatRoomService.getMyChatRoomList(user.getMember())));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ChatRoomResponse>>> searchChatRooms(
            @RequestParam("keyword") String keyword, Pageable pageable
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatRoomService.getChatRoomListByKeyword(keyword,pageable)));
    }

    @GetMapping("/join/{roomId}")
    public ResponseEntity<ApiResponse<Boolean>> isJoinChatRoom(
            @PathVariable @DecryptId Long roomId,
            @AuthenticationPrincipal PrincipalDetails user
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                chatRoomJoinService.isJoinChatRoom(roomId, user.getMember())));
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> joinChatRoom(
            @PathVariable @DecryptId Long roomId,
            @RequestParam("nickname") String nickname,
            @AuthenticationPrincipal PrincipalDetails user
    ) throws InterruptedException {
        return ResponseEntity.status(200).body(new ApiResponse<>(true,201,
                chatRoomJoinFacade.joinChatRoom(roomId,nickname,user.getMember())));
    }

    @DeleteMapping("/join/{roomId}")
    public ResponseEntity<ApiMessageResponse> leaveChatRoom(
            @PathVariable @DecryptId Long roomId,
            @AuthenticationPrincipal PrincipalDetails user
    ) throws InterruptedException {
        chatRoomJoinFacade.leaveChatRoom(roomId,user.getMember());
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "채팅방을 성공적으로 나갔습니다."));
    }
}
