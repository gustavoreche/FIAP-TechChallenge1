package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.servico.CadastroLeadDTO;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.LeadController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.br.fiap.camada.interfaceUsuario.LeadController.URL_LEAD;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FalhaParaCadastrarLeadTests {
	
	@Autowired
    LeadController leadController;
	
	@Autowired
	LeadRepository leadRepository;

	@Autowired
	@MockBean
	FilaAtendimentoRepository filaAtendimentoRepository;

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
    	this.leadRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {
    	this.leadRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }

	@Test
	public void deveRetornarStatus500_erroSalvarNaFilaDeAtendimento() throws Exception {
		var request = new CadastroLeadDTO(
				"Gustavo",
				"16911223344",
				"gustavo@teste.com",
				"2020",
				"Onix"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		Mockito
				.when(this.filaAtendimentoRepository.save(ArgumentMatchers.any(FilaAtendimento.class)))
				.thenThrow(RuntimeException.class);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_LEAD)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);

		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

}
