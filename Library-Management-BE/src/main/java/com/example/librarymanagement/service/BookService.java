package com.example.librarymanagement.service;

import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookService implements CrudService<Book, Integer> {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book update(Integer id, Book book) {
            return bookRepository.saveAndFlush(book);

    }

    public List<Book> getAllByCatagory(int catagoryId) {
        return bookRepository.findAllByCategory_Id(catagoryId);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }
}
