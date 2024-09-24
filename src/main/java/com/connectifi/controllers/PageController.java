package com.connectifi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.connectifi.helpers.Message;
import com.connectifi.helpers.MessageType;

import com.connectifi.entities.User;
import com.connectifi.forms.UserForm;
import com.connectifi.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("home")
    public String home(){
        return "home";
    }

        // about route

    @RequestMapping("about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }
    
        // services
    
    @RequestMapping("services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    

    @GetMapping("contact")
    public String contact() {
        return new String("contact");
    }

    @GetMapping("login")
    public String login() {
        return new String("login");
    }

    
    
    @GetMapping("register")
    public String register(Model model) {

        UserForm userForm=new UserForm();
        model.addAttribute("userForm", userForm);
        return new String("register");
    }
    
    @RequestMapping(value="do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {

        System.out.println("Processing Registration");

        System.out.println(userForm);

        if (rBindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic(
                "https://www.learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Fdurgesh_sir.35c6cb78.webp&w=1920&q=75");

        User savedUser = userService.saveUser(user);

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);


        return "redirect:register";
    }

    


}
