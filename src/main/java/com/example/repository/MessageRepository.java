package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.Message;

public interface MessageRepository extends  CrudRepository<Message, Long> {

	Iterable<Message> findByTag(String tag);

}
