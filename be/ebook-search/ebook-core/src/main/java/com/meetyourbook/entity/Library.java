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
