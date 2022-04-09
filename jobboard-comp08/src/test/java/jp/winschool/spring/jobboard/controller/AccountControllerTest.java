package jp.winschool.spring.jobboard.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.winschool.spring.jobboard.model.Account;
import jp.winschool.spring.jobboard.model.AccountForm;
import jp.winschool.spring.jobboard.service.AccountService;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AccountService accountService;
    
    // WebSecurityConfigで定義されているUserDetailManagerを上書き
    @MockBean
    private UserDetailsManager userDetailsManager;
    
    // staticを付けている理由は次項で説明します
    private static List<Account> accounts;
    private static Account account;
    
    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setUsername("win");
        account.setType("administrator");
        account.setActive(true);
        accounts = Arrays.asList(account);
    }
    
    @Test
    public void testIndexNotLogin() throws Exception {
        mockMvc.perform(get("/account"))
            .andExpect(status().isFound());
    }
    
    @Test
    @WithMockUser
    public void testIndexHasNotPermission() throws Exception {
        mockMvc.perform(get("/account"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testIndexHasPermission() throws Exception {
        when(accountService.getAccountList()).thenReturn(accounts);
        
        mockMvc.perform(get("/account"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/index"))
            .andExpect(model().attribute("accounts", accounts));
    }
    
    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testCreateGet() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "administrator");
        MvcResult result = mockMvc.perform(get("/account/create").params(params))
            .andExpect(status().isOk())
            .andExpect(view().name("account/create"))
            .andReturn();
        AccountForm form = (AccountForm)result.getModelAndView().getModel().get("accountForm");
        assertThat(form.getType()).isEqualTo("administrator");
    }
    
    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testCreatePostSuccess() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "administrator");
        params.add("username", "win");
        params.add("password", "12345678");
        params.add("active", "true");
        mockMvc.perform(post("/account/create").with(csrf()).params(params))
            .andExpect(status().isFound());
    }
    
    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testCreatePostFailValidation() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "administrator");
        params.add("username", "win");
        params.add("password", "1234");
        params.add("active", "true");
        mockMvc.perform(post("/account/create").with(csrf()).params(params))
            .andExpect(view().name("account/create"))
            .andExpect(model().hasErrors());
    }
    
    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testCreatePostFailDuplicate() throws Exception {
        when(accountService.createAdministratorAccount("win", "12345678", true))
            .thenThrow(new DuplicateKeyException(""));
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "administrator");
        params.add("username", "win");
        params.add("password", "12345678");
        params.add("active", "true");
        mockMvc.perform(post("/account/create").with(csrf()).params(params))
            .andExpect(view().name("account/create"))
            .andExpect(model().hasErrors());
    }

    @TestConfiguration
    static class Config implements WebMvcConfigurer {
        
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(String.class, Account.class, id -> account);
        }
        
    }

    @Test
    @WithMockUser(roles="ADMINISTRATOR")
    public void testEditGet() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/win/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/edit"))
            .andReturn();
        AccountForm form = (AccountForm)result.getModelAndView().getModel().get("accountForm");
        assertThat(form.getUsername()).isEqualTo(account.getUsername());
        assertThat(form.getType()).isEqualTo(account.getType());
        assertThat(form.getActive()).isEqualTo(account.getActive());
    }

	@Test
	@WithMockUser(roles="ADMINISTRATOR")
	public void testEditPostSuccess() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("type", "administrator");
		params.add("username", "win");
		params.add("password", "12345678");
		params.add("active", "true");
		mockMvc.perform(post("/account/win/edit").with(csrf()).params(params))
			.andExpect(status().isFound());
	}

	@Test
	@WithMockUser(roles="ADMINISTRATOR")
	public void testEditPostFail() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("type", "administrator");
		params.add("username", "win");
		params.add("password", "1234");
		params.add("active", "true");
		mockMvc.perform(post("/account/win/edit").with(csrf()).params(params))
			.andExpect(view().name("account/edit"))
			.andExpect(model().hasErrors());
	}
}
