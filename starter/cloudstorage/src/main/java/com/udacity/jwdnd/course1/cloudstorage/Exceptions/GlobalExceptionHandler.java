package com.udacity.jwdnd.course1.cloudstorage.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;


@ControllerAdvice
public class GlobalExceptionHandler {

    //Currently handling every Exception the same way but if eventually decide to change, the setup is here

    @ExceptionHandler(Exception.class)
    public RedirectView handleGeneralException(Exception ex) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/error");
        return redirectView;
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public RedirectView handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/error");
        return redirectView;
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public RedirectView handleNoteNotFoundException(NoteNotFoundException ex) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/error");
        return redirectView;
    }
}
