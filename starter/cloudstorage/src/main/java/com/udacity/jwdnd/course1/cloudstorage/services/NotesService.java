package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Exceptions.DataNotAvailableException;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotesService {

    private UserService userService;
    private NotesMapper notesMapper;
    private AuthenticationService authenticationService;

    public NotesService(UserService userService, NotesMapper notesMapper, AuthenticationService authenticationService){

        this.userService = userService;
        this.notesMapper = notesMapper;
        this.authenticationService = authenticationService;

    }


    public int insertNotes(Notes notes) throws Exception{

        if(notes == null){
            throw new Exception("No notes to insert");
        }
        if(notes.getNotetitle().isEmpty()){
            throw new IllegalArgumentException("Notes Title cannot be empty");
        }

        int noteId;
        try {
            noteId = notesMapper.insertNote(notes);
        }catch (Exception e){
            throw new Exception("Something went wrong when creating the notes"+ e.getMessage());
        }

        return noteId;
    }

    public void updateNotes(Notes notes) throws Exception{

        if(notes == null){
            throw new IllegalArgumentException("No notes are present");
        }
        if(notes.getNotedescription().isEmpty()){
            throw new IllegalArgumentException("Notes description cannot be empty");
        }

        try{
/*
            userid = userService.getUserByUserName(authenticationService.getLoggedInUser().getName()).getUserid();
*/          notesMapper.updateNotes(notes);
        }catch (Exception e){
            throw new Exception("an error occurred while updating the note"+e.getMessage());
        }

    }



    public void deleteNotes(int noteId, int userid) throws Exception{

        try {
            notesMapper.deleteNotes(noteId, userid);
        }catch (Exception e){
            throw new Exception("Note not in db");
        }
    }


    // view notes

    public List<Notes> selectNotes(int userid) throws DataNotAvailableException {
        List<Notes> notesList;

       try {
            notesList = notesMapper.selectNotes(userid).stream()
                    .peek(notes -> {
                        if (notes.getNotedescription().length() > 10)
                            notes.setNotedescription(notes.getNotedescription().substring(0, 11));
                    }).collect(Collectors.toList());
        }catch (Exception e){
           throw new DataNotAvailableException("No Notes are associated with current user");
       }
        return notesList;
    }

}
