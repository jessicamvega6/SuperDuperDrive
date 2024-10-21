package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {


    @Mock
    private NoteMapper noteMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllNotes() {
        String username = "testUser";
        User user = new User();
        user.setUserid(1);
        user.setUsername(username);

        Note note1 = new Note();
        note1.setNoteid(1);
        note1.setUserid(1);

        Note note2 = new Note();
        note2.setNoteid(2);
        note2.setUserid(1);

        List<Note> expectedNotes = Arrays.asList(note1, note2);

        when(userMapper.getUser(username)).thenReturn(user);
        when(noteMapper.getNoteByUser(user.getUserid())).thenReturn(expectedNotes);

        List<Note> actualNotes = noteService.getAllNotes(username);

        assertEquals(expectedNotes, actualNotes);
        verify(userMapper).getUser(username);
        verify(noteMapper).getNoteByUser(user.getUserid());
    }

    @Test
    void testAddNote() {
        String username = "testUser";
        User user = new User();
        user.setUserid(1);
        user.setUsername(username);

        Note note = new Note();
        note.setNotetitle("Test Note");
        note.setNotedescription("This is a test note");

        when(userMapper.getUser(username)).thenReturn(user);
        when(noteMapper.insertNote(note)).thenReturn(1);

        int result = noteService.addNote(note, username);

        assertEquals(1, result);
        assertEquals(user.getUserid(), note.getUserid());
        verify(userMapper).getUser(username);
        verify(noteMapper).insertNote(note);
    }

    @Test
    void testDeleteNote_Success() {
        String username = "testUser";
        int noteId = 1;
        User user = new User();
        user.setUserid(1);
        user.setUsername(username);

        Note note = new Note();
        note.setNoteid(noteId);
        note.setUserid(user.getUserid());

        when(userMapper.getUser(username)).thenReturn(user);
        when(noteMapper.getSpecificNote(noteId)).thenReturn(note);

        noteService.deleteNote(noteId, username);

        verify(userMapper).getUser(username);
        verify(noteMapper).getSpecificNote(noteId);
        verify(noteMapper).delete(noteId);
    }

    @Test
    void testDeleteNote_UserMismatch() {
        String expectedUsername = "testUser";
        String username = "testUser";
        int noteId = 1;
        User user = new User();
        user.setUserid(1);
        user.setUsername(username);

        Note note = new Note();
        note.setNoteid(noteId);
        note.setUserid(2); // Different user ID

        when(userMapper.getUser(username)).thenReturn(user);
        when(noteMapper.getSpecificNote(noteId)).thenReturn(note);

        noteService.deleteNote(noteId, username);

        verify(userMapper).getUser(expectedUsername);
        verify(noteMapper).getSpecificNote(noteId);
        verify(noteMapper, never()).delete(noteId);
    }
}