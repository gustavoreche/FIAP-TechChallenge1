package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.entidade.Contrato;
import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.dominio.servico.ContratoDTO;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.ContratoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.AtendimentoController;
import com.br.fiap.camada.interfaceUsuario.ContratoController;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;
import static com.br.fiap.camada.interfaceUsuario.ContratoController.URL_CONTRATO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FalhaParaRegistrarContratoTests {

	@Autowired
	ContratoController contratoController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	AtendimentoRepository atendimentoRepository;

	@Autowired
	@MockBean
	private ContratoRepository contratoRepository;

	@Autowired
	LeadRepository leadRepository;

	@Autowired
	private MockMvc mockMvc;

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
		this.contratoRepository.deleteAll();
		this.atendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
    }
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {
		this.contratoRepository.deleteAll();
		this.atendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
    }

	@Test
	public void deveRetornarStatus500_erroSalvarOContrato() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		this.atendimentoRepository.save(atendimento);

		var request = new ContratoDTO(
				"gustavo",
				"gustavo@teste.com",
				"81528330021",
				"14",
				"24",
				"3333.34"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		Mockito
				.when(this.contratoRepository.save(ArgumentMatchers.any(Contrato.class)))
				.thenThrow(RuntimeException.class);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_CONTRATO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);
		Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.contratoRepository.findAll().size());

		Atendimento atendimentoEntidade = this.atendimentoRepository.findAll()
				.get(0);
		Assertions.assertNull(atendimentoEntidade.getStatusProposta());
	}

	private Lead criaLead() {
		var leadId = new LeadId();
		leadId.setNome("gustavo");
		leadId.setEmail("gustavo@teste.com");
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno("2020");
		filtroDeBusca.setModelo("gol");
		var lead = new Lead();
		lead.setId(leadId);
		lead.setTelefone("911223344");
		lead.setFiltroDeBusca(filtroDeBusca);
		lead.setDataInsercao(LocalDateTime.now().plusMinutes(1));
		return lead;
	}

	private Atendimento criaAtendimento(Lead lead) {
		var atendimento = new Atendimento();
		atendimento.setLead(lead);
		atendimento.setNomeVendedor("Gustavo Vendedor");
		atendimento.setValorDaProposta(new BigDecimal("50000.00"));
		return atendimento;
	}

}
