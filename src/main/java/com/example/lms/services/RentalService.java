package com.example.lms.services;

import com.example.lms.models.Rental;
import com.example.lms.repositories.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    public Rental saveRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Long id, Rental updatedRental) {
        return rentalRepository.findById(id).map(rental -> {
            rental.setBook(updatedRental.getBook());
            rental.setUser(updatedRental.getUser());
            rental.setRentalDate(updatedRental.getRentalDate());
            rental.setDueDate(updatedRental.getDueDate());
            rental.setReturnDate(updatedRental.getReturnDate());
            rental.setIsReturned(updatedRental.getIsReturned());
            rental.setLost(updatedRental.getLost());
            rental.setPrice(updatedRental.getPrice());
            return rentalRepository.save(rental);
        }).orElseThrow(() -> new RuntimeException("Rental not found with id " + id));
    }

    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }

    // Calculate rental fee for returned books
    public Double calculateReturnFee(Rental rental) {
        if (rental.getRentalDate() == null || rental.getDueDate() == null || rental.getReturnDate() == null) {
            throw new IllegalArgumentException("Rental, Due, or Return dates cannot be null.");
        }
        if (rental.getReturnDate().isAfter(rental.getDueDate())) {
            long daysRented = rental.getDueDate().toEpochDay() - rental.getRentalDate().toEpochDay();
            long lateDays = rental.getReturnDate().toEpochDay() - rental.getDueDate().toEpochDay();
            return (daysRented * 1.0) + (lateDays * 1.5);
        } else {
            long daysRented = rental.getReturnDate().toEpochDay() - rental.getRentalDate().toEpochDay();
            return daysRented * 1.0;
        }
    }

    // Calculate fee for lost books
    public Double calculateLostFee(Rental rental) {
        if (rental.getBook() == null) {
            throw new IllegalArgumentException("Book details are missing for the rental.");
        }
        return 30.0 + rental.getBook().getPrice() + 15.0;
    }

}
