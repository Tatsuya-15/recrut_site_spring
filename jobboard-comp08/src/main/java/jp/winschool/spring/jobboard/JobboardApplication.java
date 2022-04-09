package jp.winschool.spring.jobboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DuplicateKeyException;

import jp.winschool.spring.jobboard.service.AccountService;

@SpringBootApplication
public class JobboardApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JobboardApplication.class, args);
	}

    // JobboardApplicationは@DataJpaTestを付けたテストでもインスタンス化されるが
    // その場合AccountServiceインスタンスがないのでエラーになる。
    // 「required = false」とするとインスタンスがない場合でもエラーにならない。
    @Autowired(required = false)
    private AccountService accountService;
    
    @Override
    public void run(String... args) throws Exception {
        // インスタンスがDIされていない（テスト時）は実行しない
        if (accountService == null) {
            return;
        }
        
        String adminUsername = "admin";
        String adminPassword = "admin";
        
        try {
            accountService.createAdministratorAccount(adminUsername, adminPassword, true);
        } catch (DuplicateKeyException e) {
        }
    }

}
