package com.cursotestes.libraryapi.model.repository;

import com.cursotestes.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
