package com.example.librarymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {
    private String name;
    private Integer categoryId;
    @Pattern(regexp = "borrowed|returned", message = "Visibility must be either 'borrowed' or 'returned'")
    private String status;
}

