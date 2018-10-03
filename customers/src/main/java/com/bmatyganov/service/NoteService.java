package com.bmatyganov.service;

import java.util.Optional;

import com.bmatyganov.model.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    public Page<Note> findAllByCustomerId(long id, Pageable pageable);

    public void save(Note note);

    public Optional<Note> findOne(long id);
}
