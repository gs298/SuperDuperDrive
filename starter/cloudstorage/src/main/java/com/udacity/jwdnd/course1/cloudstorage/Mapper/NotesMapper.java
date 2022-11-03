package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {

    // insert note user generated would be noteid

    /*CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY auto_increment,
    notetitle VARCHAR(20),
    notedescription VARCHAR (1000),
    userid INT,
    foreign key (userid) references USERS(userid)
);*/

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES" +
            "(#{notetitle}, #{notedescription}, #{userid}) ")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    public int insertNote(Notes notes);


    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} " +
            "where noteid = #{noteid}")
    public void updateNotes(Notes notes);

    @Select("SELECT * FROM NOTES where noteid = #{noteid}")
    public Notes checkForNoteId(int noteid);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    public List<Notes> selectNotes(int userid);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid} AND userid = #{userid}")
    public void deleteNotes(int noteid, int userid);

    //

}
