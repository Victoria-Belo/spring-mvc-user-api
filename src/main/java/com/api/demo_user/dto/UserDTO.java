package com.api.demo_user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotNull
    @NotBlank(message = "CPF is mandatory")
    private String cpf;

    @NotBlank(message = "Pass is mandatory")
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isNamePresent() {
        return this.name != null && !this.name.trim().isEmpty();
    }

    public boolean isCpfPresent() {
        return this.cpf != null && !this.cpf.trim().isEmpty();
    }

    public boolean isEmailPresent() {
        return this.email != null && !this.email.trim().isEmpty();
    }

    public boolean isPassPresent() {
        return this.pass != null && !this.pass.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
