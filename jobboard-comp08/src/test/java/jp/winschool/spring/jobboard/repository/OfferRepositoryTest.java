package jp.winschool.spring.jobboard.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import jp.winschool.spring.jobboard.model.Offer;

@DataJpaTest
public class OfferRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private OfferRepository offerRepository;
    
    @BeforeEach
    public void setUp() {
        Offer offer1 = new Offer();
        offer1.setTitle("Javaエンジニア募集");
        offer1.setPrefecture("東京都");
        offer1.setAddress("新宿区");
        offer1.setActive(true);
        entityManager.persist(offer1);
    
        Offer offer2 = new Offer();
        offer2.setTitle("Springエンジニア募集");
        offer2.setPrefecture("大阪府");
        offer2.setAddress("大阪市");
        offer2.setActive(true);
        entityManager.persist(offer2);
    
        Offer offer3 = new Offer();
        offer3.setTitle("C#エンジニア募集");
        offer3.setPrefecture("愛知県");
        offer3.setAddress("名古屋市");
        offer3.setActive(false);
        entityManager.persist(offer3);
    
        Offer offer4 = new Offer();
        offer4.setTitle("Springエンジニア募集");
        offer4.setPrefecture("福岡県");
        offer4.setAddress("福岡市");
        offer4.setActive(true);
        entityManager.persist(offer4);
    }
    
    @Test
    public void testFindByActive() {
        List<Offer> offers = offerRepository.findByActiveTrue();
        assertThat(offers.size()).isEqualTo(3);
    }

	@Test
	public void testFindByActiveAndTitle() {
		List<Offer> offers = offerRepository.findByActiveTrueAndTitleContainsAndPrefectureContains("Spring", "");
		assertThat(offers.size()).isEqualTo(2);
	}

	@Test
	public void testFindByActiveAndPrefecture() {
		List<Offer> offers = offerRepository.findByActiveTrueAndTitleContainsAndPrefectureContains("", "東京都");
		assertThat(offers.size()).isEqualTo(1);
	}

	@Test
	public void testFindByActiveAndTitleAndPrefecture() {
		List<Offer> offers = offerRepository.findByActiveTrueAndTitleContainsAndPrefectureContains("Spring", "大阪府");
		assertThat(offers.size()).isEqualTo(1);
	}
}
