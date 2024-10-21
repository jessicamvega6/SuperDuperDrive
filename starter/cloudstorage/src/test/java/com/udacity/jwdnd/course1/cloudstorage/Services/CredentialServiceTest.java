package com.udacity.jwdnd.course1.cloudstorage.Services;
import com.udacity.jwdnd.course1.cloudstorage.Entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTest {

    @Mock
    private CredentialsMapper credentialsMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private CredentialService credentialService;

    private User testUser;
    private Credential testCredential;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserid(1);
        testUser.setUsername("testUser");
        testUser.setSalt("testSalt");

        testCredential = new Credential();
        testCredential.setCredentialid(1);
        testCredential.setUserid(1);
        testCredential.setUrl("http://example.com");
        testCredential.setUsername("exampleUser");
        testCredential.setPassword("password123");
    }

    @Test
    void testAddCredentials() {
        when(userMapper.getUser("testUser")).thenReturn(testUser);
        when(encryptionService.encryptValue("password123", "testSalt")).thenReturn("encryptedPassword");
        when(credentialsMapper.insert(any(Credential.class))).thenReturn(1);

        int result = credentialService.addCredentials(testCredential, "testUser");

        assertEquals(1, result);
        assertEquals(testUser.getUserid(), testCredential.getUserid());
        assertEquals("encryptedPassword", testCredential.getPassword());
        verify(credentialsMapper).insert(testCredential);
    }

    @Test
    void testDeleteCredentials() {
        when(userMapper.getUser("testUser")).thenReturn(testUser);
        when(credentialsMapper.getCredential(1)).thenReturn(testCredential);

        credentialService.deleteCredentials("testUser", 1);

        verify(credentialsMapper).delete(1);
    }

    @Test
    void testDeleteCredentials_UserMismatch() {
        User differentUser = new User();
        differentUser.setUserid(2);

        when(userMapper.getUser("testUser")).thenReturn(differentUser);
        when(credentialsMapper.getCredential(1)).thenReturn(testCredential);

        credentialService.deleteCredentials("testUser", 1);

        verify(credentialsMapper, never()).delete(1);
    }

    @Test
    void testGetCredentials() {
        List<Credential> expectedCredentials = Arrays.asList(testCredential);
        when(userMapper.getUser("testUser")).thenReturn(testUser);
        when(credentialsMapper.getAllCredentials(1)).thenReturn(expectedCredentials);

        List<Credential> result = credentialService.getCredentials("testUser");

        assertEquals(expectedCredentials, result);
    }

    @Test
    void testDecryptValue() {
        when(userMapper.getUser("testUser")).thenReturn(testUser);
        when(credentialsMapper.getCredential(1)).thenReturn(testCredential);
        when(encryptionService.decryptValue("encryptedValue", "testSalt")).thenReturn("decryptedValue");

        String result = credentialService.decryptValue("encryptedValue", 1, "testUser");

        assertEquals("decryptedValue", result);
    }

    @Test
    void testDecryptValue_UserMismatch() {
        User differentUser = new User();
        differentUser.setUserid(2);

        when(userMapper.getUser("testUser")).thenReturn(differentUser);
        when(credentialsMapper.getCredential(1)).thenReturn(testCredential);

        assertThrows(BadCredentialsException.class, () ->
                credentialService.decryptValue("encryptedValue", 1, "testUser")
        );
    }
}