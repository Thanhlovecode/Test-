package com.devteria.identity_service.repository.custom;

import com.devteria.identity_service.dto.response.PageResponse;

public interface SearchRepositoryCustom {
    PageResponse<?> searchUserByCriteria(int offset,int pageSize,String sortBy,String...search);
}
