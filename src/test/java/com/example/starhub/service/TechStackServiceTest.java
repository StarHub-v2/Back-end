package com.example.starhub.service;

import com.example.starhub.dto.request.TechStackDto;
import com.example.starhub.dto.response.TechStackResponseDto;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TechStackServiceTest {

    @Autowired
    private TechStackService techStackService;

    @Autowired
    private TechStackRepository techStackRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 초기화
        techStackRepository.deleteAll();

        // 기본 데이터 추가
        saveTechStack("React", TechCategory.FRONTEND);
        saveTechStack("Spring", TechCategory.BACKEND);
        saveTechStack("React  ", TechCategory.MOBILE);
        saveTechStack("OtherTool", TechCategory.OTHER);

    }

    @Test
    void getTechStack_shouldReturnTechStackList_whenNotEmpty() {
        // When
        List<TechStackResponseDto> techStacks = techStackService.getTechStack();

        // Then
        assertEquals(3, techStacks.size());
        assertTrue(techStacks.stream().allMatch(stack -> stack.getCategory() != TechCategory.OTHER));
    }

    @Test
    void getTechStack_shouldReturnEmptyList_whenNoDataExists() {
        // Given
        techStackRepository.deleteAll();

        // When
        List<TechStackResponseDto> techStacks = techStackService.getTechStack();

        // Then
        assertTrue(techStacks.isEmpty());
    }

    @Test
    void createTechStack_shouldSaveTechStacksSuccessfully() {
        // Given
        List<TechStackDto> techStackDtos = List.of(
                buildTechStackDto("Vue", TechCategory.FRONTEND),
                buildTechStackDto("Django", TechCategory.BACKEND),
                buildTechStackDto("Swift", TechCategory.MOBILE)
        );

        // When
        techStackService.createTechStack(techStackDtos);

        // Then
        List<TechStackEntity> savedEntities = techStackRepository.findAll();
        assertEquals(7, savedEntities.size()); // 기존 4개 + 추가 3개
        assertTrue(savedEntities.stream().anyMatch(stack -> stack.getName().equals("Vue")));
        assertTrue(savedEntities.stream().anyMatch(stack -> stack.getName().equals("Django")));
        assertTrue(savedEntities.stream().anyMatch(stack -> stack.getName().equals("Swift")));
    }

    @Test
    void createTechStack_shouldHandleEmptyList() {
        // Given
        List<TechStackDto> techStackDtos = new ArrayList<>(); // 빈 리스트

        // When
        techStackService.createTechStack(techStackDtos);

        // Then
        List<TechStackEntity> savedEntities = techStackRepository.findAll();
        assertEquals(4, savedEntities.size());
    }

    @Test
    void createTechStack_shouldThrowException_whenInvalidDataProvided() {
        // Given
        List<TechStackDto> techStackDtos = List.of(
                buildTechStackDto(null, TechCategory.FRONTEND), // 이름이 null
                buildTechStackDto("InvalidCategory", null)  // 카테고리가 null
        );

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            techStackService.createTechStack(techStackDtos);
        });
    }

    private TechStackEntity saveTechStack(String name, TechCategory category) {
        return techStackRepository.save(TechStackEntity.createTechStackEntity(buildTechStackDto(name, category)));
    }

    private TechStackDto buildTechStackDto(String name, TechCategory category) {
        return TechStackDto.builder()
                .name(name)
                .category(category)
                .build();
    }

}