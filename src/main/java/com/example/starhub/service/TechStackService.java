package com.example.starhub.service;

import com.example.starhub.dto.request.CreateTechStackRequestDto;
import com.example.starhub.dto.request.TechStackDto;
import com.example.starhub.dto.response.TechStackResponseDto;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechStackService {

    private final TechStackRepository techStackRepository;

    /**
     * 기술 스택 불러오기
     * - 누구나 불러올 수 있음
     * - 기술 스택 카테고리가 OTHER가 아닌 기술 스택 목록 불러오기
     *
     * @return 기술 스택 정보가 담긴 DTO
     */
    public List<TechStackResponseDto> getTechStack() {
        List<TechStackEntity> techStackEntities = techStackRepository.findByCategoryNot(TechCategory.OTHER);

        return techStackEntities.stream()
                .map(techStack -> TechStackResponseDto.fromEntity(techStack))
                .collect(Collectors.toList());

    }

    /**
     * 기술 스택 생성하기
     * - 관리자만 허용 가능
     *
     * @param techStackDtos 기술 스택 정보 리스트
     */
    public void createTechStack(List<TechStackDto> techStackDtos) {
        List<TechStackEntity> techStackEntities = new ArrayList<>();

        for (TechStackDto dto : techStackDtos) {
            TechStackEntity entity = TechStackEntity.builder()
                    .name(dto.getName())
                    .category(dto.getCategory()) // 카테고리 enum 처리
                    .build();
            techStackEntities.add(entity);
        }

        techStackRepository.saveAll(techStackEntities); // 여러 개 한 번에 저장
    }
}
