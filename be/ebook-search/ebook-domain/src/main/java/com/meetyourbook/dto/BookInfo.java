package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookInfo {

    String provider;
    String title;
    String author;
    String publisher;
    LocalDate publishDate;
    String imageUrl;
    String description;
    String baseUrl;
    String bookUrl;

    @Override
    public String toString() {
        return "BookInfo{" +
            "provider='" + provider + '\'' +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", publisher='" + publisher + '\'' +
            ", publishDate=" + publishDate +
            ", imageUrl='" + imageUrl + '\'' +
            ", description='" + description + '\'' +
            ", baseUrl='" + baseUrl + '\'' +
            '}';
    }

    public Book toEntity() {
        return Book.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .imageUrl(imageUrl)
            .publishDate(publishDate)
            .provider(provider)
            .build();
    }
}
