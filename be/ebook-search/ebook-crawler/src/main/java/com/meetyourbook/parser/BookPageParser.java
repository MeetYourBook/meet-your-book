package com.meetyourbook.parser;

import com.meetyourbook.dto.BookInfo;
import java.util.List;
import org.jsoup.nodes.Document;

public interface BookPageParser {

    List<BookInfo> parseBooks(Document doc);
}
