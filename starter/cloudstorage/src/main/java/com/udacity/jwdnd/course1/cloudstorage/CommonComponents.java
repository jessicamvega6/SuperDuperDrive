package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Entity.File;
import com.udacity.jwdnd.course1.cloudstorage.Entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.Services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.Services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.Services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class CommonComponents {

    private final NoteService noteService;
    private final  CredentialService credentialService;
    private final  FileService fileService;

    public CommonComponents(NoteService noteService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    public Model addCommonAttributes(Model model, Authentication authentication) {
        List<Note> noteList = noteService.getAllNotes(authentication.getName());
        List<Credential> credentialList = credentialService.getCredentials(authentication.getName());
        List<File> filesList = fileService.getFiles(authentication.getName());

        model.addAttribute("note", new Note());
        model.addAttribute("noteList", noteList);

        model.addAttribute("credential", new Credential());
        model.addAttribute("credentialList", credentialList);

        model.addAttribute("file", new File());
        model.addAttribute("filesList", filesList);

        return model;
    }

}
