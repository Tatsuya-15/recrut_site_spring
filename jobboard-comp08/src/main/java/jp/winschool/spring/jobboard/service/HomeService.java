package jp.winschool.spring.jobboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.repository.OfferRepository;

@Service
public class HomeService {
    @Autowired
    private OfferRepository offerRepository;
    
    public List<Offer> getOfferList() {
        return offerRepository.findByActiveTrue();
    }
    
    public List<Offer> findOfferList(String word, String prefecture) {
        return offerRepository.findByActiveTrueAndTitleContainsAndPrefectureContains(word, prefecture);
    }
}
