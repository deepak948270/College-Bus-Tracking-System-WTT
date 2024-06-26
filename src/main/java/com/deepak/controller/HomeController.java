package com.deepak.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.deepak.entity.User;
import com.deepak.service.UserService;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = { "/home", "/" })
    public String viewHomePage(Authentication authentication, Model model) {

        if (authentication != null) {

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Extract roles from authorities
            Collection<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Now you have roles in the 'roles' collection
            // You can iterate over it or perform any other operations
            for (String role : roles) {
                if (role.equals("ROLE_USER")) {
                    model.addAttribute("role", authentication.getName() + " Logged As USER ");
                } else if (role.equals("ROLE_ADMIN")) {
                    model.addAttribute("role", authentication.getName() + " Logged As ADMIN");
                } else if (role.equals("ROLE_DRIVER")) {
                    model.addAttribute("role", authentication.getName() + " Logged As DRIVER");
                }

            }

        } else {
            model.addAttribute("role", "Yet Not Logged !! ");
        }

        return "home";
    }

    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    @GetMapping(value = "/profile")
    public String profile(Principal principal, Model model) {
        // call the repository to get the user
        User user = userService.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found "));
        model.addAttribute("name", principal.getName());
        model.addAttribute("description", user.getDescription());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @GetMapping(value = "/viewUsers")
    public String viewUsers(Model model) {
        /* List<SecurityUser> users = userRepository.findByRole("ROLE_USER"); */

        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "users";
    }

    // show createUser view
    @GetMapping(value = "/createUser")
    public String createUser() {
        return "createUser";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/newLogin")
    public String newLogin() {
        return "newLogin";
    }

    @GetMapping("/accessDenied")
    public String accessdenied() {
        return "accessdenied";
    }

    @GetMapping("/mapInput")
    public String map1() {
        return "mapInput";
    }

    @GetMapping("/showMap")
    public String map2(@RequestParam(defaultValue = "agra") String start,
            @RequestParam(defaultValue = "punjab") String end,
            @RequestParam(defaultValue = "mathura delhi") String stops, Model model) {

        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("stops", stops);
        return "showMap";
    }

}
