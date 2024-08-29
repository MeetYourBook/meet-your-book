package com.meetyourbook.dto;

import java.util.List;

public record LibraryCrawlMetaData(

    String type,
    String viewCount,
    String totalCount,
    String itemPosition,
    String maxItemPosition,
    String count,
    List<LibraryCrawlResult> item

) {

}
