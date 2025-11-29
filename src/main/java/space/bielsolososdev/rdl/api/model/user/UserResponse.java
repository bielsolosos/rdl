package space.bielsolososdev.rdl.api.model.user;

import java.time.LocalDateTime;

public record UserResponse(Long id, String username, String email, LocalDateTime createdAt){}