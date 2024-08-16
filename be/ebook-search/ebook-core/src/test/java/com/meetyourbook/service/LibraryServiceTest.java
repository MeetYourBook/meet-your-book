package com.meetyourbook.service;

import static com.meetyourbook.entity.Library.LibraryType.UNIVERSITY_LIBRARY;
import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.dto.LibraryCreation;
import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.LibraryType;
import com.meetyourbook.repository.LibraryRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceTest {

    @Autowired
    LibraryService libraryService;

    @Autowired
    LibraryRepository libraryRepository;

    @Test
    @DisplayName("도서관을 추가할 수 있다.")
    void createLibrary() {
        //given
        LibraryCreation request = new LibraryCreation("대학", "서울대학교", "www.snu.elibrary.com");

        //when
        Long libraryId = libraryService.createLibrary(request);
        Optional<Library> optionalLibrary = libraryRepository.findById(libraryId);

        //then
        assertThat(optionalLibrary)
            .isPresent()
            .get()
            .extracting(
                Library::getName,
                Library::getType,
                library -> library.getLibraryUrl().getUrl()
            )
            .containsExactly(
                "서울대학교",
                UNIVERSITY_LIBRARY,
                "www.snu.elibrary.com"
            );

    }


}