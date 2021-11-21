package com.meli.ipapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.meli.ipapi.models.db.Blacklist;

public interface BlacklistRepository extends CrudRepository<Blacklist, Long>{

}
