package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.AtendimentoController;
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

import java.time.LocalDateTime;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FalhaParaRegistrarAtendimentoTests {

	@Autowired
	AtendimentoController vendedorController;

	@Autowired
	@MockBean
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
	public void deveRetornarStatus500_erroSalvarOAtendimento() throws Exception {
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

		Mockito
				.when(this.atendimentoRepository.save(ArgumentMatchers.any(Atendimento.class)))
				.thenThrow(RuntimeException.class);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_ATENDIMENTO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				);
		Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
		Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
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

}
