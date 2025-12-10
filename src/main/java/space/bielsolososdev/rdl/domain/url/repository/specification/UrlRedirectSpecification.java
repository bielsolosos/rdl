package space.bielsolososdev.rdl.domain.url.repository.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;

@AllArgsConstructor
public class UrlRedirectSpecification implements Specification<UrlRedirect> {

    private static final long serialVersionUID = 1L;

    private String filter;
    private Boolean isEnabled;
    private Long userId;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;

    @Override
    public Predicate toPredicate(Root<UrlRedirect> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter != null && !filter.trim().isEmpty()) {
            String[] searchParams = { "slug", "url" };

            List<Predicate> searchPredicates = new ArrayList<>();

            for (String param : searchParams) {
                searchPredicates.add(cb.like(cb.lower(root.get(param)), "%" + filter.toLowerCase().trim() + "%"));
            }

            predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
        }

        if (isEnabled != null) {
            predicates.add(cb.equal(root.get("isEnabled"), isEnabled));
        }

        if (userId != null) {
            predicates.add(cb.equal(root.get("user").get("id"), userId));
        }

        if (createdAfter != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));
        }

        if (createdBefore != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
