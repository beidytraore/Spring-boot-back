package com.example.myBackend.Backend.controller;



@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/allusers")
    public String displayUsers() {
        return "Display All Users";
    }

    @GetMapping("/displayuser")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String displayToUser() {
        return "Display to both user and admin";
    }

    @GetMapping("/displayadmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String displayToAdmin() {
        return "Display only to admin";
    }
}