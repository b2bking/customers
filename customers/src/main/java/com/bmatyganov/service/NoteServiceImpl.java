package com.bmatyganov.service;

import java.util.Optional;

import com.bmatyganov.model.Note;
import com.bmatyganov.repository.NoteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }

    @Override
    public Page<Note> findAllByCustomerId(long id, Pageable pageable){
        return noteRepository.findByCustomerId(id, pageable);
    }

    @Override
    public Optional<Note> findOne(long id){
        return noteRepository.findById(id);
    }

    @Override
    public void save(Note note){
        noteRepository.save(note);
    }
}
