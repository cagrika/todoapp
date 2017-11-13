package com.data.todo.repository;

import com.data.todo.repository.entity.UserDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface UserRepository extends CouchbaseRepository<UserDocument, String> {

    UserDocument findByName(String name);
}
