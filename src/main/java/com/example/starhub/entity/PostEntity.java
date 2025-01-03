package com.example.starhub.entity;

import com.example.starhub.dto.post.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId; // 포스트 id

    @Column(nullable = false, length=20)
    private String skill;

    @Column(nullable = false, length=50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length=20)
    private String progress;

    @Column(nullable = false, length=10)
    private String peopleNum;

    @Column(nullable = false, length=20)
    private String deadline;

    @CreatedDate
    private LocalDate createdAt;

    @Column(nullable = false, length=20)
    private String type;

    @Column(nullable = false, length=255)
    private String place;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column
    private Boolean done;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST) // 또는 CascadeType.PERSIST 설정 가능
    @JsonIgnore
    @JoinColumn(name = "userId")
    private UserEntity user;

//    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY) // 양방향 관계 매핑된 것. 읽기 전용
//    @JsonIgnore
//    private List<CommentEntity> comments = new ArrayList<>();

    public PostEntity(UserEntity user, PostRequestDto requestDto) {
        this.skill=requestDto.getSkill();
        this.place=requestDto.getPlace();
        this.longitude=requestDto.getLongitude();
        this.latitude=requestDto.getLatitude();
        this.progress=requestDto.getProgress();
        this.peopleNum=requestDto.getPeopleNum();
        this.deadline=requestDto.getDeadline();
        this.type=requestDto.getType();
        this.done=requestDto.getDone();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.createdAt=requestDto.getCreatedAt();
        this.user = user;
    }


}