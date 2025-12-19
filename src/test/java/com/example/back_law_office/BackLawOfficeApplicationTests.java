package com.example.back_law_office;

import com.example.back_law_office.dtos.CreateInterviewDTO;
import com.example.back_law_office.dtos.InterviewDTO;
import com.example.back_law_office.dtos.CreateClientDTO;
import com.example.back_law_office.models.Client;
import com.example.back_law_office.models.Interview;
import com.example.back_law_office.models.User;
import com.example.back_law_office.repositories.InterviewRepository;
import com.example.back_law_office.repositories.UserRepository;
import com.example.back_law_office.services.ClientService;
import com.example.back_law_office.services.CaseService;
import com.example.back_law_office.services.InterviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
class BackLawOfficeApplicationTests {

    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private CaseService caseService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private InterviewService interviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInterview_ReturnsInterviewDTO_WhenDataIsValid() {
        CreateInterviewDTO dto = new CreateInterviewDTO();
        dto.setFactualDescription("desc");
        dto.setLegalConcept("concept");
        dto.setAction("other");
        dto.setResponsibleId(1L);
        dto.setClient(new CreateClientDTO());

        User user = new User();
        user.setId(1L);
        Client client = new Client();
        Interview savedInterview = new Interview();
        InterviewDTO interviewDTO = new InterviewDTO();
		savedInterview.setAction("asesoria");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(clientService.createOrUpdateClient(any())).thenReturn(client);
        when(interviewRepository.save(any(Interview.class))).thenReturn(savedInterview);
        when(modelMapper.map(savedInterview, InterviewDTO.class)).thenReturn(interviewDTO);

        InterviewDTO result = interviewService.createInterview(dto);

        assertNotNull(result);
        verify(interviewRepository).save(any(Interview.class));
    }

    @Test
    void createInterview_ThrowsBadRequest_WhenResponsibleNotFound() {
        CreateInterviewDTO dto = new CreateInterviewDTO();
        dto.setResponsibleId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> interviewService.createInterview(dto));
    }
}