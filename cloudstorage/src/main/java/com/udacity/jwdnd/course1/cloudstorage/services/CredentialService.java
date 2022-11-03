package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {


    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }


    public int createCredential(Credential credential) throws IllegalArgumentException {
        int credentialId;
        try{

            credential.setPassword(encryptionService.encryptValue(credential.getRawpassword(), credential.getSalt()));
            credentialId = credentialMapper.createCredential(credential);

        }catch (Exception e){
            throw new IllegalArgumentException("an error occurred while storing your credentials"+ e.getMessage());
        }
        return credentialId;
    }

    public List<Credential> getAllCredentials(int userid) throws DataNotAvailableException{

        List<Credential> userCredentials;
        try{
            userCredentials = credentialMapper.getCredentialsByUserid(userid)
                    .stream().peek(cred -> cred.setRawpassword(encryptionService.decryptValue(cred.getPassword(), cred.getSalt()))).collect(Collectors.toList());


        }catch (DataNotAvailableException e){
            throw new DataNotAvailableException("No credentials were found for this user"+e.getMessage());
        }

        return userCredentials;
    }

    public void updateCredential(Credential credential) throws Exception{

        try{
            credential.setPassword(encryptionService.encryptValue(credential.getRawpassword(), credential.getSalt()));
            credentialMapper.updateCredential(credential);
        } catch (Exception e){
            throw new Exception("error updating the credentials"+e.getMessage());
        }

    }

    public void deleteCredential(int userid, int credentialid) throws Exception{
        try{
            credentialMapper.deleteCredential(userid, credentialid);
        }catch (Exception e){
            throw new Exception("Error while deleting the credential");
        }
    }

}
