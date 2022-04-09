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
import jp.winschool.spring.jobboard.model.Entry;
import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.model.Person;
import jp.winschool.spring.jobboard.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;
    
    @ModelAttribute("person")
    public Person currentPerson(Account account) {
        return account.getPerson();
    }
    
    @GetMapping("")
    public String index() {
        return "person/index";
    }
    
    @GetMapping("/edit")
    public String edit() {
        return "person/form";
    }
    
    @PostMapping("/edit")
    public String edit(@Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/form";
        }
        
        personService.updatePerson(person);
        return "redirect:/person";
    }

    @GetMapping("/offer/{offerId}")
    public String showOffer(@PathVariable("offerId") Offer offer, Person person, Model model) {
        Entry entry = personService.getEntry(offer, person);
        model.addAttribute("entry", entry);
        
        return "person/offer/form";
    }
    
    @PostMapping("/offer/{offerId}")
    public String entryOffer(@Valid Entry entry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/offer/form";
        }
        
        personService.createEntry(entry);
        return "redirect:/person";
    }

	@ResponseStatus(HttpStatus.FORBIDDEN)
	private class ForbiddenEntryAccessException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		public ForbiddenEntryAccessException(String message) {
			super(message);
		}
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	private class EntryNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		public EntryNotFoundException(String message) {
			super(message);
		}
	}

	private void checkEntryOwner(Entry entry, Model model) {
		if (entry == null) {
			throw new EntryNotFoundException("Not Found");
		}
		
		Map<String, Object> map = model.asMap();
		Person person = (Person)map.get("person");
		if (!person.equals(entry.getPerson())) {
			throw new ForbiddenEntryAccessException("Forbidden");
		}
	}
	
	@GetMapping("/entry/{entryId}")
	public String showEntry(@PathVariable("entryId") Entry entry, Model model) {
		checkEntryOwner(entry, model);

		model.addAttribute("entry", entry);
		return "person/offer/entry";
	}
}
