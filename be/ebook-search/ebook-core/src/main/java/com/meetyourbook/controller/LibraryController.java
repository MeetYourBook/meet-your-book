package com.meetyourbook.controller;

import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.entity.Library;
import com.meetyourbook.service.LibraryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

}
