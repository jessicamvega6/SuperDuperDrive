package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.FROM;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getAllCredentials(int userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid=#{credentialid}")
    Credential getCredential(int credentialid);


    @Insert("MERGE INTO CREDENTIALS (credentialid, url, username, password, userid)" +
            "KEY (credentialid) " +
            "VALUES (#{credentialid}, #{url}, #{username}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialid}")
    void delete(int credentialid);
}
