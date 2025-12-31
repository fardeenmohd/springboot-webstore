package com.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;

    @OneToOne
    private User socialUser;

    @OneToMany(mappedBy = "socialProfile")
    private List<Post> socialPosts = new ArrayList<>();

    public void setSocialUser(User socialUser) {
        this.socialUser = socialUser;
        if (socialUser.getSocialProfile() != this) {
            socialUser.setSocialProfile(this);
        }
    }
}
