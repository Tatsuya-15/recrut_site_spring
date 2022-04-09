package jp.winschool.spring.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.jobboard.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
