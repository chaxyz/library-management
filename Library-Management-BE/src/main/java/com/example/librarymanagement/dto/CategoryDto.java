package com.example.librarymanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CategoryDto {
    private int id;
    @Size(min = 1, max = 50)
    private String name;
}
