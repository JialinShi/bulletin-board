package com.jialin.BulletinBoard.service;

import com.jialin.BulletinBoard.models.Note;
import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Note related service that performs CRUD operations on Notes
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository _noteRepository;

    public Note saveNote(Note note, User user) {
        note.setCreatedBy(user);
        return _noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return _noteRepository.findAll();
    }

    public List<Note> getNotesByUserId(Long userId) {
        return _noteRepository.findByCreatedBy_Id(userId);
    }

    public Note getNoteById(Long id) {
        return _noteRepository.findById(id).orElse(null);
    }

    public Note updateNote(Note note) {
        return _noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        _noteRepository.deleteById(id);
    }
}