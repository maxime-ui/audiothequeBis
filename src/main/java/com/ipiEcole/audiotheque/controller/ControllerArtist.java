package com.ipiEcole.audiotheque.controller;

import com.ipiEcole.audiotheque.Repository.ArtistRepository;
import com.ipiEcole.audiotheque.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController

@RequestMapping(value = "/artists")

    public class ControllerArtist {
    @Autowired
    private ArtistRepository artistRepository;

    //Recherche d'un artiste par son id
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public Artist artist(
            @PathVariable(value = "id") Integer id
    ){
        Optional<Artist> artist = artistRepository.findById(id);

        if (artist.isEmpty()){
            throw new EntityNotFoundException("artist with this : " + id + " id not found.");
        }

        return artist.get();
    }
    //search by name by url
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "name"
    )
    @ResponseStatus(HttpStatus.OK)
    public Artist artist(
            @RequestParam("name") String name
    ){
        Artist artistName = artistRepository.findByName(name);

        if (artistName == null ){
            throw new EntityNotFoundException("artist with this name : " + name + " not found.");
        }
        return artistName;
    }
    //search the name in searchbar
    @RequestMapping(
            params = {"name","page","size","sortProperty","sortDirection"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<Artist> searchByName(
            @RequestParam(value = "name") String nameSearch,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){

        if (page<0){
            throw new IllegalArgumentException("page should be valide");//erreur 400
        }
        if (page>27){
            throw new IllegalArgumentException("page should be more than 27");//erreur 400
        }
        if (size<=0 || size>=50){
            throw new IllegalArgumentException("the page should be between 0-50");//erreur 400
        }
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("the parameter should be ASC ou DESC");
        }
        if (!sortProperty.equalsIgnoreCase("name")){
            throw new IllegalArgumentException("the entry is not valide");//erreur 400
        }

        Pageable pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        return artistRepository.findByNameIgnoreCase(nameSearch, pageRequest);

    }
    //list the artists
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"page","size","sortProperty","sortDirection"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<Artist> artistsList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        if (page < 0){
            throw new IllegalArgumentException("page should be valide");//erreur 400
        }
        if (page>27){
            throw new IllegalArgumentException("page should be more than 27");//erreur 400
        }
        if (size<=0 || size>=50){
            throw new IllegalArgumentException("the page should be between 0-50");//erreur 400
        }
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("the parameter should be ASC ou DESC");
        }
        if (!sortProperty.equalsIgnoreCase("name")){
            throw new IllegalArgumentException("the entry is not valide");//erreur 400
        }

        return artistRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));
    }

    //lets create
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public Artist createArtist(
            @RequestBody Artist artist
    ){
        if (artistRepository.findByName(artist.getName()) != null){
            throw new EntityExistsException("artist " + artist.getName() + " is already exist!");
        }

        return artistRepository.save(artist);
    }

    //modify
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public Artist modifyArtist(
            @PathVariable (value = "id") Integer id,
            @RequestBody Artist artist
    ){
        if (!artistRepository.existsById(id)){
            throw new EntityNotFoundException("artist with this " + id + " not found!");
        }

        if (artistRepository.findByName(artist.getName()) != null){
            throw new EntityExistsException("artist " + artist.getName() + " already exist");
        }

        return artistRepository.save(artist);
    }

    //now delete artist
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArtist(
            @PathVariable (value = "id") Integer id
    ){
        if (!artistRepository.existsById(id)){
            throw new EntityNotFoundException("artist with this id " + id + "not found");
        }

        artistRepository.deleteById(id);
    }


    }
