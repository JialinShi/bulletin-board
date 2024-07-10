package com.jialin.BulletinBoard.controller;

import com.jialin.BulletinBoard.models.Note;
import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.service.NoteService;
import com.jialin.BulletinBoard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService _noteService;

    @Autowired
    private UserService _userService;

    /**
     * Create a note with Note entity and userId
     *
     * @param note: the note to be created
     * @param userId: id of the user who created this note
     * @return: Note entity created
     */
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note, @RequestParam Long userId) {
        User user = _userService.getUserById(userId);
        Note createdNote = _noteService.saveNote(note, user);
        return ResponseEntity.ok(createdNote);
    }

    /**
     * Get all notes by userId
     *
     * @param userId: id of the user
     * @return: List of notes owned by the user with userId
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(@RequestParam Long userId) {
        List<Note> notes = _noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    /**
     * Get not by noteId
     *
     * @param id: id of the note
     * @return: note with the required id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = _noteService.getNoteById(id);
        if (note != null) {
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update a note created by the user with userId.
     * Need to check if this note is owned by the user first, otherwise this call fails.
     *
     * @param id: id of the note to be updated
     * @param updatedNote: updatedNote that will replace the original one
     * @param userId: id of the user who wants to update this note, has to be the owner to succeed
     * @return: updated note entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id,
                                           @RequestBody Note updatedNote,
                                           @RequestParam Long userId) {
        Note existingNote = _noteService.getNoteById(id);
        User user = _userService.getUserById(userId);
        if (existingNote != null && existingNote.getCreatedBy().equals(user)) {
            existingNote.setContent(updatedNote.getContent());
            _noteService.saveNote(existingNote, user);
            return ResponseEntity.ok(existingNote);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete the note with id
     *
     * @param id: id of the note to be deleted
     * @param userId: id of the user who wants to delete this note
     * @return:
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, @RequestParam Long userId) {
        User user = _userService.getUserById(userId);
        Note existingNote = _noteService.getNoteById(id);
        if (existingNote != null && existingNote.getCreatedBy().equals(user)) {
            _noteService.deleteNote(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}