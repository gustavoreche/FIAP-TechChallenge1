package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.dominio.servico.InformaPropostaDTO;
import com.br.fiap.camada.dominio.servico.LeadNaFilaDTO;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.LeadController;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.br.fiap.camada.interfaceUsuario.LeadController.URL_LEAD_PROXIMO_DA_FILA;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class PegaProximoLeadDaFilaTests {
	
	@Autowired
    LeadController leadController;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	LeadRepository leadRepository;
	
	@Autowired
	FilaAtendimentoRepository filaAtendimentoRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void inicializaLimpezaDoDatabase() {
    	this.leadRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {
		leadRepository.deleteAll();
    	filaAtendimentoRepository.deleteAll();
    }

    @Test
    public void deveRetornarStatus200() throws Exception {
		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
		this.filaAtendimentoRepository.save(leadNaFilaAtendimento);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.get(URL_LEAD_PROXIMO_DA_FILA)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isOk()
			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
    }

	@Test
    public void deveRetornarStatus200_comMaisDeUmLead() throws Exception {
		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
		var lead2NaFilaAtendimento = this.criaOutroLeadNaFilaDeAtendimento();

		var lista = List.of(leadNaFilaAtendimento, lead2NaFilaAtendimento);
    	this.filaAtendimentoRepository.saveAll(lista);

		MvcResult response = this.mockMvc
        	.perform(MockMvcRequestBuilders.get(URL_LEAD_PROXIMO_DA_FILA)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isOk()
			)
        	.andReturn();
		String responseAppString = response.getResponse().getContentAsString();
		LeadNaFilaDTO responseApp = this.objectMapper
				.readValue(responseAppString, LeadNaFilaDTO.class);

		Assertions.assertEquals(new LeadNaFilaDTO(
				lead2NaFilaAtendimento.getId().getNome(),
				lead2NaFilaAtendimento.getId().getEmail(),
				lead2NaFilaAtendimento.getTelefone(),
				lead2NaFilaAtendimento.getAnoFiltroDeBusca(),
				lead2NaFilaAtendimento.getModeloFiltroDeBusca()
		), responseApp);
        Assertions.assertEquals(2, this.filaAtendimentoRepository.findAll().size());
    }
    
    @Test
    public void deveRetornarStatus204() throws Exception {
        this.mockMvc
    	.perform(MockMvcRequestBuilders.get(URL_LEAD_PROXIMO_DA_FILA)
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers
    			.status()
    			.isNoContent()
		);
    Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
    }

	private FilaAtendimento criaUmLeadNaFilaDeAtendimento() {
		var leadId = new LeadId();
		leadId.setNome("gustavo");
		leadId.setEmail("gustavo@teste.com");
		var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca("2020");
		filaAtendimento.setModeloFiltroDeBusca("gol");
		filaAtendimento.setId(leadId);
		filaAtendimento.setTelefone("911223344");
		filaAtendimento.setDataInsercao(LocalDateTime.now().plusMinutes(1));
		return filaAtendimento;
	}

	private FilaAtendimento criaOutroLeadNaFilaDeAtendimento() {
		var leadId = new LeadId();
		leadId.setNome("gustavo teste");
		leadId.setEmail("teste@gustavo.com");
		var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca("2024");
		filaAtendimento.setModeloFiltroDeBusca("onix");
		filaAtendimento.setId(leadId);
		filaAtendimento.setTelefone("991919191");
		filaAtendimento.setDataInsercao(LocalDateTime.now());
		return filaAtendimento;
	}

}
