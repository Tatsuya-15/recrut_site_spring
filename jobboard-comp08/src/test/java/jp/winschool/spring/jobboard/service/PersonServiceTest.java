package jp.winschool.spring.jobboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.model.Person;
import jp.winschool.spring.jobboard.repository.EntryRepository;
import jp.winschool.spring.jobboard.repository.PersonRepository;

@SpringJUnitConfig
public class PersonServiceTest {
    @TestConfiguration
    static class Config {
        @Bean
        public PersonService personService() {
            return new PersonService();
        }
    }
    
    @Autowired
    private PersonService personService;
    
    @MockBean
    private PersonRepository personRepository;
    
    @MockBean
    private EntryRepository entryRepository;
    
    @Test
    public void testCreateDefaultPerson() {
        Person person = personService.createDefaultValuePerson();
        assertThat(person.getName()).isNotBlank();
        assertThat(person.getMail()).isNotBlank();
        assertThat(person.getTel()).isNotBlank();
        assertThat(person.getBirthday()).isNotNull();
        verify(personRepository).save(person);
    }
    
    @Test
    public void testUpdatePerson() {
        Person person = new Person();
        personService.updatePerson(person);
        verify(personRepository).save(person);
    }
    
    @Test
    public void testGetEntryExists() {
        Offer offer = new Offer();
        Person person = new Person();
        Entry entry = new Entry();
        entry.setOffer(offer);
        entry.setPerson(person);
        when(entryRepository.findByOfferAndPerson(offer, person)).thenReturn(entry);
        
        Entry e = personService.getEntry(offer, person);
        assertThat(e).isEqualTo(entry);
        verify(entryRepository).findByOfferAndPerson(offer, person);
    }
    
    @Test
    public void testGetEntryNotExists() {
        Offer offer = new Offer();
        Person person = new Person();
        when(entryRepository.findByOfferAndPerson(offer, person)).thenReturn(null);
        
        Entry e = personService.getEntry(offer, person);
        assertThat(e).isNotNull();
        verify(entryRepository).findByOfferAndPerson(offer, person);
    }
    
    @Test
    public void testCreateEntry() {
        Entry entry = new Entry();
        personService.createEntry(entry);
        verify(entryRepository).save(entry);
    }
}
