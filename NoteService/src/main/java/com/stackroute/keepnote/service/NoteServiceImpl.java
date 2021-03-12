package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService{

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	NoteRepository noteRepository;
	private NoteUser noteUser = null;
    private List<Note> notes = null;
	
	public NoteServiceImpl(NoteRepository noteRepository)
	{
		this.noteRepository=noteRepository;
	}
	
	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		boolean status=false;
		if(noteRepository.existsById(note.getNoteCreatedBy()))
		{
			noteUser=noteRepository.findById(note.getNoteCreatedBy()).get();
			noteUser.getNotes().add(note);
			//noteRepository.insert(noteUser);
			if(noteRepository.insert(noteUser)==null)
				return false;
			else
				return true;
		}
		else
		{
			noteUser=new NoteUser();
			noteUser.setUserId(note.getNoteCreatedBy());
			notes=new ArrayList();
			notes.add(note);
			noteUser.setNotes(notes);
//			noteRepository.insert(noteUser);
			if(noteRepository.insert(noteUser)==null)
				return false;
			else
				return true;
		}
//		return true;
	}
	
	/* This method should be used to delete an existing note. */

	
	public boolean deleteNote(String userId, int noteId) {
		boolean status=false;
		NoteUser noteUser=new NoteUser();
		notes=noteRepository.findById(userId).get().getNotes();
		if(!notes.isEmpty())
		{
			Iterator itr=notes.iterator();
			while(itr.hasNext())
			{
				Note not=(Note) itr.next();
				if(not.getNoteId()==noteId)
				itr.remove();
			}
			noteUser.setUserId(userId);
			noteUser.setNotes(notes);
			noteRepository.save(noteUser);
			status=true;
		}
		return status;
			
		
	}
	
	/* This method should be used to delete all notes with specific userId. */

	
	public boolean deleteAllNotes(String userId) {
		NoteUser noteUser=new NoteUser();
		if(noteRepository.findById(userId).get()==null)
			return false;
		noteRepository.findById(userId).get().setNotes(null);
		return true;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		NoteUser noteUser;
		List<Note> notelist;
		try {
		 notelist=noteRepository.findById(userId).get().getNotes();
		 noteUser=noteRepository.findById(userId).get();
		}catch(NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("Note Does not exists");
		}
		Iterator itr=notelist.iterator();
		Note note1=null;
		while(itr.hasNext())
		{
			 note1=(Note) itr.next();
			if(note1.getNoteId()==id)
			{
				note1.setNoteTitle(note.getNoteTitle());
				note1.setNoteContent(note.getNoteContent());
				note1.setCategory(note.getCategory());
				note1.setNoteCreatedBy(note.getNoteCreatedBy());
				note1.setNoteCreationDate(new Date());
				note1.setNoteId(note.getNoteId());
				note1.setReminders(note.getReminders());
				break;
			}
		}
		if(note1.getNoteId()!=id)
		{
			throw new NoteNotFoundExeption("no note found");
		}
		noteUser.setUserId(userId);
		noteUser.setNotes(notelist);
		noteRepository.save(noteUser);
		return note;
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		//notes=noteRepository.findById(userId).get().getNotes();
		//if(notes.isEmpty())
//		{
	//		throw new NoteNotFoundExeption("no note is found");
		//}
		List<Note> notelist;
		try {
		 notelist=noteRepository.findById(userId).get().getNotes();
		}
		catch(NoSuchElementException exception)
		{
			throw new NoteNotFoundExeption("Note Does not exists");
		}
		Note note=null;
		Iterator<Note> itr=notelist.iterator();
		while(itr.hasNext())
		{
			note=itr.next();
			if(note.getNoteId()==noteId)
			{
				return note;
				
			}
		}
		if(note==null)
		throw new NoteNotFoundExeption("no note is found");
		return note;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		return noteRepository.findById(userId).get().getNotes();
		
	}

}
