package com.cursotestes.libraryapi.api.resource;

import com.cursotestes.libraryapi.api.dto.BookDTO;
import com.cursotestes.libraryapi.model.entity.Book;
import com.cursotestes.libraryapi.service.BookService;
import com.cursotestes.libraryapi.service.BookServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //para rodar apenas no ambiente de teste
@WebMvcTest
@AutoConfigureMockMvc //o Spring configura um objeto para fazer as requisições
public class BookControllerTest {

    static String BOOK_API = "/api/books"; //definição da rota principal

    @Autowired //injetar dependência
    MockMvc mvc; //instância do objeto que vai mockar as requisições

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception{

        BookDTO dto = BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
        Book savedBook = Book.builder().id(101L).author("Artur").title("As aventuras").isbn("001").build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto); //método que transforma um obj de qualquer tipo em Json

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //classe para receber o verbo HTTP
                .post(BOOK_API) //verbo HTTP e a variável da rota
                .contentType(MediaType.APPLICATION_JSON) //passar o conteúdo do tipo json
                .accept(MediaType.APPLICATION_JSON) //o servidor aceita requisições do tipo json
                .content(json); //passar o corpo daq requisição variável json

        mvc
                .perform(request)// ao mandar a requisição se espera estado 201 e o obj no modelo
                .andExpect(status().isCreated())//após cliar:alt+enter, a classe MockMvcResultMatchers foi importada estaticamente
                .andExpect(jsonPath("id").value(101L))//MockMvcResultMatchers foi importada estaticamente
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
                ;
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação de um livro.")
    public void createInvalidBookTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect( jsonPath("errors", hasSize(3)));//Matchers importado estaticamente no hasSize

    }

}
