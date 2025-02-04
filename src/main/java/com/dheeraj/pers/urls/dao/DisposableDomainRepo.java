package com.dheeraj.pers.urls.dao;

import com.dheeraj.pers.urls.dao.entity.DisposableEmailDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisposableDomainRepo extends JpaRepository<DisposableEmailDomain, Long> {
    Optional<DisposableEmailDomain> findByDomain(String domain);
}
