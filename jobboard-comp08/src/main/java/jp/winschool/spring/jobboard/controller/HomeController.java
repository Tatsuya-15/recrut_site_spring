package jp.winschool.spring.jobboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jp.winschool.spring.jobboard.model.Offer;
import jp.winschool.spring.jobboard.service.HomeService;

@Controller
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/")
    public String home(Model model) {
        List<Offer> offers = homeService.getOfferList();
        model.addAttribute("offers", offers);

        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String word, @RequestParam String prefecture, Model model) {
        List<Offer> offers = homeService.findOfferList(word, prefecture);
        model.addAttribute("offers", offers);
    
        return "index";
    }
    
    @GetMapping("/offer/{offerId}")
    public String showOffer(@PathVariable("offerId") Offer offer, Model model) {
        model.addAttribute("offer", offer);
        return "offer";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
