import com.example.junitdemo.Controller.PhoneController;
import com.example.junitdemo.Entity.Phone;
import com.example.junitdemo.Repository.PhoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class PhoneControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneController phoneController;

    Phone p1 = new Phone(1L, "Apple", "IPhone 14", 8, 128);
    Phone p2 = new Phone(2L, "Xiomi", "Note 12 Pro", 6, 128);
    Phone p3 = new Phone(3L, "Google", "Pixel", 8, 128);


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController).build();
    }

    @Test
    public void testGetAllPhones() throws Exception {
        List<Phone> phones = new ArrayList<>(Arrays.asList(p1, p2, p3));
        Mockito.when(phoneRepository.findAll()).thenReturn(phones);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/phones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].company", is("Google")));
    }

    @Test
    public void testGetPhoneById_success() throws Exception {
        Mockito.when(phoneRepository.findById(p1.getId())).thenReturn(Optional.of(p1));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/phones/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.company", is("Apple")))
                .andExpect(jsonPath("$.model", is("IPhone 14")))
                .andExpect(jsonPath("$.ram", is(8)))
                .andExpect(jsonPath("$.storage", is(128)));
    }

    @Test
    public void testAddNewPhone() throws Exception {
        Phone phone = Phone.builder()
                .id(4L)
                .company("Samsung")
                .model("Galaxy S3")
                .ram(4)
                .storage(64)
                .build();
        Mockito.when(phoneRepository.save(phone)).thenReturn(phone);
        String content = objectWriter.writeValueAsString(phone);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/phones/newPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.company", is("Samsung")))
                .andExpect(jsonPath("$.model", is("Galaxy S3")))
                .andExpect(jsonPath("$.ram", is(4)))
                .andExpect(jsonPath("$.storage", is(64)));
    }

    @Test
    public void testUpdatePhone() throws Exception {
        Phone updatedPhone = Phone.builder()
                .id(1L)
                .company("Apple")
                .model("IPhone 14")
                .ram(6)
                .storage(128)
                .build();
        Mockito.when(phoneRepository.findById(p1.getId())).thenReturn(Optional.of(p1));
        Mockito.when(phoneRepository.save(updatedPhone)).thenReturn(updatedPhone);
        String updatedContent = objectWriter.writeValueAsString(updatedPhone);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/phones/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.ram", is(6)));
    }

    @Test
    public void testDeletePhone() throws Exception {
        Mockito.when(phoneRepository.findById(p1.getId())).thenReturn(Optional.of(p1));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/phones/deletePhone/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPhoneById_notFound() throws Exception {
        Mockito.when(phoneRepository.findById(10L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/phones/10"))
                .andExpect(status().isBadRequest());
    }

}
