package space.bielsolososdev.rdl.domain.url.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "urls_redirect")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRedirect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Slug não pode estar vazio")
    @Size(max = 50, message = "Slug deve ter no máximo 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @NotBlank(message = "URL não pode estar vazia")
    @Size(max = 255, message = "URL deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String url;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}