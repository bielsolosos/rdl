package space.bielsolososdev.rdl.api.controller.web;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.annotations.IsAdmin;
import space.bielsolososdev.rdl.api.mapper.UserMapper;
import space.bielsolososdev.rdl.api.model.user.UserResponse;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.users.service.AdminUserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@IsAdmin
public class AdminController {
    
    private final AdminUserService adminUserService;

    @GetMapping
    public String adminPanel(
            Model model,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore) {

        // Busca usuários aplicando filtros e paginação
        Page<UserResponse> users = adminUserService
                .listUsers(pageable, filter, isActive, createdAfter, createdBefore)
                .map(UserMapper::toUserResponse);

        // Adiciona dados ao modelo para renderização
        model.addAttribute("users", users);
        model.addAttribute("filter", filter);
        model.addAttribute("isActive", isActive);
        model.addAttribute("createdAfter", createdAfter);
        model.addAttribute("createdBefore", createdBefore);

        return "admin/index";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        
        try {
            adminUserService.adminEditUser(id, username, email);
            redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/change-password")
    public String changePassword(
            @PathVariable Long id,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {
        
        if (newPassword == null || newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "A senha deve ter no mínimo 6 caracteres");
            return "redirect:/admin";
        }
        
        try {
            adminUserService.adminChangePassword(id, newPassword);
            redirectAttributes.addFlashAttribute("success", "Senha alterada com sucesso!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin";
    }

    /**
     * Alterna status ativo/inativo de um usuário
     */
    @PostMapping("/users/{id}/toggle-active")
    public String toggleActive(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            adminUserService.toggleUserActive(id);
            redirectAttributes.addFlashAttribute("success", "Status do usuário alterado!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            adminUserService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Usuário deletado com sucesso!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin";
    }
}
