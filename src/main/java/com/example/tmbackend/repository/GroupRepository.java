package com.example.tmbackend.repository;

import com.example.tmbackend.model.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    Group findByName(String name);

}
