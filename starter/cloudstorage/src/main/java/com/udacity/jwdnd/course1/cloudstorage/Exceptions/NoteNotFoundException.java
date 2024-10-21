package com.udacity.jwdnd.course1.cloudstorage.Exceptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}
