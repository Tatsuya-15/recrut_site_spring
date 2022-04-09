package jp.winschool.spring.jobboard.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jp.winschool.spring.jobboard.model.Account;
import jp.winschool.spring.jobboard.model.Company;
import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.service.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    
    @ModelAttribute("company")
    public Company currentCompany(Account account) {
        return account.getCompany();
    }
    
    @GetMapping("")
    public String index() {
        return "company/index";
    }
    
    @GetMapping("/edit")
    public String edit() {
        return "company/form";
    }
    
    @PostMapping("/edit")
    public String edit(@Valid Company company, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "company/form";
        }
        
        companyService.updateCompany(company);
        return "redirect:/company";
    }

    @GetMapping("/offer/create")
    public String createOffer(Offer offer, Company company) {
        offer.setCompany(company);
        return "company/offer/form";
    }
    
    @PostMapping("/offer/create")
    public String createOffer(@Valid Offer offer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "company/offer/form";
        }
        
        companyService.createOffer(offer);
        return "redirect:/company/offer/" + offer.getId();
    }

    @GetMapping("/offer/{offerId}")
    public String showOffer(@PathVariable("offerId") Offer offer, Model model) {
        checkOfferOwner(offer, model);
        
        model.addAttribute("offer", offer);
        return "company/offer/show";
    }

    @GetMapping("/offer/{offerId}/entry/{entryId}")
    public String showEntry(@PathVariable("entryId") Entry entry, Model model) {
        checkOfferOwner(entry.getOffer(), model);
        
        model.addAttribute("entry", entry);
        return "company/offer/entry";
    }
    
    @PostMapping("/offer/{offerId}/entry/{entryId}")
    public String processEntry(@PathVariable("entryId") Entry entry) {
        companyService.processEntry(entry);
        String path = "/company/offer/" + entry.getOffer().getId() + "/entry/" + entry.getId();
        return "redirect:" + path;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    private class ForbiddenOfferAccessException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public ForbiddenOfferAccessException(String message) {
            super(message);
        }
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class OfferNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public OfferNotFoundException(String message) {
            super(message);
        }
    }
    
    private void checkOfferOwner(Offer offer, Model model) {
        if (offer == null) {
            throw new OfferNotFoundException("Not Found");
        }
        
        Map<String, Object> map = model.asMap();
        Company company = (Company)map.get("company");
        if (!company.equals(offer.getCompany())) {
            throw new ForbiddenOfferAccessException("Forbidden");
        }
    }

	@GetMapping("/offer/{offerId}/edit")
	public String editOffer(@PathVariable("offerId") Offer offer, Model model) {
		checkOfferOwner(offer, model);

		model.addAttribute("offer", offer);
		return "company/offer/form";
	}
	
	@PostMapping("/offer/{offerId}/edit")
	public String editOffer(@Valid Offer offer, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "company/offer/form";
		}

		companyService.updateOffer(offer);
		return "redirect:/company/offer/" + offer.getId();
	}
}
