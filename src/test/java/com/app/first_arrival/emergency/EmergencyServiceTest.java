package com.app.first_arrival.emergency;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.enums.EmergencyStatus;
import com.app.first_arrival.entities.users.User;
import com.app.first_arrival.repository.EmergencyRepository;
import com.app.first_arrival.repository.UserRepository;
import com.app.first_arrival.service.EmergencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EmergencyServiceTest {

    @InjectMocks
    private EmergencyService emergencyService;

    @Mock
    private EmergencyRepository emergencyRepository;

    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void abortEmergency_shouldReturnAbortedEmergency_whenEmergencyExists() {
        Long id = 1L;
        Emergency emergency = new Emergency();
        emergency.setVolunteersAccepted(new ArrayList<>());
        User user = new User();
        user.setEmail("test@example.com");

        when(authentication.getPrincipal()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(emergencyRepository.findById(id)).thenReturn(Optional.of(emergency));
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(emergency);

        Emergency result = emergencyService.abortEmergency(id);

        assertEquals(emergency, result);
        verify(emergencyRepository, times(1)).save(any(Emergency.class));
    }

    @Test
    public void saveEmergency_shouldReturnSavedEmergency() {
        Emergency emergency = new Emergency();
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(emergency);

        Emergency result = emergencyService.save(new Emergency());

        assertEquals(emergency, result);
        verify(emergencyRepository, times(1)).save(any(Emergency.class));
    }

    @Test
    public void cancelEmergencyById_shouldChangeStatusToCancelled_whenEmergencyExists() {
        Long id = 1L;
        Emergency emergency = new Emergency();
        when(emergencyRepository.findById(id)).thenReturn(Optional.of(emergency));

        emergencyService.cancelEmergencyById(id);

        assertEquals(EmergencyStatus.CANCELLED, emergency.getStatus());
        verify(emergencyRepository, times(1)).save(emergency);
    }

    @Test
    public void cancelEmergencyById_shouldThrowEntityNotFoundException_whenEmergencyDoesNotExist() {
        Long id = 1L;
        when(emergencyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> emergencyService.cancelEmergencyById(id));
        verify(emergencyRepository, times(1)).findById(id);
    }

    @Test
    public void findAll_shouldReturnAllEmergencies() {
        List<Emergency> emergencies = new ArrayList<>();
        when(emergencyRepository.findAll()).thenReturn(emergencies);

        List<Emergency> result = emergencyService.findAll();

        assertEquals(emergencies, result);
        verify(emergencyRepository, times(1)).findAll();
    }

    @Test
    public void findAllEmergenciesWithStatusActive_shouldReturnActiveEmergencies() {
        List<Emergency> emergencies = new ArrayList<>();
        when(emergencyRepository.findAllByStatusIn(anyList())).thenReturn(emergencies);

        List<Emergency> result = emergencyService.findAllEmergenciesWithStatusActive();

        assertEquals(emergencies, result);
        verify(emergencyRepository, times(1)).findAllByStatusIn(anyList());
    }
}
