package com.meetyourbook.entity;

import com.meetyourbook.dto.LibraryUpdateInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class Library extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private LibraryType type;

    @Embedded
    @Column(unique = true)
    private LibraryUrl libraryUrl;

    @Enumerated(EnumType.STRING)
    private EbookPlatform ebookPlatform;

    private int totalBookCount;

    @Builder
    public Library(String name, LibraryType type, String libraryUrl,
        EbookPlatform ebookPlatform, int totalBookCount) {
        this.name = name;
        this.type = type;
        this.libraryUrl = new LibraryUrl(libraryUrl);
        this.ebookPlatform = ebookPlatform;
        this.totalBookCount = totalBookCount;
    }

    public void updateTotalBookCount(int totalBookCount) {
        this.totalBookCount = totalBookCount;
    }

    public boolean hasMainInk() {
        return libraryUrl.hasMainInk();
    }

    public String getUrlWithQueryParameters(int viewCnt) {
        return libraryUrl.getUrlWithQueryParameters(viewCnt);
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
            && Objects.equals(libraryUrl, library.libraryUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, libraryUrl);
    }

    public void update(LibraryUpdateInfo libraryUpdateInfo) {
        this.name = libraryUpdateInfo.name();
        this.type = LibraryType.findByDescription(libraryUpdateInfo.category());
        this.libraryUrl = new LibraryUrl(libraryUpdateInfo.url());
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
            throw new IllegalArgumentException(
                "No LibraryType found for description: " + description);
        }

    }

    @Getter
    public enum EbookPlatform {
        KYOBO("교보문고"),
        YES24("예스24"),
        BOOKCUBE("북큐브"),
        ALADIN("알라딘");

        private final String description;

        EbookPlatform(String description) {
            this.description = description;
        }

    }
}
