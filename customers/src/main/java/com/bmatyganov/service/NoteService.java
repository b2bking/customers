package com.bmatyganov.service;

import java.util.Optional;

import com.bmatyganov.model.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {

    Page<Note> findAllByCustomerId(long id, Pageable pageable);

    void save(Note note);

    Optional<Note> findOne(long id);
}
