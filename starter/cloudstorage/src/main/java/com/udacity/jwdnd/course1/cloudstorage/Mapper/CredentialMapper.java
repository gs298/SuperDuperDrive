package com.udacity.jwdnd.course1.cloudstorage.Mapper;


import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, salt, password, userid)" +
            "VALUES (#{url}, #{username}, #{salt}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    public int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, " +
            "username=#{username}, salt=#{salt}, " +
            "password=#{password} " +
            "WHERE credentialid=#{credentialid} AND userid=#{userid};")
    public void updateCredential(Credential credential);


    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid} AND userid = #{userid}")
    public void deleteCredential(int credentialid, int userid);


    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    public List<Credential> getCredentialsByUserid(int userid) throws DataNotAvailableException;


}
