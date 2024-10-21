package com.udacity.jwdnd.course1.cloudstorage.Controllers;

import com.udacity.jwdnd.course1.cloudstorage.CommonComponents;
import com.udacity.jwdnd.course1.cloudstorage.Entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.NoteNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.UnauthorizedAccessException;
import com.udacity.jwdnd.course1.cloudstorage.Services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {

    NoteService noteService;
    CommonComponents commonComponents;


    public NoteController(NoteService noteService, CommonComponents commonComponents) {
        this.noteService = noteService;
        this.commonComponents = commonComponents;
    }

    @PostMapping("/addNote")
    public String addNote(Authentication authentication, Note note, Model model) {
        noteService.addNote(note, authentication.getName());
        commonComponents.addCommonAttributes(model, authentication);
        model.addAttribute("successMsgNote", "Note added successfully.");
        return "home";
    }

    @PostMapping("/delete")
    public String deleteNote(Authentication authentication, Integer noteId, Model model) {
        try {
            noteService.deleteNote(noteId, authentication.getName());
            model.addAttribute("successMsgNote", "Note deleted successfully.");
        } catch (Exception e) {
            throw e;
        }
        commonComponents.addCommonAttributes(model, authentication);
        return "home";
    }

}
