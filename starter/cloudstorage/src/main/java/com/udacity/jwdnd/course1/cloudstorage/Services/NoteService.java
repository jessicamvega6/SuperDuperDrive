package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.NoteNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.UnauthorizedAccessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    NoteMapper noteMapper;
    UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public List<Note> getAllNotes(String username) {
        User user = userMapper.getUser(username);
        return noteMapper.getNoteByUser(user.getUserid());
    }

    public int addNote(Note note, String username) {
        User user = userMapper.getUser(username);
        note.setUserid(user.getUserid());
        return noteMapper.insertNote(note);
    }

    public void deleteNote(int noteId, String username) {
        User user = userMapper.getUser(username);
        Note note = noteMapper.getSpecificNote(noteId);

        if(note == null ) {
            throw new NoteNotFoundException("Note not found with id: " + noteId);
        }

        if(note.getUserid() != user.getUserid()){
            throw new UnauthorizedAccessException("Invalid Access for deleting note");
        }
        else {
            noteMapper.delete(noteId);
        }


    }
}
