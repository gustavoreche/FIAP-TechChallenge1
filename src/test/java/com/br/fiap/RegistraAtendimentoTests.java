package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RegistraAtendimentoTests {
	
	@Autowired
    AtendimentoController atendimentoController;
	
	@Autowired
	AtendimentoRepository atendimentoRepository;
	
	@Autowired
	FilaAtendimentoRepository filaAtendimentoRepository;

	@Autowired
	LeadRepository leadRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void inicializaLimpezaDoDatabase() {
    	this.atendimentoRepository.deleteAll();
		this.filaAtendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
	}
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {
    	this.atendimentoRepository.deleteAll();
		this.filaAtendimentoRepository.deleteAll();
		this.leadRepository.deleteAll();
	}


    @Test
    public void deveRetornarStatus201() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
    	this.filaAtendimentoRepository.save(leadNaFilaAtendimento);
    	
        var request = """
        		{
        			"nomeVendedor": "Anderson",
        			"lead": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s"
					  }
        		}
        		""".formatted(
						leadNaFilaAtendimento.getId().getNome(),
						leadNaFilaAtendimento.getTelefone(),
						leadNaFilaAtendimento.getId().getEmail(),
						leadNaFilaAtendimento.getAnoFiltroDeBusca(),
						leadNaFilaAtendimento.getModeloFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post(URL_ATENDIMENTO)
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
			);
        Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }
   

    @Test
    public void deveRetornarStatus201_comMaisDeUmLead() throws Exception {
		var lead = this.criaLead();
		var lead2 = this.criaOutroLead();
		var listaLead = List.of(lead, lead2);
		this.leadRepository.saveAll(listaLead);


		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
		var lead2NaFilaAtendimento = this.criaOutroLeadNaFilaDeAtendimento();

		var lista = List.of(leadNaFilaAtendimento, lead2NaFilaAtendimento);
    	this.filaAtendimentoRepository.saveAll(lista);
    	
        var request = """
        		{
        			"nomeVendedor": "Anderson",
        			"lead": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s"
					  }
        		}
        		""".formatted(
						leadNaFilaAtendimento.getId().getNome(),
						leadNaFilaAtendimento.getTelefone(),
						leadNaFilaAtendimento.getId().getEmail(),
						leadNaFilaAtendimento.getAnoFiltroDeBusca(),
						leadNaFilaAtendimento.getModeloFiltroDeBusca()
        			);
    	
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post(URL_ATENDIMENTO)
        			.contentType(MediaType.APPLICATION_JSON)
        			.content(request))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
    }

	@Test
	public void deveRetornarStatus500_registroDeAtendimentoSemCapturarLead() throws Exception {
		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
		var lead2NaFilaAtendimento = this.criaOutroLeadNaFilaDeAtendimento();

		var lista = List.of(leadNaFilaAtendimento, lead2NaFilaAtendimento);
		this.filaAtendimentoRepository.saveAll(lista);

		var request = """
        		{
        			"nomeVendedor": "Anderson",
        			"lead": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s"
					  }
        		}
        		""".formatted(
				leadNaFilaAtendimento.getId().getNome(),
				leadNaFilaAtendimento.getTelefone(),
				leadNaFilaAtendimento.getId().getEmail(),
				leadNaFilaAtendimento.getAnoFiltroDeBusca(),
				leadNaFilaAtendimento.getModeloFiltroDeBusca()
		);

		MvcResult response = this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_ATENDIMENTO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				)
				.andReturn();
		String responseAppString = response.getResponse().getContentAsString();
		Assertions.assertEquals("Não pode registrar um atendimento sem capturar um LEAD", responseAppString);
		Assertions.assertEquals(2, this.filaAtendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
	}
    
	@Test
    public void deveRetornarStatus400_campoNomeVendedorNull() throws Exception {
		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
    	this.filaAtendimentoRepository.save(leadNaFilaAtendimento);
    	
        var request = """
        		{
        			"lead": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s"
					  }
        		}
        		""".formatted(
						leadNaFilaAtendimento.getId().getNome(),
						leadNaFilaAtendimento.getTelefone(),
						leadNaFilaAtendimento.getId().getEmail(),
						leadNaFilaAtendimento.getAnoFiltroDeBusca(),
						leadNaFilaAtendimento.getModeloFiltroDeBusca()
        			);
    	
        this.assertBadRequest(request);
    }

	@ParameterizedTest
	@ValueSource(strings = {
			"aa",
			"aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef",
			" ",
			""
	})
    public void deveRetornarStatus400_validacoesDoCampoNomeVendedor(String nomeVendedor) throws Exception {
		var leadNaFilaAtendimento = this.criaUmLeadNaFilaDeAtendimento();
    	this.filaAtendimentoRepository.save(leadNaFilaAtendimento);
    	
        var request = """
        		{
        		 	"nomeVendedor": "%s",
        			"lead": {
					    "nome": "%s",
					    "telefone": "%s",
					    "email": "%s",
					    "ano": "%s",
					    "modelo": "%s"
					  }
        		}
        		""".formatted(
						nomeVendedor,
						leadNaFilaAtendimento.getId().getNome(),
						leadNaFilaAtendimento.getTelefone(),
						leadNaFilaAtendimento.getId().getEmail(),
						leadNaFilaAtendimento.getAnoFiltroDeBusca(),
						leadNaFilaAtendimento.getModeloFiltroDeBusca()
        			);
    	
        this.assertBadRequest(request);
    }
    
	private void assertBadRequest(String request) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_ATENDIMENTO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
			);
		Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
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

	private Lead criaOutroLead() {
		var leadId = new LeadId();
		leadId.setNome("gustavo teste");
		leadId.setEmail("teste@gustavo.com");
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno("2024");
		filtroDeBusca.setModelo("onix");
		var lead = new Lead();
		lead.setId(leadId);
		lead.setTelefone("991919191");
		lead.setFiltroDeBusca(filtroDeBusca);
		lead.setDataInsercao(LocalDateTime.now().plusMinutes(5));
		return lead;
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
		filaAtendimento.setDataInsercao(LocalDateTime.now().plusMinutes(5));
		return filaAtendimento;
	}

}
