package com.meetyourbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryCreationResult;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateInfo;
import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.EbookPlatform;
import com.meetyourbook.entity.Library.LibraryType;
import com.meetyourbook.repository.jpa.LibraryRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LibraryDomainServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryDomainService libraryDomainService;

    @Test
    @DisplayName("도서관을 추가할 수 있다.")
    void createLibrary() {
        // Given
        LibraryCreationInfo request = new LibraryCreationInfo("서울대학교", "www.snu.elibrary.com",
            EbookPlatform.KYOBO);
        Library savedLibrary = Library.builder()
            .name("서울대학교")
            .libraryUrl("www.snu.elibrary.com")
            .ebookPlatform(EbookPlatform.KYOBO)
            .build();

        when(libraryRepository.save(any(Library.class))).thenReturn(savedLibrary);

        // When
        libraryDomainService.createLibrary(request);

        // Then
        verify(libraryRepository).save(argThat(library ->
            library.getName().equals("서울대학교") &&
                library.getLibraryUrl().getUrl().equals("www.snu.elibrary.com") &&
                library.getEbookPlatform() == EbookPlatform.KYOBO
        ));
    }

    @Test
    @DisplayName("ID로 도서관 정보를 조회할 수 있다.")
    void findById() {
        // Given
        Long libraryId = 1L;
        Library library = Library.builder()
            .name("서울대학교")
            .totalBookCount(1000)
            .build();

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        // When
        LibraryResponse response = libraryDomainService.findById(libraryId);

        // Then
        assertThat(response.name()).isEqualTo("서울대학교");
    }

    @Test
    @DisplayName("도서관 ID에 해당하는 도서관을 삭제할 수 있다.")
    void delete() {
        // Given
        Long libraryId = 1L;
        Library library = Library.builder()
            .name("서울대학교")
            .totalBookCount(1000)
            .build();

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        // When
        libraryDomainService.delete(libraryId);

        // Then
        verify(libraryRepository).delete(library);
    }

    @Test
    @DisplayName("도서관 정보를 업데이트할 수 있다.")
    void updateLibrary() {
        // Given
        Long libraryId = 1L;
        Library existingLibrary = Library.builder()
            .name("경기도서관")
            .type(LibraryType.UNIVERSITY_LIBRARY)
            .totalBookCount(1000)
            .libraryUrl("www.example.com")
            .build();

        LibraryUpdateInfo updateInfo = new LibraryUpdateInfo("경기도서관", "공공도서관",
            "www.new-example.com");

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(existingLibrary));

        // When
        libraryDomainService.updateLibrary(libraryId, updateInfo);

        // Then
        verify(libraryRepository).findById(libraryId);
        assertThat(existingLibrary.getName()).isEqualTo("경기도서관");
        assertThat(existingLibrary.getType()).isEqualTo(LibraryType.PUBLIC_LIBRARY);
        assertThat(existingLibrary.getLibraryUrl().getUrl()).isEqualTo("www.new-example.com");
    }

    @Test
    @DisplayName("createLibraries 메서드는 중복된 도서관을 제외하고 저장한다.")
    void createLibraries_WithDuplicates_ShouldReturnCorrectResult() {
        // Given
        List<LibraryCreationInfo> infos = Arrays.asList(
            new LibraryCreationInfo("Library1", "url1", EbookPlatform.KYOBO),
            new LibraryCreationInfo("Library2", "url1", EbookPlatform.YES24),
            new LibraryCreationInfo("Library3", "url2", EbookPlatform.KYOBO)
        );

        when(libraryRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        LibraryCreationResult result = libraryDomainService.createLibraries(infos);

        // Then
        assertThat(result.savedLibraryCount()).isEqualTo(2);
        assertThat(result.duplicatedLibraryCount()).isEqualTo(1);

        verify(libraryRepository).saveAll(argThat(list -> ((List<Library>) list).size() == 2));
    }
}
