package com.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trading.domain.USER_ROLE;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data //from lombok => provides the getter+setter method for our class
public class User { //attributs de l'user

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //generer id=clé primaire pour la bdd
    private Long id;
    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

}
