package com.murilonerdx.gerenciador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.service.UserService;
import com.murilonerdx.gerenciador.util.DozerConverter;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    static String USER_API = "/v1/api/user";

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @Test
    @DisplayName("Deve criar um usuario com sucesso.")
    public void createUserTest() throws Exception {
        UserDTO build = UserDTO.builder().id(1L).email("mu-silva@outlook.com").name("Murilo P.S").build();
        UserDTO dto =  userDTOCreate();

        BDDMockito.given(service.criarUsuario(Mockito.any(UserRequestDTO.class))).willReturn(build);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("email").value("mu-silva@outlook.com"))
                .andExpect(jsonPath("name").value("Murilo P.S"));
    }

    @Test
    @DisplayName("Deve buscar por um e-mail existente")
    public void createUserWithDuplicatedEmail() throws Exception {

        UserDTO userDTO = userDTOCreate();
        userDTO.setId(1L);
        String json = new ObjectMapper().writeValueAsString(userDTO);

        BDDMockito.given(service.procurarPorEmail(Mockito.any(String.class))).willReturn(userDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(USER_API.concat("/search-email/" + URLEncoder.encode(userDTO.getEmail(), StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deletar um usuario")
    public void deleteUserTest() throws Exception {
        BDDMockito.given(service.buscarPorId(anyLong()))
                .willReturn(UserDTO.builder().id(1L).build());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(USER_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve atualizar um usuario")
    public void updateUsuarioTest() throws Exception {
        Long id = 1L;

        UserDTO updatingUser = UserDTO.builder().id(id).name("Murilo P.S").email("mu-silva@outlook.com").build();
        UserRequestDTO userRequestDTO = DozerConverter.parseObject(updatingUser, UserRequestDTO.class);
        String json = new ObjectMapper().writeValueAsString(userRequestDTO);

        BDDMockito.given(service.buscarPorId(id)).willReturn(updatingUser);
        UserDTO updatedUser = UserDTO.builder().id(id).name("Roberto").email("mu-silva@outlook.com").build();

        BDDMockito.given(service.atualizarUsuario(id, userRequestDTO)).willReturn(updatedUser);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(USER_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    private UserDTO userDTOCreate() {
        return UserDTO.builder().email("mu-silva@outlook.com").name("Murilo P.S").build();
    }

    private User userCreate(){
        return User.builder().email("mu-silva@outlook.com").name("Murilo P.S").build();
    }

}
