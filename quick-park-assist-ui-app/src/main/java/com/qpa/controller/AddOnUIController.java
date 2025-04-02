package com.qpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class AddOnUIController {

    @GetMapping("/view-addon")
    public String viewAddOnPage() {
        return "admin/viewADDon";
    }
    @GetMapping("/new-addon")
    public String getMethodName() {
        return "admin/newADDon";
    }
    @GetMapping("/status")
    public String getMethodStatus() {
        return "admin/statusADDon";
    }
}
