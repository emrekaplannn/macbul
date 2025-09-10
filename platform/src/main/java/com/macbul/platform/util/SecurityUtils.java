package com.macbul.platform.util;

import com.macbul.platform.model.User;
import com.macbul.platform.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserService userService;

    public SecurityUtils(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the authenticated user's account ID.
     */
    public String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        String email = auth.getName(); // in your setup, username = email
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found for email: " + email));
        return user.getId();
    }
}
