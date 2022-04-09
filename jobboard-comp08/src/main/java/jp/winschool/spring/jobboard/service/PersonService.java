package jp.winschool.spring.jobboard.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.model.Person;
import jp.winschool.spring.jobboard.repository.EntryRepository;
import jp.winschool.spring.jobboard.repository.PersonRepository;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person createDefaultValuePerson() {
        Person person = new Person();
        person.setName("名前を設定してください");
        person.setMail("please@set.email");
        person.setTel("000-0000-0000");
        person.setBirthday(LocalDate.of(1980, 1, 1));
        personRepository.save(person);
        return person;
    }

    @Autowired
    private EntryRepository entryRepository;
    
    public void updatePerson(Person person) {
        personRepository.save(person);
    }
    
    public Entry getEntry(Offer offer, Person person) {
        Entry entry = entryRepository.findByOfferAndPerson(offer, person);
        if (entry == null) {
            entry = new Entry();
            entry.setOffer(offer);
            entry.setPerson(person);
            entry.setActive(true);
        }
        return entry;
    }
    
    public Entry createEntry(Entry entry) {
        return entryRepository.save(entry);
    }
}
