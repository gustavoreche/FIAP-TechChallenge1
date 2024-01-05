package com.br.fiap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.fiap.controller.VendedorController;
import com.br.fiap.model.Cliente;
import com.br.fiap.model.ClienteId;
import com.br.fiap.model.FilaAtendimento;
import com.br.fiap.model.FiltroDeBusca;
import com.br.fiap.repository.AtendimentoRepository;
import com.br.fiap.repository.ClienteRepository;
import com.br.fiap.repository.FilaAtendimentoRepository;
 
@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RegistraAtendimentoTests {
	
	@Autowired
    VendedorController vendedorController;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	AtendimentoRepository atendimentoRepository;
	
	@Autowired
	FilaAtendimentoRepository filaAtendimentoRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void cleanDatabase() {
    	this.atendimentoRepository.deleteAll();
    	this.clienteRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }
    
    @AfterAll
    void cleanDatabaseToOtherTests() {
    	this.atendimentoRepository.deleteAll();
    	this.clienteRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }


    @Test
    public void shouldReturnStatus201_site() throws Exception {
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
    	
        var request = """
        		{
        			"nome": "Anderson",
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
        			.header("localAcessado", "SITE")
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.clienteRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus201_estande() throws Exception {
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
    	
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno("2020");
		filtroDeBusca.setModelo("gol");
		filtroDeBusca.setCategoria("suv");
    	var cliente = new Cliente();
    	cliente.setId(clienteId);
    	cliente.setTelefone("911223344");
    	cliente.setFiltroDeBusca(filtroDeBusca);
    	this.clienteRepository.save(cliente);
    	
        var request = """
        		{
        			"nome": "Anderson",
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
        			.header("localAcessado", "ESTANDE")
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.clienteRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }
   

    @Test
    public void shouldReturnStatus201_withMoreThanOneCustomer_site() throws Exception {
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

		var clienteId2 = new ClienteId();
		clienteId2.setNome("gustavo teste");
		clienteId2.setEmail("teste@gustavo.com");
    	var filaAtendimento2 = new FilaAtendimento();
		filaAtendimento2.setAnoFiltroDeBusca("2024");
		filaAtendimento2.setModeloFiltroDeBusca("onix");
		filaAtendimento2.setCategoriaFiltroDeBusca("sedan");		
		filaAtendimento2.setId(clienteId2);
		filaAtendimento2.setTelefone("991919191");
		filaAtendimento2.setDataInsercao(LocalDateTime.now().plusMinutes(1));
		var lista = List.of(filaAtendimento, filaAtendimento2);
    	this.filaAtendimentoRepository.saveAll(lista);
    	
        var request = """
        		{
        			"nome": "Anderson",
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
        			.header("localAcessado", "SITE")
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.clienteRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus201_withMoreThanOneCustomer_estande() throws Exception {
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

		var clienteId2 = new ClienteId();
		clienteId2.setNome("gustavo teste");
		clienteId2.setEmail("teste@gustavo.com");
    	var filaAtendimento2 = new FilaAtendimento();
		filaAtendimento2.setAnoFiltroDeBusca("2024");
		filaAtendimento2.setModeloFiltroDeBusca("onix");
		filaAtendimento2.setCategoriaFiltroDeBusca("sedan");		
		filaAtendimento2.setId(clienteId2);
		filaAtendimento2.setTelefone("991919191");
		filaAtendimento2.setDataInsercao(LocalDateTime.now().plusMinutes(1));
		var lista = List.of(filaAtendimento, filaAtendimento2);
    	this.filaAtendimentoRepository.saveAll(lista);
    	
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno("2020");
		filtroDeBusca.setModelo("gol");
		filtroDeBusca.setCategoria("suv");
    	var cliente = new Cliente();
    	cliente.setId(clienteId);
    	cliente.setTelefone("911223344");
    	cliente.setFiltroDeBusca(filtroDeBusca);
    	this.clienteRepository.save(cliente);
    	
        var request = """
        		{
        			"nome": "Anderson",
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
        			.header("localAcessado", "ESTANDE")
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(2, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.clienteRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }

    @Test
    public void shouldReturnStatus400_withoutHeader() throws Exception {
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
    	
        var request = """
        		{
        		 	"nome": "vendedor teste",	
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
				this.mockMvc
				.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()
				);
				Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
				Assertions.assertEquals(0, this.clienteRepository.findAll().size());
				Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_withoutName(String header) throws Exception {
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
    	
        var request = """
        		{
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.assertBadRequest(request, header);
    }
    
    @ParameterizedTest
    @MethodSource("headerAndName")
    public void shouldReturnStatus400_withNamesInvalids(String header, String name) throws Exception {
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
    	
        var request = """
        		{
        		 	"nome": "%s",
        			"cliente": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s",
					    "categoria": "%s"
					  }
        		}
        		""".formatted(
        				name,
        				clienteId.getNome(),
        				filaAtendimento.getTelefone(),
        				clienteId.getEmail(),
        				filaAtendimento.getAnoFiltroDeBusca(),
        				filaAtendimento.getModeloFiltroDeBusca(),
        				filaAtendimento.getCategoriaFiltroDeBusca()
        			);
    	
        this.assertBadRequest(request, header);
    }
    
	private void assertBadRequest(String request, String header) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/vendedor/registra-atendimento")
						.header("localAcessado", header)
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()
			);
		Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.clienteRepository.findAll().size());
		Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
	}
	
	private static Stream<Arguments> headerAndName() {
	    return Stream.of(
	            Arguments.of("SITE", "aa"),
	            Arguments.of("SITE", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
	            Arguments.of("SITE", " "),
	            Arguments.of("ESTANDE", "aa"),
	            Arguments.of("ESTANDE", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
	            Arguments.of("ESTANDE", " ")
	    );
	}

}
