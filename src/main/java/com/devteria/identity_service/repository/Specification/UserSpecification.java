package com.devteria.identity_service.repository.Specification;

import com.devteria.identity_service.dto.request.UserRequest;
import com.devteria.identity_service.entity.User;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public final class UserSpecification {
    private UserSpecification() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    public static Specification<User> filter(UserRequest userRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Field field : userRequest.getClass().getDeclaredFields()) {
                Object value = getFieldValue(field, userRequest);
                if (value != null && StringUtils.hasLength(value.toString())) {
                    predicates.add(cb.like(root.get(field.getName()), "%" + value + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    private static Object getFieldValue(Field field, UserRequest userRequest) {
        try {
            if(field.trySetAccessible()){
                return field.get(userRequest);
            }
        } catch (IllegalAccessException e) {
            log.error("cannot access field :{}",field.getName(),e);
        }
        return null;
    }
}
