package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookDto;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.entity.Rent;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.service.CategoryService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto bookDto) {
        Optional<Category> categoryOpt = categoryService.getById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }System.out.println(bookDto);

        Book book = modelMapper.map(bookDto, Book.class);
        book.setCategory(categoryOpt.get());
        book.setStatus(bookDto.getStatus() != null ? Rent.Status.valueOf(bookDto.getStatus()) : Rent.Status.RETURNED);

        Book createdBook = bookService.create(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookService.getById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody BookDto bookDetailsDto) {
        Optional<Book> existingBookOpt = bookService.getById(id);
        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            existingBook.setName(bookDetailsDto.getName());
            existingBook.setStatus(Rent.Status.valueOf(bookDetailsDto.getStatus()));
            if (bookDetailsDto.getCategoryId() != null) {
                Category newCategory = categoryService.getById(bookDetailsDto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                existingBook.setCategory(newCategory);
            }
            Book updatedBook = bookRepository.save(existingBook);
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        Optional<Book> book = bookService.getById(id);
        if (book.isPresent()) {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}






