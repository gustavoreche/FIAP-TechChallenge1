package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.dominio.servico.InformaPropostaDTO;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.AtendimentoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_PEGA_PROPOSTA;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class PegaPropostaTests {
	
	@Autowired
    AtendimentoController atendimentoController;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	AtendimentoRepository atendimentoRepository;

	@Autowired
	LeadRepository leadRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void inicializaLimpezaDoDatabase() {
    	this.atendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
	}
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {
    	this.atendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
	}


    @Test
    public void deveRetornarStatus200() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		this.atendimentoRepository.save(atendimento);

		MvcResult response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_PEGA_PROPOSTA
								.replace("{nome}", lead.getId().getNome())
								.replace("{email}", lead.getId().getEmail())
						)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		String responseAppString = response.getResponse().getContentAsString();

		InformaPropostaDTO responseApp = this.objectMapper
				.readValue(responseAppString, InformaPropostaDTO.class);

		InformaPropostaDTO retornoEsperado = this.objectMapper
				.readValue("""
					{
					  "idAtendimento": 1,
					  "valorDaProposta": 50000.00,
					  "parcelas": [
					    {
					      "quantidade": 24,
					      "valor": 2083.34
					    },
					    {
					      "quantidade": 36,
					      "valor": 1388.89
					    }
					  ]
					}
					""", InformaPropostaDTO.class);
		Assertions.assertEquals(retornoEsperado, responseApp);
    }

	@Test
	public void deveRetornarStatus200_comMaisDeUmAtendimentoParaOMesmoLead() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		var atendimento2 = this.criaOutroAtendimento(lead);
		var lista = List.of(atendimento, atendimento2);
		this.atendimentoRepository.saveAll(lista);

		MvcResult response = this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_PEGA_PROPOSTA
								.replace("{nome}", lead.getId().getNome())
								.replace("{email}", lead.getId().getEmail())
						)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		String responseAppString = response.getResponse().getContentAsString();

		InformaPropostaDTO responseApp = this.objectMapper
				.readValue(responseAppString, InformaPropostaDTO.class);

		InformaPropostaDTO retornoEsperado = this.objectMapper
				.readValue("""
				{
				  "idAtendimento": 3,
				  "valorDaProposta": 80000.00,
				  "parcelas": [
				    {
				      "quantidade": 24,
				      "valor": 3333.34
				    },
				    {
				      "quantidade": 36,
				      "valor": 2222.23
				    }
				  ]
				}
				""", InformaPropostaDTO.class);
		Assertions.assertEquals(retornoEsperado, responseApp);
	}

	@Test
	public void deveRetornarStatus204() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get(URL_PEGA_PROPOSTA
								.replace("{nome}", lead.getId().getNome())
								.replace("{email}", lead.getId().getEmail())
						)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isNoContent()
				);
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

	private Atendimento criaOutroAtendimento(Lead lead) {
		var atendimento = new Atendimento();
		atendimento.setLead(lead);
		atendimento.setNomeVendedor("Outro Vendedor");
		atendimento.setValorDaProposta(new BigDecimal("80000.00"));
		return atendimento;
	}

}
