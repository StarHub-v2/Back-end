package com.example.starhub.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * USER
     */
    SUCCESS_CREATE_USER(HttpStatus.CREATED, "사용자 회원가입을 성공했습니다."),
    SUCCESS_CHECK_ID(HttpStatus.OK, "아이디 중복 확인이 완료되었습니다."),
    SUCCESS_CREATE_PROFILE(HttpStatus.CREATED, "프로필이 성공적으로 생성되었습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "사용자 로그인을 성공했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "사용자 로그아웃을 성공했습니다."),
    SUCCESS_REISSUE_TOKEN(HttpStatus.OK, "토큰 재발급을 성공했습니다."),

    /**
     * MYPAGE
     */
    SUCCESS_GET_PROFILE(HttpStatus.OK, "마이페이지 사용자 정보를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_PROFILE(HttpStatus.OK, "마이페이지 사용자 정보를 성공적으로 수정했습니다."),
    SUCCESS_GET_MEETINGS(HttpStatus.OK, "마이페이지 모임 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_CREATED_MEETINGS(HttpStatus.OK, "내가 작성한 모임 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_LIKED_MEETINGS(HttpStatus.OK, "내가 관심 있는 모임 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_APPLIED_MEETINGS(HttpStatus.OK, "내가 참여한 모임 목록을 성공적으로 불러왔습니다."),

    /**
     * MEETING
     */
    SUCCESS_CREATE_MEETING(HttpStatus.CREATED, "모임이 성공적으로 생성되었습니다."),
    SUCCESS_GET_MEETING_LIST(HttpStatus.OK, "모임 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_MEETING_DETAIL(HttpStatus.OK, "모임 상세 정보를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_MEETING(HttpStatus.OK, "모임이 성공적으로 수정되었습니다."),
    SUCCESS_DELETE_MEETING(HttpStatus.OK, "모임이 성공적으로 삭제되었습니다."),

    SUCCESS_CONFIRM_MEETING_MEMBER(HttpStatus.OK, "모임원이 성공적으로 확정되었습니다."),
    SUCCESS_GET_CONFIRMED_MEMBERS(HttpStatus.OK, "확정된 모임원 목록을 성공적으로 불러왔습니다."),

    /**
     * APPLICANT
     */
    SUCCESS_CREATE_APPLICANT(HttpStatus.CREATED, "지원서가 성공적으로 생성되었습니다."),
    SUCCESS_GET_APPLICANT_LIST(HttpStatus.OK, "지원서 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_APPLICANT_DETAIL(HttpStatus.OK, "지원서 상세 정보를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_APPLICANT(HttpStatus.OK, "지원서가 성공적으로 수정되었습니다."),
    SUCCESS_DELETE_APPLICANT(HttpStatus.OK, "지원서가 성공적으로 삭제되었습니다."),

    /**
     * TECH STACK
     */
    SUCCESS_CREATE_TECH_STACK(HttpStatus.CREATED, "기술 스택을 성공적으로 생성되었습니다."),
    SUCCESS_GET_TECH_STACK(HttpStatus.OK, "기술 스택을 성공적으로 불러왔습니다."),

    /**
     * LIKE
     */
    SUCCESS_CREATE_LIKE(HttpStatus.CREATED, "좋아요를 성공적으로 생성되었습니다."),
    SUCCESS_DELETE_LIKE(HttpStatus.OK, "좋아요를 성공적으로 삭제되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
