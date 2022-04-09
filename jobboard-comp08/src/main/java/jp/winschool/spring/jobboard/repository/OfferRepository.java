package jp.winschool.spring.jobboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.jobboard.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
    List<Offer> findByActiveTrue();
    List<Offer> findByActiveTrueAndTitleContainsAndPrefectureContains(String word, String prefecture);
}
