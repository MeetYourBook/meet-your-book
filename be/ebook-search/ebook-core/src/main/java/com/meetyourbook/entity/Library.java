package com.meetyourbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String name;
    @Enumerated(EnumType.STRING)
    LibraryType type;
    String url;

    public enum LibraryType {
        CORPORATE_LIBRARY,
        PUBLIC_INSTITUTION_LIBRARY,
        PUBLIC_LIBRARY,
        SCHOOL_LIBRARY,
        UNIVERSITY_LIBRARY,
        APARTMENT_LIBRARY,
        RESEARCH_INSTITUTE_LIBRARY
    }
}
