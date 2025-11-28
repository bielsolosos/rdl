package space.bielsolososdev.rdl.domain.users.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import space.bielsolososdev.rdl.domain.users.model.User;

public class UserSpecification implements Specification<User> {

    private static final long serialVersionUID = 1L;

    private String filter;
    private Long elementId;

    //TODO terminar
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("element").get("id"), elementId));

        if (filter != null && !filter.trim().isEmpty()) {
            String[] commonSearchParams = { "username", "email" };

            List<Predicate> searchPredicates = new ArrayList<>();

            for (String param : commonSearchParams) {
                searchPredicates.add(cb.like(cb.lower(root.get(param)), "%" + filter.toLowerCase().trim() + "%"));
            }

            predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}