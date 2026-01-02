package com.users.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "social_users")
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

    @OneToOne(mappedBy = "socialUser", cascade = CascadeType.ALL)
    @JoinColumn(name = "social_user_id", referencedColumnName = "id")
    private Profile socialProfile;

    public void setSocialProfile(Profile socialProfile) {
        socialProfile.setSocialUser(this);
        this.socialProfile = socialProfile;
    }

}
