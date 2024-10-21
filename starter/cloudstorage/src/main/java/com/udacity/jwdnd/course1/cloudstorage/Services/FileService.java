package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Entity.File;
import com.udacity.jwdnd.course1.cloudstorage.Entity.User;
import com.udacity.jwdnd.course1.cloudstorage.Exceptions.UnauthorizedAccessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@Service
public class FileService {

    private final UserMapper userMapper;
    private FilesMapper filesMapper;

    public FileService(FilesMapper filesMapper, UserMapper userMapper) {
        this.filesMapper = filesMapper;
        this.userMapper = userMapper;
    }

    public Integer uploadFile(MultipartFile multiPartFile, String username) throws IOException {
        //add verification that files with the same name are not uploaded
        checkForDuplicateFileName(username, multiPartFile.getOriginalFilename());
        User user = userMapper.getUser(username);

        File file = new File();
        file.setFilename(multiPartFile.getOriginalFilename());
        file.setContenttype(multiPartFile.getContentType());
        file.setFilesize(String.valueOf(multiPartFile.getSize()));
        file.setUserid(user.getUserid());
        file.setFiledata(multiPartFile.getBytes());

        return filesMapper.insertFile(file);
    }

    public List<File> getFiles(String username) {
        User user = userMapper.getUser(username);
        return filesMapper.getFileByUser(user.getUserid());
    }

    public ResponseEntity<byte[]> getFile(int fileId, String username) throws FileNotFoundException {
        User user = userMapper.getUser(username);
        File file = filesMapper.getFileByFileId(fileId);
        HttpHeaders headers = new HttpHeaders();

        if (file == null) {
            throw new FileNotFoundException("File not found");
        }

        if(file.getUserid() == user.getUserid()) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getContenttype()));
            headers.setContentDispositionFormData("attachment", file.getFilename());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        } else {
            throw new UnauthorizedAccessException("User does not have permission to delete this file");
        }

        return new ResponseEntity<>(file.getFiledata(), headers, HttpStatus.OK);
    }

    public void deleteFile(Integer fileId, String username) throws FileNotFoundException {
        User user = userMapper.getUser(username);
        File file = filesMapper.getFileByFileId(fileId);
        if (file == null) {
            throw new FileNotFoundException("File not found");
        }

        if(file.getUserid() == user.getUserid()) {
            filesMapper.deleteFile(fileId);
        } else {
            throw new UnauthorizedAccessException("User does not have permission to delete this file");
        }
    }

    private void checkForDuplicateFileName(String username, String originalFilename) throws FileAlreadyExistsException {
        User user = userMapper.getUser(username);
        File existingFile = filesMapper.getFileByFilename(originalFilename);
        if (existingFile != null) {
            throw new FileAlreadyExistsException("File already exists");
        }
    }

}
