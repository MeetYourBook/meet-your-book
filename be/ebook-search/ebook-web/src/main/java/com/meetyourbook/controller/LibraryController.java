package com.meetyourbook.controller;

import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.service.LibraryWebService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryWebService libraryWebService;

    @GetMapping
    public List<LibraryResponse> getLibraries() {
        return libraryWebService.findAllLibraryResponses();
    }

    @GetMapping("/{libraryId}")
    public ResponseEntity<LibraryResponse> getLibrary(@PathVariable Long libraryId) {
        LibraryResponse response = libraryWebService.findById(libraryId);
        return ResponseEntity.ok(response);
    }

}
