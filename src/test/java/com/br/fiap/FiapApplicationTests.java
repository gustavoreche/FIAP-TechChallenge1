package com.br.fiap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.br.fiap.controller.ClienteController;
import com.br.fiap.controller.LocalAcessadoEnum;
 
@WebMvcTest
@AutoConfigureMockMvc
class FiapApplicationTests {
	
	@Autowired
    ClienteController clienteController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatus201() throws Exception {
        String request = """
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
    }
    
    @Test
    public void shouldReturnStatus400_withoutHeader() throws Exception {
        String request = """
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
    }
    
    @Test
    public void shouldReturnStatus400_fieldsNull() throws Exception {
        String request = """
        		{
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request);
    }
    
    @Test
    public void shouldReturnStatus400_fieldsEmpty() throws Exception {
        String request = """
        		{
        			"nome": "",
        			"telefone": "",
        			"email": "",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request);
    }
    
    @Test
    public void shouldReturnStatus400_validationsPart1() throws Exception {
        String request = """
        		{
        			"nome": "aaa",
        			"telefone": "aaa",
        			"email": "aaa",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request);
    }
    
    @Test
    public void shouldReturnStatus400_validationsPart2() throws Exception {
        String request = """
        		{
        			"nome": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        			"telefone": "1233333333333333333333333333333333333333333333333333333333333333333333333333333333",
        			"email": "gustavo@teste.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request);
    }
    
    @Test
    public void shouldReturnStatus400_validationsPart3() throws Exception {
        String request = """
        		{
        			"nome": "aaa",
        			"telefone": "123",
        			"email": "gustavo@teste.com",
        			"ano": "2020",
        			"modelo": "Onix",
        			"categoria": "Sedan"
        		}
        		""";
        this.assertBadRequest(request);
    }

	private void assertBadRequest(String request) throws Exception {
		this.mockMvc
        	.perform(MockMvcRequestBuilders.post("/cliente")
        			.header("localAcessado", LocalAcessadoEnum.SITE)
        			.content(request)
        			.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(MockMvcResultMatchers
        			.status()
        			.isBadRequest()
        		);
	}

}
