package com.example.member.controller;

import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

    }

    @Nested()
    class RegisterTest {
        @Test
        @DisplayName("회원가입 DTO 정상 요청")
        void registerOKMember() throws Exception {
            // given
            MemberRegisterRequestDto dto = MemberRegisterRequestDto.builder()
                    .username("username_1234")
                    .password("password!1234")
                    .nickname("닉네임")
                    .build();


            // when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/members/register")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON));

            // then
//            MvcResult mvcResult = resultActions
//                    .andExpect(status().isOk())
//                    .andDo(print())
//                    .andReturn();
//
//            System.out.println(mvcResult.getResponse().getContentAsString());

        }

        @Test
        @DisplayName("회원가입 DTO username 비정상 요청")
        void registerUsernameNotOKMember() throws Exception {
            // given
            MemberRegisterRequestDto dto = MemberRegisterRequestDto.builder()
                    .username("_username")
                    .password("password!1234")
                    .nickname("닉네임")
                    .build();

            // when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/members/register")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            MvcResult mvcResult = resultActions
                    .andExpect(status().is4xxClientError())
                    .andDo(print())
                    .andReturn();

            System.out.println(mvcResult.getResponse().getContentAsString());
        }

        @Test
        @DisplayName("회원가입 DTO password 비정상 요청")
        void registerPasswordNotOKMember() throws Exception {
            // given
            MemberRegisterRequestDto dto = MemberRegisterRequestDto.builder()
                    .username("username_1234")
                    .password("pass4")
                    .nickname("닉네임")
                    .build();

            // when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/members/register")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            MvcResult mvcResult = resultActions
                    .andExpect(status().is4xxClientError())
                    .andDo(print())
                    .andReturn();

            System.out.println(mvcResult.getResponse().getContentAsString());
        }

        @Test
        @DisplayName("회원가입 DTO nickname 비정상 요청")
        void registerNicknameNotOKMember() throws Exception {
            // given
            MemberRegisterRequestDto dto = MemberRegisterRequestDto.builder()
                    .username("username_1234")
                    .password("password!1234")
                    .nickname(" ")
                    .build();

            // when
            ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post("/members/register")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON));

            // then
            MvcResult mvcResult = resultActions
                    .andExpect(status().is4xxClientError())
                    .andDo(print())
                    .andReturn();

            System.out.println(mvcResult.getResponse().getContentAsString());
        }
    }
}