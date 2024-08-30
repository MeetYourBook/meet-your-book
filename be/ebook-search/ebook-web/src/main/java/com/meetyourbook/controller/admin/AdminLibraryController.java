package com.meetyourbook.controller.admin;

import com.meetyourbook.dto.LibraryCreateRequest;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateRequest;
import com.meetyourbook.service.LibraryWebService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/admin/libraries")
@RequiredArgsConstructor
public class AdminLibraryController {

    private final LibraryWebService libraryWebService;

    @PostMapping
    public ResponseEntity<Void> createLibrary(@RequestBody LibraryCreateRequest request) {
        Long libraryId = libraryWebService.createLibrary(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(libraryId)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{libraryId}")
    public ResponseEntity<LibraryResponse> updateLibrary(@PathVariable Long libraryId,
        @RequestBody LibraryUpdateRequest request) {
        libraryWebService.updateLibrary(libraryId, request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(libraryId)
            .toUri();
        return ResponseEntity.noContent().location(location).build();
    }

    @DeleteMapping("/{libraryId}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long libraryId) {
        libraryWebService.delete(libraryId);
        return ResponseEntity.noContent().build();
    }

}
