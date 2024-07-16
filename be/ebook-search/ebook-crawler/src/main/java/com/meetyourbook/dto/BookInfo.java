package com.meetyourbook.dto;

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
    String imageURL;
    String description;

    @Override
    public String toString() {
        return "BookInfo{" +
            "provider='" + provider + '\'' +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", publisher='" + publisher + '\'' +
            ", publishDate='" + publishDate + '\'' +
            ", imageURL='" + imageURL + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

}
