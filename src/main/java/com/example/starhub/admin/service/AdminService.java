package com.example.starhub.admin.service;

import com.example.starhub.dto.request.TechStackDto;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final TechStackRepository techStackRepository;

    /**
     * 기술 스택 생성하기
     * - 관리자만 허용 가능
     *
     * @param techStackDtos 기술 스택 정보 리스트
     */
    public void createTechStack(List<TechStackDto> techStackDtos) {
        List<TechStackEntity> techStackEntities = techStackDtos.stream()
                .map(TechStackEntity::createTechStackEntity)
                .collect(Collectors.toList());

        techStackRepository.saveAll(techStackEntities); // 여러 개 한 번에 저장
    }

    /**
     * 사용하지 않는 기술 스택 불러오기
     */

    /**
     * 사용하지 않는 기술 스택 삭제하기
     * - 스케쥴링 고민
     */


}
