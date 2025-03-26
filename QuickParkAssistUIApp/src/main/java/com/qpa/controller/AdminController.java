// package com.qpa.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.qpa.dto.LoginDTO;
// import com.qpa.entity.AuthUser;
// import com.qpa.service.AuthService;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpSession;

// @RequestMapping("/admin")
// @Controller
// public class AdminController {
//     @Autowired
//     private AuthService authService;

//     // Dummy admin credentials (replace with database authentication)
//     private static final String ADMIN_USERNAME = "admin";
//     private static final String ADMIN_PASSWORD = "password";

//     @GetMapping("/login")
//     public String showLoginPage(Model model) {
//         model.addAttribute("admin", new LoginDTO());
//         return "admin/login"; // This will render login.html
//     }

//     @PostMapping("/login")
//     public String login(@ModelAttribute LoginDTO loginDTO, HttpServletRequest request, HttpSErvletResponse) {
//             authService.loginAdmin(request, response, authUser)
//             return "redirect:/admin/"; // Redirect to dashboard
//         } else {
//             model.addAttribute("error", "Invalid username or password!");
//             return "login"; // Show login page with error message
//         }
//     }

//     @GetMapping("/")
//     public String showDashboard(HttpSession session) {
//         return "admin/index"; // Render dashboard.html
//     }

//     @GetMapping("/logout")
//     public String logout(HttpSession session) {
//         session.invalidate(); // Invalidate session
//         return "redirect:/login";
//     }
// }
