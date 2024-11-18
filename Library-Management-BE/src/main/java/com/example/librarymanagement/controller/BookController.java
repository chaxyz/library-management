package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.BookDto;
import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.entity.Rent;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.service.BookService;
import com.example.librarymanagement.service.CategoryService;

import com.example.librarymanagement.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


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
    @Autowired
    private StorageService storageService;

    @PostMapping("")
    public ResponseEntity<Book> createBook(
            @RequestPart("bookDetails") BookDto bookDto,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Optional<Category> categoryOpt = categoryService.getById(bookDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Book book = modelMapper.map(bookDto, Book.class);
        book.setCategory(categoryOpt.get());
        book.setStatus(bookDto.getStatus() != null ? Rent.Status.valueOf(bookDto.getStatus().toLowerCase()) : Rent.Status.returned);

        if (file != null) {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image format");
            }
            String fileUrl = storageService.uploadFile(file);
            book.setPath(fileUrl);
        }
        Book createdBook = bookService.create(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookService.getById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("")
    public List<Book> getAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        if (categoryId != null) {
            return bookService.getAllByCatagory(categoryId);
        }
        return bookService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer id,
            @RequestPart("bookDetails") BookDto bookDetailsDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Optional<Book> existingBookOpt = bookService.getById(id);
        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();

            existingBook.setName(bookDetailsDto.getName());
            existingBook.setStatus(Rent.Status.valueOf(bookDetailsDto.getStatus().toLowerCase()));

            if (bookDetailsDto.getCategoryId() != null) {
                Category newCategory = categoryService.getById(bookDetailsDto.getCategoryId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
                existingBook.setCategory(newCategory);
            }

            if (file != null) {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image format");
                }
                if (existingBook.getPath() != null) {

                    String oldFileName = extractFileNameFromUrl(existingBook.getPath());
                    storageService.deleteFileFromUrl(oldFileName);
                }
                String newFileUrl = storageService.uploadFile(file);
                existingBook.setPath(newFileUrl);
            }else {
                if (existingBook.getPath() != null) {
                    String oldFileName = extractFileNameFromUrl(existingBook.getPath());
                    storageService.deleteFileFromUrl(oldFileName);
                }
                existingBook.setPath(null);
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
            Book existingBook = book.get();
            if (existingBook.getPath() != null) {
                String oldFileName = extractFileNameFromUrl(existingBook.getPath());
                storageService.deleteFileFromUrl(oldFileName);
            }
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        try {
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8.toString());
            URL url = new URL(decodedUrl);
            String path = url.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file URL");
        }
    }

}






