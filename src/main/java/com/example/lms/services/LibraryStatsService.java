package com.example.lms.services;

import com.example.lms.repositories.AuthorRepository;
import com.example.lms.repositories.BookRepository;
import com.example.lms.repositories.RentalRepository;
import com.example.lms.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LibraryStatsService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public LibraryStatsService(AuthorRepository authorRepository,
                               BookRepository bookRepository,
                               UserRepository userRepository,
                               RentalRepository rentalRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    public Map<String, Long> getLibraryStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("authors", authorRepository.count());
        stats.put("books", bookRepository.count());
        stats.put("users", userRepository.count());
        stats.put("rentals", rentalRepository.countByIsReturnedNullAndLostNull());
        return stats;
    }
}
