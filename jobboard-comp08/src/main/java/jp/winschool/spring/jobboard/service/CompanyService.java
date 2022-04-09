package jp.winschool.spring.jobboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.winschool.spring.jobboard.model.Company;
import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.repository.CompanyRepository;
import jp.winschool.spring.jobboard.repository.EntryRepository;
import jp.winschool.spring.jobboard.repository.OfferRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company createDefaultValueCompany() {
        Company company = new Company();
        company.setName("名前を設定してください");
        company.setUrl("please set url");
        company.setMail("please@set.email");
        company.setTel("000-0000-0000");
        companyRepository.save(company);
        return company;
    }

    @Autowired
    private OfferRepository offerRepository;
    
    @Autowired
    private EntryRepository entryRepository;
    
    public void updateCompany(Company company) {
        companyRepository.save(company);
    }
    
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }
    
    public void updateOffer(Offer offer) {
        offerRepository.save(offer);
    }
    
    public void processEntry(Entry entry) {
        entry.setActive(false);
        entryRepository.save(entry);
    }
}
