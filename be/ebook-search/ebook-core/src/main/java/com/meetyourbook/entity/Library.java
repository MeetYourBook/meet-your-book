package com.meetyourbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public Library(String name, LibraryType type, String url) {
        this.name = name;
        this.type = type;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Library library = (Library) o;
        return Objects.equals(name, library.name) && type == library.type
            && Objects.equals(url, library.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, url);
    }

    @Getter
    public enum LibraryType {
        CORPORATE_LIBRARY("기업"),
        PUBLIC_INSTITUTION_LIBRARY("공공기관"),
        PUBLIC_LIBRARY("공공도서관"),
        SCHOOL_LIBRARY("초중고"),
        UNIVERSITY_LIBRARY("대학"),
        APARTMENT_LIBRARY("아파트"),
        RESEARCH_INSTITUTE_LIBRARY("연구소");

        private final String description;

        LibraryType(String description) {
            this.description = description;
        }


        public static LibraryType findByDescription(String description) {
            for (LibraryType type : LibraryType.values()) {
                if (type.getDescription().equals(description)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No LibraryType found for description: " + description);
        }

    }
}
