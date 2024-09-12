package com.example.editor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.editor.dto.CreateDocumentDTO;
import com.example.editor.dto.DocElementDTO;
import com.example.editor.dto.DocumentDTO;
import com.example.editor.dto.InviteDTO;
import com.example.editor.dto.TextChangeDTO;
import com.example.editor.dto.TextMessageDTO;
import com.example.editor.models.Document;
import com.example.editor.models.Invite;
import com.example.editor.services.DocumentService;
import com.example.editor.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.HashMap;

import org.springframework.security.core.Authentication;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

	@Autowired
	DocumentService documentService;

	@Autowired
	UserService userService;

	@GetMapping("/")
	public ResponseEntity<?> gethome() {
		return ResponseEntity.ok("Hello World!");
	}

	@PostMapping("/create-doc/{docID}")
	public ResponseEntity<?> postMethodName(@RequestBody CreateDocumentDTO createDocumentDTO, @PathVariable String docID, Authentication authentication) {
		
		String username = authentication.getName();
		Document document;

		document = userService.getDocument(docID, username);

		if (document == null) {
			document = documentService.createDocument(docID, createDocumentDTO.getTitle());
			userService.addDocument(document, username);
		}		
		
		return ResponseEntity.ok("created");
	}
	
	
	@GetMapping("/get-doc/{docID}")
	public ResponseEntity<DocumentDTO> getDocument(@PathVariable String docID, Authentication authentication) {
		
		// doc = userService.getdocument(docId, username)
		// if doc
		// 		documentService.getDTO(doc);
		// else 
		// 		doc = documentService.getdocument(docId)
		// 		userService.adddocument(doc, username)
		
		String username = authentication.getName();
		Document document;
		DocumentDTO dto;

		document = userService.getDocument(docID, username);

		if (document != null) {
			dto = documentService.getDTO(document);
		} else {
			document = documentService.getDocument(docID);
			userService.addDocument(document, username);
			dto = documentService.getDTO(document);
		}
		
		return ResponseEntity.ok(dto);		
	}

	@GetMapping("/fetchallnotes")
	public ResponseEntity<List<DocElementDTO>> fetchAllDocument(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok( userService.fetchAllDocs(username));		
	}

	@GetMapping("/fetchallinvites")
	public ResponseEntity<List<Invite>> fetchAllInvites(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok( userService.fetchAllInvites(username));		
	}

	@PostMapping("/sendinvite")
	public ResponseEntity<?> sendInvite(@RequestBody InviteDTO inviteDTO) {
		try {
			userService.addInvite(inviteDTO);
			return ResponseEntity.ok("sent");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(e.getMessage());
		}
	}

	@PostMapping("/acceptinvite")
	public ResponseEntity<?> acceptInvite(@RequestBody Invite invite, Authentication authentication) {
		String username = authentication.getName();
		Document document = documentService.getDocument(invite.getId());
		userService.addDocument(document, username);
		userService.deleteInvite(invite, username);
		return ResponseEntity.ok("accepted");
	}

	@PostMapping("/declineinvite")
	public ResponseEntity<?> declineInvite(@RequestBody Invite invite, Authentication authentication) {
		String username = authentication.getName();
		userService.deleteInvite(invite, username);
		return ResponseEntity.ok("declined");
	}

	@DeleteMapping("/deletenote/{docID}")
	public ResponseEntity<String> deleteDocument(@PathVariable String docID, Authentication authentication) {
		String username = authentication.getName();
		// delete from user
		// update user in repo
		// delete from document repo

		userService.deleteDocument(docID, username);
		documentService.deleteDocument(docID);

		return ResponseEntity.ok("success");		
	}


	@MessageMapping("/send-changes")
	public void receiveMessage(@Payload TextChangeDTO textChangeDTO, Principal principal) {

		if (principal == null) {
			return;
		}
		String username = principal.getName();
		
		String documentId = textChangeDTO.getDocumentId();
		Object delta = textChangeDTO.getDelta();
		System.err.println("changes from id: "+documentId);


		Document document = userService.getDocument(documentId, username);
		

		if (document != null) {

			LinkedList<Object> data = document.getData();
			data.add(delta);
			document.setData(data);

			userService.addDocument(document, username);
			documentService.save(document);
			System.out.println("change saved");
		} else {
			throw new RuntimeException("document not found");
		}


		template.convertAndSend("/topic/receive-changes/" + documentId, textChangeDTO);
	}

    @SendTo("/topic/message")
	public TextMessageDTO broadcastMessage(@Payload TextMessageDTO textMessageDTO) {
		return textMessageDTO;
	}
}

