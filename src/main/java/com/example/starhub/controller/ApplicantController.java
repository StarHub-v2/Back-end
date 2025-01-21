package com.example.starhub.controller;

import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;

    /**
     * 지원서 작성
     */
    @PostMapping("/applicants")
    public ResponseEntity<ResponseDto> createApplicant() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_APPLICANT, null));
    }

    /**
     * 지원서 목록 불러오기
     */
    @GetMapping("/applicants")
    public ResponseEntity<ResponseDto> getApplicantList() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_LIST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_LIST, null));
    }


    /**
     * 지원서 상세 불러오기
     */
    @GetMapping("/applicants/{id}")
    public ResponseEntity<ResponseDto> getApplicantDetail() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL, null));
    }

    /**
     * 지원서 수정하기
     */
    @PatchMapping("/applicants/{id}")
    public ResponseEntity<ResponseDto> updateApplicant() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_UPDATE_APPLICANT, null));
    }

    /**
     * 지원서 삭제하기
     */
    @DeleteMapping("/applicants/{id}")
    public ResponseEntity<ResponseDto> deleteApplicant() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_DELETE_APPLICANT, null));
    }
}
