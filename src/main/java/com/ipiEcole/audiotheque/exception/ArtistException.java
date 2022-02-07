package com.ipiEcole.audiotheque.exception;

import com.ipiEcole.audiotheque.model.Artist;

public class ArtistException extends Throwable {

    public static final String ID = "the id not match with the artist : ";

    public ArtistException(String message, Artist artist, Object valeurIncorrecte) {
        super(message + valeurIncorrecte + ", artist : " + artist.toString());
        System.out.println(this.getMessage());
    }

    public ArtistException(String message) {
        super(message);
        System.out.println(this.getMessage());
    }
}