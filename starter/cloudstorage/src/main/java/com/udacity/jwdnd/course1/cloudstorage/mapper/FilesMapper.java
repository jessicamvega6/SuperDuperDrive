package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.Entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilesMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFileByUser(int userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileByFileId(int fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);


    @Delete("DELETE  FROM FILES WHERE fileId=#{fileId}")
    int deleteFile(int fileId);

    @Select("SELECT * FROM FILES WHERE filename=#{filename}")
    File getFileByFilename(String filename);


}
