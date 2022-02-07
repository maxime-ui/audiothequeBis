package com.ipiEcole.audiotheque.Repository;

import com.ipiEcole.audiotheque.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

    Album findByTitle(String title);

}

