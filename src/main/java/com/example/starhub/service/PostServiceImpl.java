package com.example.starhub.service;

import com.example.starhub.dto.post.PostListResponseDto;
import com.example.starhub.dto.post.PostRequestDto;
import com.example.starhub.dto.post.PostResponseDto;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements  PostService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public PostResponseDto createPost(PostRequestDto requestDto){

        Integer userId = requestDto.getUserId();
        UserEntity user = userRepository.findById(userId).orElse(null);
        PostEntity post = new PostEntity(user, requestDto);
        postRepository.save(post);
        return new PostResponseDto(post);

    }

    public List<PostListResponseDto> findAllPost(){
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // 글 하나 가져오기
    public PostResponseDto findOnePost(Integer id) {
        PostEntity post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 없습니다")
        );
        return new PostResponseDto(post);
    }
}
