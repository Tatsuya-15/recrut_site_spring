package jp.winschool.spring.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.jobboard.model.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}
