package com.devteria.identity_service.converter;

import com.devteria.identity_service.dto.reponse.UserResponse;
import com.devteria.identity_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ModelMapper modelMapper;

    public UserResponse convertUserResponse(User user){
        return modelMapper.map(user,UserResponse.class);
    }

}
