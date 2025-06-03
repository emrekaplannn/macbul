// src/main/java/com/macbul/platform/util/MapperUtil.java
package com.macbul.platform.util;

import com.macbul.platform.dto.UserDto;
import com.macbul.platform.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
