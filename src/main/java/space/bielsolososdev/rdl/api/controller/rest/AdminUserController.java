package space.bielsolososdev.rdl.api.controller.rest;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.annotations.IsAdmin;
import space.bielsolososdev.rdl.api.mapper.UserMapper;
import space.bielsolososdev.rdl.api.model.MessageResponse;
import space.bielsolososdev.rdl.api.model.user.ChangePasswordRequest;
import space.bielsolososdev.rdl.api.model.user.EditUserRequest;
import space.bielsolososdev.rdl.api.model.user.UserResponse;
import space.bielsolososdev.rdl.domain.users.service.AdminUserService;

@Tag(name = "Admin Users")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@IsAdmin
public class AdminUserController {

    private final AdminUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> listUsers(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore) {

        Page<UserResponse> response = userService.listUsers(pageable, filter, isActive, createdAfter, createdBefore)
                .map(UserMapper::toUserResponse);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/credentials")
    public ResponseEntity<UserResponse> editUser(
            @PathVariable Long id,
            @Valid @RequestBody EditUserRequest request) {
        
        return ResponseEntity.ok(UserMapper
                .toUserResponse(userService.adminEditUser(id, request.username(), request.email())));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<MessageResponse> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        
        userService.adminChangePassword(id, request.newPassword());
        return ResponseEntity.ok(new MessageResponse("Senha alterada com sucesso"));
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<UserResponse> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toUserResponse(userService.toggleUserActive(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("Usuário deletado com sucesso"));
    }

}