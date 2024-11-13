package com.example.librarymanagement.repository;
import com.example.librarymanagement.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends JpaRepository<Rent, Rent.RentId> {

}

