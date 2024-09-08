package com.example.editor.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.editor.models.Document;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {

    Optional<Document> findById(String id);

}
