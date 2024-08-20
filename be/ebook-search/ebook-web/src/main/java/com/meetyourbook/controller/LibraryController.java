package com.meetyourbook.controller;

import com.meetyourbook.dto.LibraryCreateRequest;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateRequest;
import com.meetyourbook.service.LibraryWebService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libraries")
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

    @PostMapping
    public ResponseEntity<Void> createLibrary(@RequestBody LibraryCreateRequest request) {
        Long libraryId = libraryWebService.createLibrary(request);
        URI location = URI.create("/api/libraries/" + libraryId);
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{libraryId}")
    public ResponseEntity<LibraryResponse> updateLibrary(@PathVariable Long libraryId,
        @RequestBody LibraryUpdateRequest request) {
        LibraryResponse response = libraryWebService.updateLibrary(libraryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{libraryId}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long libraryId) {
        libraryWebService.delete(libraryId);
        return ResponseEntity.noContent().build();
    }

}
