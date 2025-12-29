package com.mballem.demoparkapi.web.controller;


import com.mballem.demoparkapi.entity.Cliente;
import com.mballem.demoparkapi.exception.EntityNotFoundException;
import com.mballem.demoparkapi.jwt.JwtUserDetails;
import com.mballem.demoparkapi.repository.ClienteRepository;
import com.mballem.demoparkapi.service.ClienteService;
import com.mballem.demoparkapi.service.UsuarioService;
import com.mballem.demoparkapi.web.dto.ClienteCreateDto;
import com.mballem.demoparkapi.web.dto.ClienteResponseDto;
import com.mballem.demoparkapi.web.dto.mapper.ClienteMapper;
import com.mballem.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Contém todas as opereções relativas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")


public class ClienteController {
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final ClienteRepository clienteRepository;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Criar novo cliente",
            description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado"
    +"Requisição exige o uso de um bearer token. Acesso restrito ao Role='CLIENTE'",
    responses = {
            @ApiResponse(responseCode = "201",description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode ="409" ,description = "Cliente CPF já possui cadastro no sistema",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "422",description = "Recurso não processados por falta de dados OR dados inválidos",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "403",description = "Recurso NÃO permitido para perfil do tipo 'ADMIN'",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClienteResponseDto.class)))
    })

    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente pelo ID. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto>getById(@PathVariable Long id){
        Cliente cliente=clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
