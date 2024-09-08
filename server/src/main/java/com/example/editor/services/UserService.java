package com.example.editor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.editor.dto.DocElementDTO;
import com.example.editor.dto.InviteDTO;
import com.example.editor.models.Document;
import com.example.editor.models.Invite;
import com.example.editor.models.User;
import com.example.editor.repositories.DocumentRepository;
import com.example.editor.repositories.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        if (user.getDocuments()==null) user.setDocuments(new LinkedList<>());
        if (user.getInvites()==null) user.setInvites(new LinkedList<>());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<DocElementDTO> fetchAllDocs(String username) {
        User user = getUserById(username);
        List<Document> documents = user.getDocuments();
        if (documents == null) return new LinkedList<DocElementDTO>();
        return documents.stream()
                        .map(doc -> new DocElementDTO(doc.getId(), doc.getHeadline()))
                        .collect(Collectors.toList());
    }

    
    public List<Invite> fetchAllInvites(String username) {
        User user = getUserById(username);
        List<Invite> invites = user.getInvites();
        if (invites == null) return new LinkedList<>();
        return invites;
    }

    public void addInvite(InviteDTO inviteDTO) throws Exception {
        User user = getUserById(inviteDTO.getRecipientEmail());
        if (user == null)  {
            throw new RuntimeException("Recipient not found");
        }
        List<Invite> invites = user.getInvites();
        boolean inviteExists = invites.stream().anyMatch(u -> u.getId().equals(inviteDTO.getDocumentId()));
        if (!inviteExists) {
            invites.add(new Invite(inviteDTO.getDocumentId(), inviteDTO.getTitle()));
            user.setInvites(invites);
            userRepository.save(user);
        }
    }

    public void deleteInvite(Invite invite, String username) {
        User user = getUserById(username);
        List<Invite> invites = user.getInvites();
        invites.removeIf(inv -> inv.getId().equals(invite.getId()));
        user.setInvites(invites);
        userRepository.save(user);
        return;
    }

    public Document getDocument(String id, String username) {
        User user = getUserById(username);
        List<Document> documents = user.getDocuments();
        if (documents==null) return null;
        Optional<Document> opDcument = documents.stream()
                                .filter(doc -> doc.getId().equals(id))
                                .findFirst();

        if (opDcument.isPresent())
            return opDcument.get();
        return null;
    }

    public void addDocument(Document document, String username){
        User user = getUserById(username);
        List<User> users = document.getUsers();
        boolean userExists = users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (!userExists) {
            users.add(user);
            document.setUsers(users);
            documentRepository.save(document);
        }
        
        List<Document> documents = user.getDocuments();
        boolean docExists = documents.stream().anyMatch(u -> u.getId().equals(document.getId()));
        if (!docExists) {
            documents.add(document);
            user.setDocuments(documents);
            userRepository.save(user);
        }
    }

    public void deleteDocument(String docId, String username){
        User user = getUserById(username);
        List<Document> documents = user.getDocuments();
        documents.removeIf(document -> document.getId().equals(docId));
        user.setDocuments(documents);
        userRepository.save(user);
    }

    // public void addDocumentToUser(String userEmail, Document document) {
    //     // Find the user by email
    //     User user = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

    //     // Set the userId in the document
    //     document.setUserId(userEmail);

    //     // Save the document
    //     Document savedDocument = documentRepository.save(document);

    //     // Add the document to the user's list
    //     user.getDocuments().add(savedDocument);

    //     // Save the user with the updated list of documents
    //     userRepository.save(user);
    // }
}


