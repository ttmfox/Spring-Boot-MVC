package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
            UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("dat", "Hello form model");
        return "hello";
    }

    @GetMapping("/admin/user/create")
    public String getAdminPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping("/admin/user")
    public String getTableUser(Model model, @RequestParam("page") Optional<String> optionalPage) {
        int page = 1;
        try {
            if (optionalPage.isPresent()) {
                page = Integer.parseInt(optionalPage.get());
            } else {
                // page = 1
            }
        } catch (Exception e) {
            // page = 1
        }
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<User> users = this.userService.getAllProducts(pageable);
        List<User> listUsers = users.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("users", listUsers);

        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getDetailUser(Model model, @PathVariable Long id) {
        System.out.println("user id = " + id);
        User user = this.userService.getUserById(id);

        // String roleName = user.getRole().getName();
        model.addAttribute("id", id);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("address", user.getAddress());
        if (user.getRole() != null) {
            model.addAttribute("roleName", user.getRole().getName());
        }
        return "admin/user/detail";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(Model model,
            @ModelAttribute("newUser") @Valid User hoidanNTD,
            BindingResult newUserBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>> " + error.getField() + " - " + error.getDefaultMessage());
        }

        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }

        String avatar = this.uploadService.handleSaveFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(hoidanNTD.getPassword());

        hoidanNTD.setAvatar(avatar);
        hoidanNTD.setPassword(hashPassword);
        hoidanNTD.setRole(this.userService.getRoleByName(hoidanNTD.getRole().getName()));
        // Save
        this.userService.handleSaveUser(hoidanNTD);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("userUpdate", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(Model model,
            @ModelAttribute("userUpdate") @Valid User userU,
            BindingResult userBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        List<FieldError> errors = userBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>> " + error.getField() + " - " + error.getDefaultMessage());
        }
        if (userBindingResult.hasFieldErrors("fullName")) {
            return "admin/user/update";
        }

        User currentUser = this.userService.getUserById(userU.getId());
        String avatar = this.uploadService.handleSaveFile(file, "avatar");

        if (currentUser != null) {
            currentUser.setFullName(userU.getFullName());
            currentUser.setAddress(userU.getAddress());
            currentUser.setPhone(userU.getPhone());
            currentUser.setRole(this.userService.getRoleByName(userU.getRole().getName()));
            currentUser.setAvatar(avatar);

            this.userService.handleSaveUser(currentUser);
        }

        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User ntd) {
        this.userService.deleteAUser(ntd.getId());
        return "redirect:/admin/user";
    }

}