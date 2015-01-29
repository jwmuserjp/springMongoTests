package com.example.spring.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.spring.model.Conference;
import com.example.spring.model.Signup;

@Repository
public interface SignupRepository extends MongoRepository<Signup, String>
,QueryDslPredicateExecutor<Signup>
{
	
	List<Signup> findByConference(Conference con);

}