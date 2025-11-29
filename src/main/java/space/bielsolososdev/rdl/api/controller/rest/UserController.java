package space.bielsolososdev.rdl.api.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UserMapper;
import space.bielsolososdev.rdl.api.model.MessageResponse;
import space.bielsolososdev.rdl.api.model.user.ChangePasswordRequest;
import space.bielsolososdev.rdl.api.model.user.CreateUserRequest;
import space.bielsolososdev.rdl.api.model.user.EditUserRequest;
import space.bielsolososdev.rdl.api.model.user.UserResponse;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.users.service.UserService;
import space.bielsolososdev.rdl.infrastructure.RdlProperties;

@Tag(name = "Users", description = "Operações Utilizadas pelo próprio usuário")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final RdlProperties props;

    @PatchMapping("/change-password")
    public ResponseEntity<MessageResponse> postMethodName(@Valid @RequestBody ChangePasswordRequest request) {
        service.changePassword(service.getMe().getId(), request.oldPassword(), request.newPassword());
        return ResponseEntity.ok(new MessageResponse("Senha alterada com sucesso"));
    }

    @PatchMapping("/edit-credentials")
    public ResponseEntity<UserResponse> editUser(@Valid @RequestBody EditUserRequest request) {
        return ResponseEntity.ok(UserMapper
                .toUserResponse(service.editUser(service.getMe().getId(), request.username(), request.email())));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request)
            throws NoHandlerFoundException {
        if (!props.getRegistrationEnabled())
            throw new NoHandlerFoundException(
                    "POST",
                    "/api/users/register",
                    new org.springframework.http.HttpHeaders());

        if (!request.password().equals(request.confirmPassword()))
            throw new BusinessException("As senhas não conferem");

        UserResponse userResponse = UserMapper.toUserResponse(
                service.createUser(request.username(), request.email(), request.password()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

}
