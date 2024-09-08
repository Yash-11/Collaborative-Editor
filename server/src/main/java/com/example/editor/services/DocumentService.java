package com.example.editor.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.editor.dto.DocumentDTO;
import com.example.editor.models.Document;
import com.example.editor.models.User;
import com.example.editor.repositories.DocumentRepository;

import java.util.LinkedList;

// import jakarta.persistence.EntityNotFoundException;


@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Document createDocument(String id, String title) {

        Document document;
        Optional<Document> opDocument = documentRepository.findById(id);

        if (!opDocument.isPresent()) {
            document = new Document();
            document.setId(id);
            document.setHeadline(title);
            document.setData(new LinkedList<>());
            document.setUsers(new LinkedList<>());
            return document;
        }
        return opDocument.get();
    }
    
    public Document getDocument(String id) {

        Document document;
        Optional<Document> opDocument = documentRepository.findById(id);

        if (opDocument.isPresent()) {
            return opDocument.get();
        } else {
            document = new Document();
            document.setId(id);
            document.setUsers(new LinkedList<>());
            return document;
        }
    }

    public void deleteDocument(String id) {
        documentRepository.deleteById(id);
    }

    public DocumentDTO getDTO(Document document) {

        // System.out.println(document);
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDoc(document.getData());
        documentDTO.setDocumentId(document.getId());
        documentDTO.setTitle(document.getHeadline());
        return documentDTO;
    }

    public Document findOneById(String id) {

        try {
            return documentRepository.findById(id).get();
        } catch (Exception e) {
            // TODO: handle exception
            throw(e);
        }
        // return documentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
    }
    
}
