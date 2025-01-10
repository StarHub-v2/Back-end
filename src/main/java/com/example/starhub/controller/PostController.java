package com.example.starhub.controller;

import com.example.starhub.code.ResponseCode;
import com.example.starhub.dto.post.PostListResponseDto;
import com.example.starhub.dto.post.PostRequestDto;
import com.example.starhub.dto.post.PostResponseDto;
import com.example.starhub.dto.response.ResponseDTO;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createPost(@RequestBody PostRequestDto requestDto) {
        PostResponseDto res = postService.createPost(requestDto);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_POST.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_CREATE_POST, res));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> getAllPosts() {
        List<PostListResponseDto> res = postService.findAllPost();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POSTS.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_GET_POSTS, res));
    }

    //경로변수
//    @GetMapping("/detail/{post_id}")
//    public PostResponseDto getOnePost(@PathVariable("post_id") Integer post_id) {
//        return postService.findOnePost(post_id);
//    }

    //쿼리변수
    @GetMapping("/detail")
    public ResponseEntity<ResponseDTO> getOnePost(@RequestParam("post_id") Integer post_id) {
        PostResponseDto res = postService.findOnePost(post_id);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POST.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_GET_POST, res));
    }
}