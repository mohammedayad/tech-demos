package com.ayad.usertracking.domain.model;

import com.ayad.usertracking.domain.enums.UserId;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class User {

    public User(UserId userId) {
        this.userId = userId;
        this.preferences = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    private UserId userId;

    private String username;

    private Date dateOfBirth;
    private List<PreferredProduct> preferences;

    private List<String> suggestions;

}
