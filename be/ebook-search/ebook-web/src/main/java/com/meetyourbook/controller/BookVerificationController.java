package com.meetyourbook.controller;

import com.meetyourbook.dto.BookCountVerificationResult;
import com.meetyourbook.service.BookCountVerificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BookVerificationController {

    private final BookCountVerificationService bookCountVerificationService;

    @GetMapping("/verify-book-count")
    public List<BookCountVerificationResult> verifyBookCount() {
        return bookCountVerificationService.checkBookCount();
    }


}
