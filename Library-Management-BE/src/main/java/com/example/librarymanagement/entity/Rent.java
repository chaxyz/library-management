package com.example.librarymanagement.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
@Data
public class Rent {
    @EmbeddedId
    private RentId id;

    @Column(name = "loan_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @MapsId("loanOid")
    @JoinColumn(name = "loan_oid", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "books_id", nullable = false)
    private Book book;

    @Embeddable
    @Data
    public class RentId implements Serializable {
        private String loanOid;
        private Integer bookId;
    }

    public enum Status {
        returned,borrowed;
    }
}
