package com.data212.todo.repository;

import com.data212.todo.repository.entity.UserDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface UserRepository extends CouchbaseRepository<UserDocument, String> {

    UserDocument findByName(String name);
}
