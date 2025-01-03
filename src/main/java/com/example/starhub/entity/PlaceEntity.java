package com.example.starhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length=255)
    private String placeName;

    @Column(nullable = false, length=20)
    private Float latitude;

    @Column(nullable = false, length=20)
    private Float longtitude;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) // 또는 CascadeType.PERSIST 설정 가능
    @JsonIgnore
    @JoinColumn(name = "place_id")
    private PostEntity post;

}
