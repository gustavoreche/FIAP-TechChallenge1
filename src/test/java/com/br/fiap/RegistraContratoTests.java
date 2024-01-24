package com.br.fiap;

import com.br.fiap.camada.dominio.servico.ContratoDTO;
import com.br.fiap.camada.interfaceUsuario.ContratoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
    private MockMvc mockMvc;
    
    @BeforeEach
    void inicializaLimpezaDoDatabase() {

	}
    
    @AfterAll
    void finalizaLimpezaDoDatabase() {

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
//		Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.atendimentoRepository.findAll().size());
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

}
