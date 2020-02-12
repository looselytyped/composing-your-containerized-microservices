package com.looselytyped.persistence.repo;

import com.looselytyped.persistence.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
