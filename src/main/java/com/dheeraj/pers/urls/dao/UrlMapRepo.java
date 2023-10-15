package com.dheeraj.pers.urls.dao;

import com.dheeraj.pers.urls.model.UrlShortMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMapRepo extends JpaRepository<UrlShortMapEntity, Long> {

}
