package com.meetyourbook.dto;

import java.util.List;

public record BookSearchRequest(String query, Condition condition, List<Long> libraryIds) {


}
