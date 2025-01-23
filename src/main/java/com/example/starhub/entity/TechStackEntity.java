package com.example.starhub.entity;

import com.example.starhub.dto.request.CreateMeetingRequestDto;
import com.example.starhub.dto.request.CreateTechStackRequestDto;
import com.example.starhub.dto.request.TechStackDto;
import com.example.starhub.entity.enums.TechCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TechStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기술 스택 고유 식별자

    @Column(nullable = false, unique = true, length = 50)
    private String name; // 기술 스택 이름

    @Enumerated(EnumType.STRING)
    private TechCategory category;  // 카테고리

    public static TechStackEntity createTechStackEntity(TechStackDto techStackDto) {
        return TechStackEntity.builder()
                .name(techStackDto.getName())
                .category(techStackDto.getCategory())
                .build();
    }
}
