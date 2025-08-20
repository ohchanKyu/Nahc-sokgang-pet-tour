package kr.ac.dankook.SokGangPetTour.facade;

import kr.ac.dankook.SokGangPetTour.dto.response.chatResponse.ChatRoomResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatRoomJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatRoomJoinFacade {

    private final ChatRoomJoinService chatRoomJoinService;
    private static final int RETRY_DELAY_MS = 50;
    private static final int MAX_RETRY_COUNT = 20;

    public ChatRoomResponse joinChatRoom(Long chatRoomId, String nickname, Member member) throws InterruptedException {
        int retryCount = 0;
        while(retryCount < MAX_RETRY_COUNT) {
            try{
                return chatRoomJoinService.joinChatRoom(chatRoomId,nickname, member);
            }catch (Exception e){
                if (e instanceof CustomException customException){
                    throw new CustomException(customException.getErrorCode());
                }
                retryCount++;
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw new CustomException(ErrorCode.DELAY_JOIN_CHATROOM);
    }

    public void leaveChatRoom(Long chatRoomId, Member member) throws InterruptedException {
        int retryCount = 0;
        while(retryCount < MAX_RETRY_COUNT) {
            try{
                chatRoomJoinService.leaveChatRoom(chatRoomId, member);
                return;
            }catch (Exception e){
                if (e instanceof CustomException customException){
                    throw new CustomException(customException.getErrorCode());
                }
                retryCount++;
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw new CustomException(ErrorCode.DELAY_LEAVE_CHATROOM);
    }
}
