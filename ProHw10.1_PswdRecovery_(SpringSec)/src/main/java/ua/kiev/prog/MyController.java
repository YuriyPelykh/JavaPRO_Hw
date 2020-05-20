package ua.kiev.prog;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Random;

@Controller
public class MyController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/")
    public String index(Model model) {
        User user = getCurrentUser();

        String login = user.getUsername();
        CustomUser dbUser = userService.findByLogin(login);

        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin", isAdmin(user));
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());

        return "index";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone) {
        User user = getCurrentUser();

        String login = user.getUsername();
        userService.updateUser(login, email, phone);

        return "redirect:/";
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String update(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         Model model) {
        String passHash = passwordEncoder.encode(password);

        if (!userService.addUser(login, passHash, UserRole.USER, email, phone)) {
            model.addAttribute("exists", true);
            model.addAttribute("login", login);
            return "register";
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/recovery")
    public String recovery() {
        return "recovery";
    }

    @RequestMapping(value = "/password_recovery", method = RequestMethod.POST)
    public String recoveryStart(@RequestParam String login,
                                @RequestParam String email,
                                Model model) {
        Random rnd = new Random();
        //String recoveryHash = passwordEncoder.encode(String.valueOf(rnd.nextInt(1000000)));
        String recoveryHash = DigestUtils.md5Hex(String.valueOf(rnd.nextInt(1000000000)));

        if (userService.setRecoveryHash(login, email, recoveryHash)) {

            String link = "http://localhost:8080/password_recovery_confirmation/" + recoveryHash;
            String message = "This E-mail was sent automatically because someone requested a password recovery" +
                    " on http://localhost:8080 for user " + login + ". To finish password recovery procedure follow the link: " +
                    link + "\nIgnore this E-mail if it wasn't you.";

            MailSender sslMailSender = new MailSender();
            sslMailSender.send("Password Recovery", message, "yuriypelykh@gmail.com", email);

            model.addAttribute("recovery_need_confirm", true);
            model.addAttribute("login", login);
            model.addAttribute("email", email);

            return "recovery";
        } else {
            model.addAttribute("recovery_need_confirm", false);
        }

        return "recovery";
    }

    @RequestMapping("/password_recovery_confirmation/{hash}")
    public String recoveryProceed(
            @PathVariable(value = "hash") String hash,
            Model model) {

        CustomUser user = null;
        if ((user = userService.findByRecoveryHash(hash)) != null) {
            model.addAttribute("login", user.getLogin());
            return "newpassword";
        }

        return "recovery";
    }

    @RequestMapping(value = "/password_recovery_complete", method = RequestMethod.POST)
    public String recoveryComplete(@RequestParam String login,
                                   @RequestParam String password,
                                   @RequestParam String passwordConfirmation,
                                   Model model) {

        if (password.equals(passwordConfirmation)) {
            String passHash = passwordEncoder.encode(password);
            userService.updatePassword(login, passHash);
            model.addAttribute("recovery_complete", true);
            model.addAttribute("login", login);
            return "login";
        } else {
            model.addAttribute("err_passwords_mismatch", true);
        }

        return "newpassword";
    }

    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // !!!
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    // ----

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }
}
