package com.br.fiap;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.entidade.Contrato;
import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.dominio.servico.ContratoDTO;
import com.br.fiap.camada.dominio.servico.InformaPropostaDTO;
import com.br.fiap.camada.dominio.servico.StatusPropostaEnum;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.ContratoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.ContratoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;
import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_PEGA_PROPOSTA;
import static com.br.fiap.camada.interfaceUsuario.ContratoController.URL_CONTRATO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class RegistraContratoTests {
	
	@Autowired
	ContratoController contratoController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	LeadRepository leadRepository;

	@Autowired
	AtendimentoRepository atendimentoRepository;

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
	public void deveRetornarStatus201() throws Exception {
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
				"2083.34"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_CONTRATO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);
		Assertions.assertEquals(1, this.atendimentoRepository.findAll().size());
		Assertions.assertEquals(1, this.contratoRepository.findAll().size());
		var contrato = this.contratoRepository.findAll().get(0);
		Assertions.assertEquals(24, contrato.getQuantidadeParcelas());
		Assertions.assertEquals(new BigDecimal("2083.34"), contrato.getValorPorParcela());
		Atendimento atendimentoEntidade = this.atendimentoRepository.findAll()
				.get(0);
		Assertions.assertEquals(atendimentoEntidade.getStatusProposta(), StatusPropostaEnum.ACEITADA.name());
	}

	@Test
	public void deveRetornarStatus201_comMaisDeUmAtendimentoParaOMesmoLead() throws Exception {
		var lead = this.criaLead();
		this.leadRepository.save(lead);

		var atendimento = this.criaAtendimento(lead);
		var atendimento2 = this.criaOutroAtendimento(lead);
		var lista = List.of(atendimento, atendimento2);
		this.atendimentoRepository.saveAll(lista);

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

		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_CONTRATO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers
						.status()
						.isCreated()
				);

		Assertions.assertEquals(2, this.atendimentoRepository.findAll().size());
		Assertions.assertEquals(1, this.contratoRepository.findAll().size());
		var contrato = this.contratoRepository.findAll().get(0);
		Assertions.assertEquals(24, contrato.getQuantidadeParcelas());
		Assertions.assertEquals(new BigDecimal("3333.34"), contrato.getValorPorParcela());
		Atendimento atendimentoEntidade = this.atendimentoRepository.findById(contrato.getAtendimento().getId()).get();
		Assertions.assertEquals(atendimentoEntidade.getStatusProposta(), StatusPropostaEnum.ACEITADA.name());
	}

	@Test
	public void deveRetornarStatus500_registroDeContratoSemIniciarAtendimento() throws Exception {
		var request = new ContratoDTO(
				"Gustavo",
				"gustavo@teste.com",
				"81528330021",
				"14",
				"24",
				"2200.14"
		);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);

		MvcResult response = this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_CONTRATO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				)
				.andReturn();
		String responseAppString = response.getResponse().getContentAsString();
		Assertions.assertEquals("Não pode registrar um CONTRATO sem iniciar um ATENDIMENTO", responseAppString);
		Assertions.assertEquals(0, this.contratoRepository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestComCampoNull")
	public void deveRetornarStatus400_camposNulls(String nome,
												  String email,
												  String cpf,
												  String diaDataVencimento,
												  String quantidadeDeParcelas,
												  String valorDaParcela) throws Exception {
		var request = new ContratoDTO(nome, email, cpf, diaDataVencimento, quantidadeDeParcelas, valorDaParcela);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		this.assertBadRequest(jsonRequest);
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String nome,
														  String email,
														  String cpf,
														  String diaDataVencimento,
														  String quantidadeDeParcelas,
														  String valorDaParcela) throws Exception {
		var request = new ContratoDTO(nome, email, cpf, diaDataVencimento, quantidadeDeParcelas, valorDaParcela);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		this.assertBadRequest(jsonRequest);
	}
    
	private void assertBadRequest(String request) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_CONTRATO)
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
			);
		Assertions.assertEquals(0, this.contratoRepository.findAll().size());
	}

	private static Stream<Arguments> requestComCampoNull() {
		return Stream.of(
				Arguments.of(null, "teste@teste.com", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", null, "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", null, "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", null, "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", null, "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "24", null),
				Arguments.of(null, null, null, null, null, null)
		);
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("abc", "teste@teste.com", "81528330021", "2", "24", "500.00"),
				Arguments.of("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "teste@teste.com", "81528330021", "2", "24", "500.00"),
				Arguments.of("", "teste@teste.com", "81528330021", "2", "24", "500.00"),
				Arguments.of(" ", "teste@teste.com", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "emailerrado", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef@gmail.com", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", " ", "81528330021", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "abc", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "11111111111", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "1", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", " ", "2", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", " ", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "-1", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "0", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "abc", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "32", "24", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", " ", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "abc", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "23", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "37", "500.00"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "24", ""),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "24", " "),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "24", "abc"),
				Arguments.of("Nome Teste", "teste@teste.com", "81528330021", "2", "24", "0")
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
