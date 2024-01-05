package com.br.fiap;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.fiap.controller.ClienteController;
import com.br.fiap.dto.ClienteNaFilaDTO;
import com.br.fiap.model.ClienteId;
import com.br.fiap.model.FilaAtendimento;
import com.br.fiap.repository.ClienteRepository;
import com.br.fiap.repository.FilaAtendimentoRepository;
 
@AutoConfigureMockMvc
@SpringBootTest
class PegaProximoClienteDaFilaTests {
	
	@Autowired
    ClienteController clienteController;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	FilaAtendimentoRepository filaAtendimentoRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void cleanDatabase() {
    	this.clienteRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }

    @Test
    public void shouldReturnStatus200() throws Exception {
		var clienteId = new ClienteId();
		clienteId.setNome("gustavo");
		clienteId.setEmail("gustavo@teste.com");
    	var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca("2020");
		filaAtendimento.setModeloFiltroDeBusca("gol");
		filaAtendimento.setCategoriaFiltroDeBusca("suv");		
		filaAtendimento.setId(clienteId);
		filaAtendimento.setTelefone("911223344");
		filaAtendimento.setDataInsercao(LocalDateTime.now());
    	this.filaAtendimentoRepository.save(filaAtendimento);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.get("/cliente/proximo-da-fila")
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isOk()
        			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.clienteRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus200_withMoreThanOneCustomer() throws Exception {
		var clienteId = new ClienteId();
		clienteId.setNome("gustavo");
		clienteId.setEmail("gustavo@teste.com");
    	var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca("2020");
		filaAtendimento.setModeloFiltroDeBusca("gol");
		filaAtendimento.setCategoriaFiltroDeBusca("suv");		
		filaAtendimento.setId(clienteId);
		filaAtendimento.setTelefone("911223344");
		filaAtendimento.setDataInsercao(LocalDateTime.now().plusMinutes(1));
		
		var clienteId2 = new ClienteId();
		clienteId2.setNome("gustavo teste");
		clienteId2.setEmail("teste@gustavo.com");
    	var filaAtendimento2 = new FilaAtendimento();
		filaAtendimento2.setAnoFiltroDeBusca("2024");
		filaAtendimento2.setModeloFiltroDeBusca("onix");
		filaAtendimento2.setCategoriaFiltroDeBusca("sedan");		
		filaAtendimento2.setId(clienteId2);
		filaAtendimento2.setTelefone("991919191");
		filaAtendimento2.setDataInsercao(LocalDateTime.now());
		var lista = List.of(filaAtendimento, filaAtendimento2);
    	this.filaAtendimentoRepository.saveAll(lista);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.get("/cliente/proximo-da-fila")
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isOk()
        			)
        	.andReturn()
        	.equals(new ClienteNaFilaDTO(
        				clienteId2.getNome(),
        				clienteId2.getEmail(),
        				filaAtendimento2.getTelefone(),
        				filaAtendimento2.getAnoFiltroDeBusca(),
        				filaAtendimento2.getModeloFiltroDeBusca(),
        				filaAtendimento2.getCategoriaFiltroDeBusca()
        			)
        		);
        Assertions.assertEquals(2, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.clienteRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus204() throws Exception {
        this.mockMvc
    	.perform(MockMvcRequestBuilders.get("/cliente/proximo-da-fila")
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers
    			.status()
    			.isNoContent()
    			);
    Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
    Assertions.assertEquals(0, this.clienteRepository.findAll().size());
    }

}
