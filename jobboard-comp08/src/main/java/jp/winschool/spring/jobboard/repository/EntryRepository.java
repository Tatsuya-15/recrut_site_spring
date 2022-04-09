package jp.winschool.spring.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.model.Person;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
    Entry findByOfferAndPerson(Offer offer, Person person);
}
