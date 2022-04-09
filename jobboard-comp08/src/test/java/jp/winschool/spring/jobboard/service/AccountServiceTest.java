package jp.winschool.spring.jobboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jp.winschool.spring.jobboard.model.Account;
import jp.winschool.spring.jobboard.model.Company;
import jp.winschool.spring.jobboard.model.Person;
import jp.winschool.spring.jobboard.repository.AccountRepository;

@SpringJUnitConfig
public class AccountServiceTest {
    @TestConfiguration
    static class Config {
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }
    }
    
    @Autowired
    private AccountService accountService;
    
    @MockBean
    private AccountRepository accountRepository;
    
    @MockBean
    private CompanyService companyService;
    
    @MockBean
    private PersonService personService;
    
    @MockBean
    private SpringUserService springUserService;
    
    @Test
    public void testGetAccountList() {
        List<Account> accounts = accountService.getAccountList();
        verify(accountRepository).findAll();
    }
    
    @Test
    public void testFind() {
        Account account = new Account();
        when(accountRepository.findById("win")).thenReturn(Optional.of(account));
        
        Account a = accountService.find("win");
        verify(accountRepository).findById("win");
    }
    
    @Test
    public void testCreateAdministratorAccount() {
        Account account = accountService.createAdministratorAccount("win", "win", true);
        verify(accountRepository).save(account);
        verify(springUserService).createSpringUser("win", "win", "ADMINISTRATOR", true);
    }
    
    @Test
    public void testCreateCompanyAccount() {
        Company company = new Company();
        when(companyService.createDefaultValueCompany()).thenReturn(company);
        
        Account account = accountService.createCompanyAccount("win", "win", true);
        assertThat(account.getCompany()).isNotNull();
        verify(accountRepository).save(account);
        verify(companyService).createDefaultValueCompany();
        verify(springUserService).createSpringUser("win", "win", "COMPANY", true);
    }

	@Test
	public void testCreatePersonAccount() {
		Person person = new Person();
		when(personService.createDefaultValuePerson()).thenReturn(person);

		Account account = accountService.createPersonAccount("win", "win", true);
		assertThat(account.getPerson()).isNotNull();
		verify(accountRepository).save(account);
		verify(personService).createDefaultValuePerson();
		verify(springUserService).createSpringUser("win", "win", "PERSON", true);
	}

	@Test
	public void testUpdateAccount() {
		Account account = new Account();
		when(accountRepository.findById("win")).thenReturn(Optional.of(account));
		
		accountService.updateAccount("win", "win", true);
		verify(accountRepository).save(account);
		verify(springUserService).updateSpringUser("win", "win", true);
	}
}
