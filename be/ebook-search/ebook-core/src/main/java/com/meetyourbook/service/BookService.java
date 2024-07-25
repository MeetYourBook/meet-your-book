package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryService libraryService;

    @Transactional
    public void saveAll(List<BookInfo> bookInfos, String baseUrl) {
        Library library = libraryService.findByBaseUrl(baseUrl);

        List<Book> books = bookInfos.stream()
            .map(this::findOrCreateBook)
            .peek(book -> book.addLibrary(library))
            .toList();

        bookRepository.saveAll(books);
    }

    private Book findOrCreateBook(BookInfo bookInfo) {
        Optional<Book> optionalBook = bookRepository.findFirstByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());

        return optionalBook.orElseGet(bookInfo::toEntity);
    }

}
