package com.example.starhub.service;

import com.example.starhub.dto.comment.CommentRequestDto;
import com.example.starhub.dto.comment.CommentResponseDto;
import com.example.starhub.entity.CommentEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.projection.comment.GetCommentList;
import com.example.starhub.repository.CommentRepository;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //@Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto){
        Integer userId = requestDto.getUserId();
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 조회 실패"));
        Integer postId = requestDto.getPostId();
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("포스트 조회 실패"));
        CommentEntity comment = new CommentEntity(user, post, requestDto);
        commentRepository.save(comment);
        //CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return new CommentResponseDto(comment);
    }


    // 전체 댓글 조회
    @Transactional
    public List<CommentResponseDto> readAllComments(Integer id) {
        return commentRepository.findAllByPostPostId(id);
    }

    @Override
    public List<GetCommentList> pickComments(List<Integer> commentIdList) {
        List<CommentEntity> commentList = commentRepository.findAllById(commentIdList);
        for (CommentEntity comment : commentList) {
            comment.setPick(true);

            PostEntity postEntity = postRepository.findById(comment.getPost().getPostId()).orElse(null);

            // PostEntity가 존재하고 있다면 done 값을 true로 설정
            if (postEntity != null) {
                postEntity.setDone(true);
                postRepository.save(postEntity);
            }
            commentRepository.save(comment);

        }

        List<CommentEntity> pickedComments = commentRepository.findAllByIdInAndPickIsTrue(commentIdList);

        List<GetCommentList> result = convertToGetCommentList(pickedComments);
        return result;
    }

    public List<GetCommentList> getPickedComments(Integer postId) {
        List<CommentEntity> pickedComments = commentRepository.findByPickIsTrueAndPostPostId(postId);
        return convertToGetCommentList(pickedComments);
    }

    private List<GetCommentList> convertToGetCommentList(List<CommentEntity> comments) {
        return comments.stream()
                .map(this::convertToGetCommentList)
                .collect(Collectors.toList());
    }

    private GetCommentList convertToGetCommentList(CommentEntity commentEntity) {
        return new GetCommentList() {

            @Override
            public UserEntity getUser() {
                return new UserEntity() {
                    @Override
                    public String getName() {
                        return commentEntity.getUser().getName();
                    }

                    @Override
                    public String getIntroduction() {
                        return commentEntity.getUser().getIntroduction();
                    }

                    @Override
                    public String getEmail() {
                        return commentEntity.getUser().getEmail();
                    }

//                    @Override
//                    public byte[] getImageData() {
//                        return commentEntity.getUser().getImageData();
//                    }

                    @Override
                    public String getPhoneNum() {
                        return commentEntity.getUser().getPhoneNum();
                    }

                    @Override
                    public int getAge() {
                        return commentEntity.getUser().getAge();
                    }
                };
            }
        };
    }


}