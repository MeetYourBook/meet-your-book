package com.meetyourbook.controller;

import com.meetyourbook.dto.LibraryPageResponse;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibrarySearchRequest;
import com.meetyourbook.service.LibraryWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryWebService libraryWebService;

    @GetMapping
    public LibraryPageResponse getLibraries(@ModelAttribute LibrarySearchRequest request) {
        return libraryWebService.findLibraries(request);
    }

    @GetMapping("/{libraryId}")
    public ResponseEntity<LibraryResponse> getLibrary(@PathVariable Long libraryId) {
        LibraryResponse response = libraryWebService.findById(libraryId);
        return ResponseEntity.ok(response);
    }

}
