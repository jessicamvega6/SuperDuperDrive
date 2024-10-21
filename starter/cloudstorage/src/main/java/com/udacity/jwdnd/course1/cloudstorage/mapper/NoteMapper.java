package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.Entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> getNoteByUser(int userid);

    @Insert("MERGE INTO NOTES (noteid, notetitle, notedescription, userid) " +
            "KEY (noteid) " +
            "VALUES (#{noteid}, #{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note getSpecificNote(int noteid);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void delete(int noteId);
}
