package com.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "profile_groups",
            joinColumns = @JoinColumn(name = "social_profile"),
            inverseJoinColumns = @JoinColumn(name = "social_group")
    )
    private Set<Group> socialGroups = new HashSet<>();

    @OneToMany(mappedBy = "socialProfile")
    private List<Post> socialPosts = new ArrayList<>();

    public void setSocialUser(User socialUser) {
        this.socialUser = socialUser;
        if (socialUser.getSocialProfile() != this) {
            socialUser.setSocialProfile(this);
        }
    }
}
