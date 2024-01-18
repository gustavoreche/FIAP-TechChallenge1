package com.br.fiap;

import com.br.fiap.camada.dominio.servico.CadastroLeadDTO;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import com.br.fiap.camada.interfaceUsuario.LeadController;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static com.br.fiap.camada.interfaceUsuario.LeadController.URL_LEAD;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class CadastraLeadTests {
	
	@Autowired
    LeadController leadController;
	
	@Autowired
	LeadRepository leadRepository;
	
	@Autowired
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
    public void deveRetornarStatus201() throws Exception {
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

        this.mockMvc
        	.perform(MockMvcRequestBuilders.post(URL_LEAD)
        			.content(jsonRequest)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.leadRepository.findAll().size());
    }


	@ParameterizedTest
	@MethodSource("requestComCampoNull")
    public void deveRetornarStatus400_camposNulls(String nome,
												  String telefone,
												  String email,
												  String ano,
												  String modelo) throws Exception {
		var request = new CadastroLeadDTO(nome, telefone, email, ano, modelo);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
        this.assertBadRequest(jsonRequest);
    }

	@ParameterizedTest
	@MethodSource("requestComCampoVazio")
	public void deveRetornarStatus400_camposVazios(String nome,
												   String telefone,
												   String email,
												   String ano,
												   String modelo) throws Exception {
		var request = new CadastroLeadDTO(nome, telefone, email, ano, modelo);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		this.assertBadRequest(jsonRequest);
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String nome,
														  String telefone,
														  String email,
														  String ano,
														  String modelo) throws Exception {
		var request = new CadastroLeadDTO(nome, telefone, email, ano, modelo);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		this.assertBadRequest(jsonRequest);
	}

	private void assertBadRequest(String request) throws Exception {
		this.mockMvc
        	.perform(MockMvcRequestBuilders.post(URL_LEAD)
        			.content(request)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isBadRequest()
			);
		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	private static Stream<Arguments> requestComCampoNull() {
		return Stream.of(
				Arguments.of(null, "16991919191", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", null, "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", null, "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", null, "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "2018", null),
				Arguments.of(null, null, null, null, null)
		);
	}

	private static Stream<Arguments> requestComCampoVazio() {
		return Stream.of(
				Arguments.of("", "16991919191", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "2018", ""),
				Arguments.of("", "", "", "", "")
		);
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("abc", "16991919191", "teste@teste.com", "2018", "onix"),
				Arguments.of("aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef", "16991919191", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "12", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "161122334455", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "169919191ab", "teste@teste.com", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "emailerrado", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef@gmail.com", "2018", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "202a", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "202", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "20212", "onix"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "2018", "a"),
				Arguments.of("Nome Teste", "16991919191", "teste@teste.com", "2018", "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef")
		);
	}

}
