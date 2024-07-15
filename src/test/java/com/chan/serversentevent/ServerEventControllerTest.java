package com.chan.serversentevent;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ServerEventController.class)
class ServerEventControllerTest {

  @MockBean
  private ServerEventService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;


  @BeforeEach
  void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(provider))
        .build();
  }

  @Test
  void test_get_message_hello() throws Exception {

    //given
    RequestBuilder request = MockMvcRequestBuilders.get("/hello")
        .accept(MediaType.APPLICATION_JSON);
    //when then
    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(content().json(
            mapper.writeValueAsString(new ServerEventController.Event("Hello World!"))))
        .andDo(document(
            "{ClassName}/{methodName}",
            preprocessRequest(
                modifyHeaders()
                    .remove("Content-Length")
                    .remove("Host"),
                prettyPrint()),
            preprocessResponse(
                modifyHeaders()
                    .remove("Content-Length")
                    .remove("Expires")
                    .remove("Cache-Control"),
                prettyPrint()),

            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지")
            )
        ))
        .andReturn();


  }
}