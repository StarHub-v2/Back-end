package com.example.starhub.controller;

import com.example.starhub.code.ResponseCode;
import com.example.starhub.dto.comment.CommentRequestDto;
import com.example.starhub.dto.comment.CommentResponseDto;
import com.example.starhub.dto.response.ResponseDTO;
import com.example.starhub.projection.comment.GetCommentList;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createComment(@RequestBody CommentRequestDto requestDto) {
        CommentResponseDto res = commentService.createComment(requestDto);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_COMMENT.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_CREATE_COMMENT, res));
    }

    // 댓글 목록 조회
    //쿼리변수
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> readAllComments(@RequestParam("post_id") Integer post_id) {
        List<CommentResponseDto> res = commentService.readAllComments(post_id);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_COMMENTS.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_GET_COMMENTS, res));
    }

    @PutMapping("/pick")
    public ResponseEntity<ResponseDTO> pickComment(@RequestParam List<Integer> commentIdList) {
        List<GetCommentList> res = commentService.pickComments(commentIdList);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_PICK_COMMENT.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_PICK_COMMENT, res));
    }

    @GetMapping("/pick/list")
    public ResponseEntity<ResponseDTO> getPickedComments(@RequestParam("postId") Integer postId) {
        // CommentService를 사용하여 픽한 댓글 정보를 가져오는 메서드 호출
        List<GetCommentList> res = commentService.getPickedComments(postId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_PICKED_COMMENTS.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_GET_PICKED_COMMENTS, res));
    }
}