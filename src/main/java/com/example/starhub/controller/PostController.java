package com.example.starhub.controller;

import com.example.starhub.dto.post.PostListResponseDto;
import com.example.starhub.dto.post.PostRequestDto;
import com.example.starhub.dto.post.PostResponseDto;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/post")
@RestController
public class PostController {
    private final PostService postService;

    private final UserRepository userRepository;

    public PostController(PostService postService, UserRepository userRepository){
        this.postService=postService;
        this.userRepository=userRepository;
    }
    @PostMapping("/create")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto){
        return postService.createPost(requestDto);
    }

    @GetMapping("/list")
    public List<PostListResponseDto> getAllPosts() {return postService.findAllPost();}

    //경로변수
//    @GetMapping("/detail/{post_id}")
//    public PostResponseDto getOnePost(@PathVariable("post_id") Integer post_id) {
//        return postService.findOnePost(post_id);
//    }

    //쿼리변수
    @GetMapping("/detail")
    public PostResponseDto getOnePost(@RequestParam("post_id") Integer post_id) {
        return postService.findOnePost(post_id);
    }
}
