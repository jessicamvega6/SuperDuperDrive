package com.udacity.jwdnd.course1.cloudstorage.Controllers;

import com.udacity.jwdnd.course1.cloudstorage.CommonComponents;
import com.udacity.jwdnd.course1.cloudstorage.Entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.UnauthorizedAccessException;
import com.udacity.jwdnd.course1.cloudstorage.Services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final CommonComponents commonComponents;


    public CredentialController(CredentialService credentialService, CommonComponents commonComponents) {
        this.credentialService = credentialService;
        this.commonComponents = commonComponents;
    }

    @PostMapping("addCredential")
    public String addCredential(Model model, Authentication authentication, Credential credential) {

        credentialService.addCredentials(credential, authentication.getName());
        model.addAttribute("successMsgCred", "Credential added successfully");
        commonComponents.addCommonAttributes(model, authentication);

        return "home";
    }

    @PostMapping("/deleteCred")
    public String deleteCredential(Model model, Authentication authentication, Integer credentialId) {
        credentialService.deleteCredentials(authentication.getName(), credentialId);
        model.addAttribute("successMsgCred", "Credential deleted successfully");
        commonComponents.addCommonAttributes(model, authentication);

        return "home";
    }

    @PostMapping("/decrypt-password")
    @ResponseBody
    public String decryptPassword(@RequestBody Map<String, String> payload, Authentication authentication) {
        String pw;
        Integer credentialId = Integer.parseInt(payload.get("credentialId"));
        try {
            pw = credentialService.decryptValue(payload.get("encryptedPassword"), credentialId, authentication.getName());
        } catch (UnauthorizedAccessException e) {
            throw new UnauthorizedAccessException("Invalid Access");
        }

        return pw;
    }


}
