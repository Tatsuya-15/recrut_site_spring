package jp.winschool.spring.jobboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.jobboard.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}
