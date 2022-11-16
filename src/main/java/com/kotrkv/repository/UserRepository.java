package com.kotrkv.repository;

import com.kotrkv.model.entity.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Long> {
}
