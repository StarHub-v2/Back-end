package com.example.starhub.controller;

import com.example.starhub.code.ResponseCode;
import com.example.starhub.dto.comment.CommentRequestDto;
import com.example.starhub.dto.comment.CommentResponseDto;
import com.example.starhub.dto.response.ResponseDTO;
import com.example.starhub.entity.CommentEntity;
import com.example.starhub.projection.comment.GetCommentList;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentController(CommentService commentService, UserRepository userRepository, PostRepository postRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.postRepository=postRepository;
    }

    @PostMapping("/create")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(requestDto);
    }

    // 댓글 목록 조회
    //쿼리변수
    @GetMapping("/list")
    public List<CommentResponseDto> readAllComments(@RequestParam("post_id") Integer post_id) {
        return commentService.readAllComments(post_id);
    }

    @PutMapping("/pick")
    public ResponseEntity<ResponseDTO> pickComment(@RequestParam List<Integer> commentIdList) {
        List<GetCommentList> res = commentService.pickComments(commentIdList);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_PICK_COMMENT.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_PICK_COMMENT, res));
    }

    @GetMapping("/pick/list")
    public ResponseEntity<List<GetCommentList>> getPickedComments(@RequestParam("postId") Integer postId) {
        // CommentService를 사용하여 픽한 댓글 정보를 가져오는 메서드 호출
        List<GetCommentList> pickedComments = commentService.getPickedComments(postId);

        // ResponseEntity를 사용하여 응답 생성
        return new ResponseEntity<>(pickedComments, HttpStatus.OK);
    }

}
