package com.mballem.demoparkapi.service;

import com.mballem.demoparkapi.entity.Cliente;
import com.mballem.demoparkapi.entity.ClienteVaga;
import com.mballem.demoparkapi.entity.Vaga;
import com.mballem.demoparkapi.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class EstacionamentoService {
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;

    public ClienteVaga checkIn (ClienteVaga clienteVaga){
        Cliente cliente=clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga= vagaService.buscarPorVagaLivre();
        //supondo que tenha encontrado vaga livre vamos estacionar
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        //relacionar com ClienteVaga onde a vaga que está livre na tabela ficará ocupada
        clienteVaga.setVaga(vaga);
        //add data de entrada
        clienteVaga.setDataEntrada(LocalDateTime.now());
        //gerado recibo e inserido obj clientevaga na tabela
        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());
        //salvar BD
        return clienteVagaService.salvar(clienteVaga);
    }
}
