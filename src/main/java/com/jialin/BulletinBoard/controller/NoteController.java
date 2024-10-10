package com.jialin.BulletinBoard.controller;

import com.jialin.BulletinBoard.models.Note;
import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.service.NoteService;
import com.jialin.BulletinBoard.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    /**
     * Create a new note for the user
     *
     * @param note Note object to be created
     * @param authentication User authentication information
     * @return
     */
    @PostMapping
    public ResponseEntity<Note> createNote(
            @RequestBody Note note,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());
        Note createdNote = noteService.saveNote(note, user);
        return ResponseEntity.ok(createdNote);
    }

    /**
     * Fetch all notes for the user
     *
     * @param userId Id of the user
     * @param authentication User authentication information
     * @return List of notes created by the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getAllNotes(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());
        if (!user.getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        List<Note> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    /**
     * Get note by noteId
     *
     * @param noteId Id of the note
     * @param authentication User authentication information
     * @return Note with required id
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getNoteById(
            @PathVariable Long noteId,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());
        Note note = noteService.getNoteById(noteId);
        if (note != null && note.getCreatedBy().equals(user)) {
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.status(404).build();
    }

    /**
     * Update the note owned by user with userId
     * Need to validate the ownership of the user to the note
     *
     * @param userId Id of the user performing the update
     * @param noteId Id of the note to be updated
     * @param updatedNote Updated note object
     * @param authentication User authentication information
     * @return Updated note object
     */
    @PutMapping("/user/{userId}/note/{noteId}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long userId,
            @PathVariable Long noteId,
            @RequestBody Note updatedNote,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());
        if (!user.getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        Note existingNote = noteService.getNoteById(noteId);
        if (existingNote != null && existingNote.getCreatedBy().equals(user)) {
            existingNote.setTitle(updatedNote.getTitle());
            existingNote.setContent(updatedNote.getContent());
            Note savedNote = noteService.saveNote(existingNote, user);
            return ResponseEntity.ok(savedNote);
        }
        return ResponseEntity.status(404).build();
    }

    /**
     * Delete the note owned by user
     *
     * @param userId Id of the user performing the deletion
     * @param noteId Id of the note to be deleted
     * @param authentication User authentication information
     * @return
     */
    @DeleteMapping("/user/{userId}/note/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long userId,
            @PathVariable Long noteId,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName());
        if (!user.getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        Note existingNote = noteService.getNoteById(noteId);
        if (existingNote != null && existingNote.getCreatedBy().equals(user)) {
            noteService.deleteNote(noteId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();
    }
}
