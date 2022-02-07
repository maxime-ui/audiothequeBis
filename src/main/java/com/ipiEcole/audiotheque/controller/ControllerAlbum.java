package com.ipiEcole.audiotheque.controller;

import com.ipiEcole.audiotheque.model.Album;
import com.ipiEcole.audiotheque.Repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.ipiEcole.audiotheque.exception.GlobalException.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/albums")

public class ControllerAlbum {

    @Autowired
    private AlbumRepository albumRepository;

    //create an album
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public Album createAlbum(
            @RequestBody Album album
    ) {
        //it doesn't save the albums with the same name
        if (albumRepository.findByTitle(album.getTitle()) != null) {
            throw new EntityExistsException("album " + album.getTitle() + " already exist.");
        }
        //it does not accept title empty
        if (album.getTitle() == null) {
            throw new IllegalArgumentException("you should enter album title!");
        }

        return albumRepository.save(album);
    }

    @RequestMapping(
            value = "/{albumId}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAlbum(
            @PathVariable("albumId") Integer albumId
    ) {
        if (!albumRepository.existsById(albumId)) {
            throw new EntityNotFoundException("album id not found");
        }
        albumRepository.deleteById(albumId);
    }
}