package com.mballem.demoparkapi.web.dto.mapper;

import com.mballem.demoparkapi.entity.Cliente;
import com.mballem.demoparkapi.web.dto.ClienteCreateDto;
import com.mballem.demoparkapi.web.dto.ClienteResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

//@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ClienteMapper {

    public static Cliente toCliente (ClienteCreateDto dto){

        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto (Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}
