package cz.horejsi.usersapp.controller;

import cz.horejsi.usersapp.entity.User;
import cz.horejsi.usersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "user-list"})
    public String userList(@RequestParam(name = "deleteUserId", required = false) Long userId,
                           HttpServletRequest request, Model model) {

        if (userId != null) {
            userService.deleteUser(userId);
            request.setAttribute("taskComplete", "User with id " + userId + " deleted!");
        }

        model.addAttribute("users", userService.findAllUsers());

        return "user-list";
    }

    @GetMapping("edit-user")
    public String userForm(@RequestParam(name = "editUserId", required = false) Long userId, Model model) {

        if (userId != null) {
            model.addAttribute("user", userService.findUserById(userId));
        } else {
            model.addAttribute("user", new User());
        }

        return "user-form";
    }

    @PostMapping("edit-user")
    public String userFormSubmit(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes rm) {
        if (result.hasErrors()) { return "user-form"; }

        userService.saveUser(user);
        rm.addFlashAttribute("taskComplete", "User inserted/updated successfully!");

        return "redirect:user-list";
    }
}
