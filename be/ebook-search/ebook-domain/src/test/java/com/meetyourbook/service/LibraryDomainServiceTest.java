package com.meetyourbook.service;

import static com.meetyourbook.entity.Library.LibraryType.PUBLIC_LIBRARY;
import static com.meetyourbook.entity.Library.LibraryType.UNIVERSITY_LIBRARY;
import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.common.RepositoryTest;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateInfo;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.LibraryRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class LibraryDomainServiceTest {

    private LibraryDomainService libraryDomainService;

    @Autowired
    private LibraryRepository libraryRepository;

    @BeforeEach
    void setUp() {
        libraryDomainService = new LibraryDomainService(libraryRepository);
    }

    @AfterEach
    void tearDown() {
        libraryRepository.deleteAll();
    }

    @Test
    @DisplayName("도서관을 추가할 수 있다.")
    void createLibrary() {
        //given
        LibraryCreationInfo request = new LibraryCreationInfo("서울대학교",
            "www.snu.elibrary.com");

        //when
        Long libraryId = libraryDomainService.createLibrary(request);
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
                null,
                "www.snu.elibrary.com"
            );
    }

    @Test
    @DisplayName("ID로 도서관 정보를 조회할 수 있다.")
    void findById() {
        //given
        Library library = Library.builder()
            .name("서울대학교")
            .type(UNIVERSITY_LIBRARY)
            .totalBookCount(1000)
            .build();
        Library save = libraryRepository.save(library);

        //when
        LibraryResponse response = libraryDomainService.findById(save.getId());

        //then
        assertThat(response).extracting(
                LibraryResponse::id,
                LibraryResponse::name
            )
            .containsExactly(
                library.getId(),
                library.getName()
            );
    }

    @Test
    @DisplayName("도서관 ID에 해당하는 도서관을 삭제할 수 있다.")
    void delete() {
        //given
        Library library = Library.builder()
            .name("서울대학교")
            .type(UNIVERSITY_LIBRARY)
            .totalBookCount(1000)
            .build();
        Library save = libraryRepository.save(library);

        //when
        libraryDomainService.delete(save.getId());

        //then
        assertThat(libraryRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("도서관 정보를 업데이트할 수 있다.")
    void update() {
        //given
        Library library = Library.builder()
            .name("경기도서관")
            .type(UNIVERSITY_LIBRARY)
            .totalBookCount(1000)
            .build();
        Library save = libraryRepository.save(library);

        Long id = save.getId();
        LibraryUpdateInfo updateInfo = new LibraryUpdateInfo("경기도서관", "공공도서관",
            "www.example.com");

        //when
        libraryDomainService.updateLibrary(id, updateInfo);
        Optional<Library> optionalLibrary = libraryRepository.findById(id);

        //then
        assertThat(optionalLibrary)
            .isPresent()
            .get()
            .extracting(
                lib -> lib.getName(),
                lib -> lib.getType(),
                lib -> lib.getLibraryUrl().getUrl()
            )
            .containsExactly(
                "경기도서관",
                PUBLIC_LIBRARY,
                "www.example.com"
            );

    }


}