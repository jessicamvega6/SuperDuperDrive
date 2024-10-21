package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final UserMapper userMapper;
    CredentialsMapper credentialsMapper;
    EncryptionService encryptionService;

    public CredentialService(CredentialsMapper credentialsMapper, UserMapper userMapper,
                             EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredentials(Credential credential, String username) {
        User user = userMapper.getUser(username);
        credential.setUserid(user.getUserid());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(),  user.getSalt());
        credential.setPassword(encryptedPassword);
        return credentialsMapper.insert(credential);
    }

    public void deleteCredentials(String username, int credentialid) {
        User user = userMapper.getUser(username);
        Credential credential = credentialsMapper.getCredential(credentialid);
        if(user.getUserid().equals(credential.getUserid())) {
            credentialsMapper.delete(credentialid);
        }
    }

    public List<Credential> getCredentials(String username) {
        User user = userMapper.getUser(username);
        return credentialsMapper.getAllCredentials(user.getUserid());
    }

    public String decryptValue(String encryptedValue, Integer credId, String username) {
        User user = userMapper.getUser(username);
        Credential credential = credentialsMapper.getCredential(credId);

        if(credential.getUserid() != user.getUserid()) {
            throw new BadCredentialsException("Access denied");
        }
        return encryptionService.decryptValue(encryptedValue, user.getSalt());
    }
}
