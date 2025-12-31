package com.users.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Email(message = "Invalid email format")
    @Valid
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @OneToOne(mappedBy = "socialUser")
    @JoinColumn(name = "social_user_id", referencedColumnName = "id")
    private Profile socialProfile;

    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "social_user"),
            inverseJoinColumns = @JoinColumn(name = "social_group")
    )
    private Set<Group> socialGroups = new HashSet<>();
}
