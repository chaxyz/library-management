package com.example.librarymanagement.controller;



import com.example.librarymanagement.dto.CategoryDto;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category createdCategory = categoryService.create(category);
        CategoryDto responseDto = modelMapper.map(createdCategory, CategoryDto.class);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.getById(id);
        return category.map(value -> ResponseEntity.ok(modelMapper.map(value, CategoryDto.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryService.getAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        Optional<Category> existingCategory = categoryService.getById(id);
        if (existingCategory.isPresent()) {
            Category categoryToUpdate = modelMapper.map(categoryDto, Category.class);
            categoryToUpdate.setId(id);
            Category updatedCategory = categoryService.update(id, categoryToUpdate);
            CategoryDto responseDto = modelMapper.map(updatedCategory, CategoryDto.class);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            categoryService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

