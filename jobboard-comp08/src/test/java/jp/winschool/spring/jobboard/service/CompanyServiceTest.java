package jp.winschool.spring.jobboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jp.winschool.spring.jobboard.model.Company;
import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.repository.CompanyRepository;
import jp.winschool.spring.jobboard.repository.EntryRepository;
import jp.winschool.spring.jobboard.repository.OfferRepository;

@SpringJUnitConfig
public class CompanyServiceTest {
    @TestConfiguration
    static class Config {
        @Bean
        public CompanyService companyService() {
            return new CompanyService();
        }
    }
    
    @Autowired
    private CompanyService companyService;
    
    @MockBean
    private CompanyRepository companyRepository;
    
    @MockBean
    private OfferRepository offerRepository;
    
    @MockBean
    private EntryRepository entryRepository;
    
    @Test
    public void testCreateDefaultCompany() {
        Company company = companyService.createDefaultValueCompany();
        assertThat(company.getName()).isNotBlank();
        assertThat(company.getUrl()).isNotBlank();
        assertThat(company.getMail()).isNotBlank();
        assertThat(company.getTel()).isNotBlank();
        verify(companyRepository).save(company);
    }
    
    @Test
    public void testUpdateCompany() {
        Company company = new Company();
        companyService.updateCompany(company);
        verify(companyRepository).save(company);
    }
    
    @Test
    public void testCreateOffer() {
        Offer offer = new Offer();
        companyService.createOffer(offer);
        verify(offerRepository).save(offer);
    }
    
    @Test
    public void testUpdateOffer() {
        Offer offer = new Offer();
        companyService.updateOffer(offer);
        verify(offerRepository).save(offer);
    }
    
    @Test
    public void testProcessEntry() {
        Entry entry = new Entry();
        companyService.processEntry(entry);
        assertThat(entry.getActive()).isEqualTo(false);
        verify(entryRepository).save(entry);
    }
}
