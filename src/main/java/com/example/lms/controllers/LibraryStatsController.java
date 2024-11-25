package com.example.lms.controllers;

import com.example.lms.services.LibraryStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/library")
public class LibraryStatsController {

    private final LibraryStatsService libraryStatsService;

    public LibraryStatsController(LibraryStatsService libraryStatsService) {
        this.libraryStatsService = libraryStatsService;
    }

    @GetMapping("/stats")
    public Map<String, Long> getLibraryStats() {
        return libraryStatsService.getLibraryStats();
    }
}
