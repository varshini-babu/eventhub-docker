package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank
    private String login_email;
    @NotBlank
    private String login_password;
}
