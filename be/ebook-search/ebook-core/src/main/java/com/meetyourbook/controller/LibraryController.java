package com.meetyourbook.controller;

import com.meetyourbook.dto.LibraryCreation;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateRequest;
import com.meetyourbook.service.LibraryService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public List<LibraryResponse> getLibraries() {
        return libraryService.findAllLibraryResponses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryResponse> getLibrary(@PathVariable long id) {
        LibraryResponse response = libraryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createLibrary(@RequestBody LibraryCreation request) {
        Long id = libraryService.createLibrary(request);
        URI location = URI.create("/api/libraries/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryResponse> updateLibrary(@PathVariable Long id,
        @RequestBody LibraryUpdateRequest request) {

        LibraryResponse response = libraryService.updateLibrary(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        libraryService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
