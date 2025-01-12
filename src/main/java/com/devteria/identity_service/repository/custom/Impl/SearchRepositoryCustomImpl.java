package com.devteria.identity_service.repository.custom.Impl;

import com.devteria.identity_service.converter.UserConverter;
import com.devteria.identity_service.dto.response.PageResponse;
import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.repository.criteria.SearchCriteria;
import com.devteria.identity_service.repository.criteria.UserSearchQueryCriteriaConsumer;
import com.devteria.identity_service.repository.custom.SearchRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {

    @PersistenceContext
    protected EntityManager entityManager;
    private final UserConverter userConverter;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public PageResponse<?> searchUserByCriteria(int offset, int pageSize, String sortBy, String... search) {
        List<SearchCriteria> criteriaList = new ArrayList<>();

        if(search.length>0){
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])(.*)");
            for(String word: search){
                Matcher matcher = pattern.matcher(word);
                if(matcher.find()){
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3)));
                }
            }
        }

        List<User> users = getUsers(offset,pageSize,criteriaList,sortBy);
        Long totalElements = getTotalElements(criteriaList);

        Page<User> page = new PageImpl<>(users, PageRequest.of(offset,pageSize),totalElements);
        return PageResponse.builder()
                .pageNo(offset)
                .pageSize(pageSize)
                .totalElement(totalElements)
                .totalPage(page.getTotalPages())
                .items(users.stream().map(userConverter::convertUserResponse).toList())
                .build();
    }
    private List<User> getUsers(int offset, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);

        Predicate userPredicate = criteriaBuilder.conjunction();
        UserSearchQueryCriteriaConsumer consumer = new UserSearchQueryCriteriaConsumer(userPredicate,criteriaBuilder,userRoot);
        criteriaList.forEach(consumer);
        userPredicate = consumer.getPredicate();
        query.where(userPredicate);

        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String fieldName= matcher.group(1);
                String direction= matcher.group(3);
                if(direction.equalsIgnoreCase("asc")){
                    query.orderBy(criteriaBuilder.asc(userRoot.get(fieldName)));
                }
                else {
                    query.orderBy(criteriaBuilder.desc(userRoot.get(fieldName)));
                }
            }
        }
        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    private Long getTotalElements(List<SearchCriteria> criteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, criteriaBuilder, root);
        criteriaList.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
