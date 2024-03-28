package com.formssafe.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void 유효한세션이_있다면_로그아웃할수있다() throws Exception {
//        //given
//
//        //when then
//        mockMvc.perform(get("/api/v1/auth/logout")
//                        .cookie(new Cookie("SESSION_ID",
//                                "YjBmNTYxNTMtMTYxMS00ZjBiLTlkODktZWM2ZmI5MTRkOTJi")))
//                .andExpect(status().isOk());
//    }
}