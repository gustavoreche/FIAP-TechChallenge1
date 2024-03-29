package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.AtendimentoController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_VALOR_PROPOSTA;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class EnviaPropostaTests {
	
	@Autowired
    AtendimentoController atendimentoController;
	
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
    public void deveRetornarStatus202() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		this.atendimentoRepository.save(atendimento);
    	
        var request = """
        		{
        			"valorDaProposta": 10000
        		}
        		""";

		List<Atendimento> atendimentos = this.atendimentoRepository.findAll();
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.put(URL_VALOR_PROPOSTA.replace("{atendimentoId}", String.valueOf(atendimentos.get(0).getId())))
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isAccepted()
			);
		atendimentos = this.atendimentoRepository.findAll();
		Assertions.assertEquals(1, atendimentos.size());
        Assertions.assertNotNull(atendimentos.get(0).getValorDaProposta());
        Assertions.assertEquals(new BigDecimal("10000.00"), atendimentos.get(0).getValorDaProposta());
    }

	@Test
	public void deveRetornarStatus500_envioDePropostaSemRegistroDeAtendimento() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var request = """
        		{
        			"valorDaProposta": 10000
        		}
        		""";

		this.mockMvc
				.perform(MockMvcRequestBuilders.put(URL_VALOR_PROPOSTA.replace("{atendimentoId}", "1"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);
		List<Atendimento> atendimentos = this.atendimentoRepository.findAll();
		Assertions.assertEquals(0, atendimentos.size());
	}
    
	@Test
    public void deveRetornarStatus400_campoValorDaPropostaNull() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		this.atendimentoRepository.save(atendimento);

		var request = """
        		{}
        		""";
    	
        this.assertBadRequest(request);
    }

	@ParameterizedTest
	@ValueSource(strings = {
			"0",
			"aaaaa",
			"",
			" "
	})
    public void deveRetornarStatus400_validacoesDoCampoValorDaProposta(String valorDaProposta) throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		this.atendimentoRepository.save(atendimento);

		var request = """
        		{
        			"valorDaProposta": %s
        		}
        		""".formatted(valorDaProposta);
    	
        this.assertBadRequest(request);
    }
    
	private void assertBadRequest(String request) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.put(URL_VALOR_PROPOSTA.replace("{atendimentoId}", "1"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
			);
		Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
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
		return atendimento;
	}

}
