package com.jialin.BulletinBoard.service;

import com.jialin.BulletinBoard.models.Note;
import com.jialin.BulletinBoard.models.User;
import com.jialin.BulletinBoard.repository.NoteRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTests {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private ContentCheckService contentCheckService;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNoteResponse() {
        User user = new User();
        Note note = new Note();
        note.setContent("This is a safe content");
        when(contentCheckService.isToxic(note.getContent())).thenReturn(false);
        when(noteRepository.save(note)).thenReturn(note);

        ResponseEntity<Note> response = noteService.saveNoteResponse(note, user);

        assertEquals(ResponseEntity.ok(note), response);
        verify(contentCheckService, times(1)).isToxic(note.getContent());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void saveNoteToxic() {
        User user = new User();
        Note note = new Note();
        note.setContent("This content is toxic");
        when(contentCheckService.isToxic(note.getContent())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> noteService.saveNoteResponse(note, user));
        verify(contentCheckService, times(1)).isToxic(note.getContent());
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void getAllNotes() {
        List<Note> notes = Arrays.asList(new Note(), new Note());
        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteService.getAllNotes();

        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void getNotesByUserId() {
        List<Note> notes = Arrays.asList(new Note());
        when(noteRepository.findByCreatedBy_Id(1L)).thenReturn(notes);

        List<Note> result = noteService.getNotesByUserId(1L);

        assertEquals(1, result.size());
        verify(noteRepository, times(1)).findByCreatedBy_Id(1L);
    }

    @Test
    void getNoteById() {
        Note note = new Note();
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById(1L);

        assertNotNull(result);
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void getNoteByIdNotExist() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        Note result = noteService.getNoteById(1L);

        assertNull(result);
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void updateNote() {
        Note note = new Note();
        when(noteRepository.save(note)).thenReturn(note);

        Note result = noteService.updateNote(note);

        assertEquals(note, result);
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    void deleteNote() {
        doNothing().when(noteRepository).deleteById(1L);

        noteService.deleteNote(1L);

        verify(noteRepository, times(1)).deleteById(1L);
    }
}
