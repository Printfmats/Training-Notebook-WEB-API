package com.example.demo.user_account;


import com.example.demo.security_log.SecurityUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class UserRestController {

        @RequestMapping("/")
        public String startingPage() {
            return "startpage";
        }

        @RequestMapping("/login")
        public String logingPage(){
            return "loginpage";
        }

        @RequestMapping("/register")
        public String registeringgPage(Model model) {
            return "registerpage";
        }
//        @RequestMapping(value = "/processForm", method= RequestMethod.POST)
//        public String processForm(@ModelAttribute(value="foo") UserAccount userAccount) {
//
//        }
        @RequestMapping("/api")
        public String apiPage() {
            return "apipage";
        }


//    	@Bean
//        CommandLineRunner commandLineRunner (UserAccountDAO repositoryUser){
//		return args -> {
//			repositoryUser.save(new UserAccount(2L,"Meksyk","1234","Meks123@wp.pl"));
//		};
//	}
}
