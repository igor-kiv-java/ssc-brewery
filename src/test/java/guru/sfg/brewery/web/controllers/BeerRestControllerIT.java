package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerUrlParam() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34")
                .param("login", "spring")
                        .param("password", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadUrlParam() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34")
                        .param("login", "spring")
                        .param("pass", "guruXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34")
                        .with(httpBasic("spring", "guru")))
                        .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
      }

    @Test
    void findBeersById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/37f4ee5c-9fd7-4d6f-9680-116b40419b34"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeersByUPC() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0083783375213"))
                .andExpect(status().isOk());
    }

}