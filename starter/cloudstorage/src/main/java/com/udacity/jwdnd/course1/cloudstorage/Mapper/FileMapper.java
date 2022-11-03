package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.FileResponse;
import org.apache.ibatis.annotations.*;

import java.nio.file.Files;
import java.util.List;

@Mapper
public interface FileMapper {



    // insert file

    @Insert("INSERT INTO files (filename, contenttype,filesize, userid, filedata, createdon) " +
            "VALUES (#{filename}, #{contenttype} ,#{filesize}, #{userid}, #{filedata}, #{createdon})")
   @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public int insertFile(File file);


    @Update("UPDATE FILES SET filename = #{filename} WHERE userid = #{userid}")
    public void updateFileName(int userid, String filename);


    // select all files given userid
                                            /* ---------------- SELECT -----------------------*/
    @Select("SELECT * FROM files where userid = #{userid}")
    public List<File> getFilesByUserId (int userid);
    //select by fileId
    @Select("SELECT * FROM files WHERE fileId = #{fileId}")
    public File getFileByFileId (int fileId);

    // select by filename
    @Select("SELECT * FROM files WHERE filename = #{filename} AND userid = #{userid}")
    public File getFileByFileName (String filename, int userid );

                                          /* ---------------- DELETE -----------------------*/
    // delete file
    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    public void deleteFileByFileId(int fileId);

    @Delete("DELETE FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    public void deletFileByFilename(String filename, int userid);

}
