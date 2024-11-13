package com.example.librarymanagement.service;

import com.example.librarymanagement.entity.Category;
import com.example.librarymanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CrudService<Category,Integer>{
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category update(Integer integer, Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
