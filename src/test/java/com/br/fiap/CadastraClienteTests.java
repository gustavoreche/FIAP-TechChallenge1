package com.br.fiap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.fiap.controller.ClienteController;
import com.br.fiap.controller.LocalAcessadoEnum;
import com.br.fiap.repository.ClienteRepository;
import com.br.fiap.repository.FilaAtendimentoRepository;
 
@AutoConfigureMockMvc
@SpringBootTest
class CadastraClienteTests {
	
	@Autowired
    ClienteController clienteController;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	FilaAtendimentoRepository filaAtendimentoRepository;

    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void cleanDatabase() {
    	this.clienteRepository.deleteAll();
    	this.filaAtendimentoRepository.deleteAll();
    }

    @Test
    public void shouldReturnStatus201_site() throws Exception {
        var request = """
        		{
        			"nome": "Gustavo",
        			"telefone": "16911223344",
        			"email": "gustavo@teste.com",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/cliente")
        			.header("localAcessado", LocalAcessadoEnum.SITE)
        			.content(request)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(1, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.clienteRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus201_estande() throws Exception {
        var request = """
        		{
        			"nome": "Gustavo",
        			"telefone": "16911223344",
        			"email": "gustavo@teste.com",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/cliente")
        			.header("localAcessado", LocalAcessadoEnum.ESTANDE)
        			.content(request)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isCreated()
        			);
        Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(1, this.clienteRepository.findAll().size());
    }
    
    @Test
    public void shouldReturnStatus400_withoutHeader() throws Exception {
        var request = """
        		{
        			"nome": "Gustavo",
        			"telefone": "16911223344",
        			"email": "gustavo@teste.com",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
		this.mockMvc
    	.perform(MockMvcRequestBuilders.post("/cliente")
    			.content(request)
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(MockMvcResultMatchers
    			.status()
    			.isBadRequest()
    		);
		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.clienteRepository.findAll().size());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_fieldsNull(String header) throws Exception {
        var request = """
        		{
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request, header);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_fieldsEmpty(String header) throws Exception {
        var request = """
        		{
        			"nome": "",
        			"telefone": "",
        			"email": "",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request, header);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_validationsPart1(String header) throws Exception {
    	var request = """
        		{
        			"nome": "aaa",
        			"telefone": "aaa",
        			"email": "aaa",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request, header);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_validationsPart2(String header) throws Exception {
    	var request = """
        		{
        			"nome": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        			"telefone": "1233333333333333333333333333333333333333333333333333333333333333333333333333333333",
        			"email": "gustavo@teste.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request, header);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"SITE", "ESTANDE"})
    public void shouldReturnStatus400_validationsPart3(String header) throws Exception {
    	var request = """
        		{
        			"nome": "aaa",
        			"telefone": "123",
        			"email": "gustavo@teste.com",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request, header);
    }

	private void assertBadRequest(String request, String header) throws Exception {
		this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/cliente")
        			.header("localAcessado", header)
        			.content(request)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isBadRequest()
        		);
		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
        Assertions.assertEquals(0, this.clienteRepository.findAll().size());
	}

}
