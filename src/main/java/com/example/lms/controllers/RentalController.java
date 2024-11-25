package com.example.lms.controllers;

import com.example.lms.models.Rental;
import com.example.lms.services.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        return ResponseEntity.ok(rentalService.saveRental(rental));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable Long id, @RequestBody Rental updatedRental) {
        try {
            return ResponseEntity.ok(rentalService.updateRental(id, updatedRental));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<Double> returnRental(@PathVariable Long id) {
        return rentalService.getRentalById(id)
                .map(rental -> {
                    rental.setReturnDate(LocalDate.now()); // Automatically set the return date to today's date
                    rental.setIsReturned(true); // Mark the rental as returned
                    Double rentalFee = rentalService.calculateReturnFee(rental); // Calculate the fee
                    rental.setFee(rentalFee); // Save the fee in the rental record
                    rentalService.saveRental(rental); // Save the updated rental
                    return ResponseEntity.ok(rentalFee); // Return the calculated fee
                })
                .orElse(ResponseEntity.notFound().build()); // If rental not found, return 404
    }

    @PatchMapping("/{id}/mark-as-lost")
    public ResponseEntity<Double> markAsLost(@PathVariable Long id) {
        return rentalService.getRentalById(id)
                .map(rental -> {
                    rental.setLost(true); // Mark the rental as lost
                    Double lostFee = rentalService.calculateLostFee(rental); // Calculate the lost fee
                    rental.setFee(lostFee); // Save the fee in the rental record
                    rentalService.saveRental(rental); // Save the updated rental
                    return ResponseEntity.ok(lostFee); // Return the calculated lost fee
                })
                .orElse(ResponseEntity.notFound().build()); // If rental not found, return 404
    }
}
