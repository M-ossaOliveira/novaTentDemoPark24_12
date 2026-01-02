package com.mballem.demoparkapi.web.controller;
import com.mballem.demoparkapi.entity.ClienteVaga;
import com.mballem.demoparkapi.service.EstacionamentoService;
import com.mballem.demoparkapi.web.dto.EstacionamentoCreateDto;
import com.mballem.demoparkapi.web.dto.EstacionamentoResponseDto;
import com.mballem.demoparkapi.web.dto.mapper.ClienteVagaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/estacionamentos")
@RequiredArgsConstructor
public class EstacionamentoController {
    //injetar estacionamento service
    private final EstacionamentoService estacionamentoService;

    //1o metodo

    public ResponseEntity<EstacionamentoResponseDto> checkin
            (@RequestBody @Valid EstacionamentoCreateDto dto){
        //instrução transforma objeto dto em clienteVaga
        ClienteVaga clienteVaga= ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        //transforma o que for retornado pelo service em um responseDto
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location= ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    // retorna cabeçalho com url de acesso + corpo com infos referentes ao processo de criação
        }
}
