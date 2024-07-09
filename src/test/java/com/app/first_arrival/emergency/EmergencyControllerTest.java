package com.app.first_arrival.emergency;

import com.app.first_arrival.controller.EmergencyController;
import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.dto.EmergencyDTO;
import com.app.first_arrival.service.EmergencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmergencyControllerTest {

    @InjectMocks
    private EmergencyController emergencyController;

    @Mock
    private EmergencyService emergencyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cancelEmergency_shouldReturnOkResponse_whenEmergencyExists() {
        Long id = 1L;
        doNothing().when(emergencyService).cancelEmergencyById(id);

        ResponseEntity<Void> response = emergencyController.cancelEmergency(id);

        assertEquals(200, response.getStatusCodeValue());
        verify(emergencyService, times(1)).cancelEmergencyById(id);
    }

    @Test
    public void getEmergencyById_shouldReturnEmergency_whenEmergencyExists() {
        Long id = 1L;
        Emergency emergency = new Emergency();
        when(emergencyService.findById(id)).thenReturn(Optional.of(emergency));

        ResponseEntity<Emergency> response = emergencyController.getEmergencyById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(emergency, response.getBody());
        verify(emergencyService, times(1)).findById(id);
    }

    @Test
    public void getEmergencyById_shouldReturnNotFound_whenEmergencyDoesNotExist() {
        Long id = 1L;
        when(emergencyService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Emergency> response = emergencyController.getEmergencyById(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(emergencyService, times(1)).findById(id);
    }

    @Test
    public void getEmergenciesWhereStatusIsActive_shouldReturnActiveEmergencies() {
        List<Emergency> emergencies = new ArrayList<>();
        when(emergencyService.findAllEmergenciesWithStatusActive()).thenReturn(emergencies);

        List<Emergency> response = emergencyController.getEmergenciesWhereStatusIsActive();

        assertEquals(emergencies, response);
        verify(emergencyService, times(1)).findAllEmergenciesWithStatusActive();
    }

    @Test
    public void createEmergency_shouldReturnCreatedEmergency() {
        Emergency emergency = new Emergency();
        when(emergencyService.save(any(Emergency.class))).thenReturn(emergency);

        Emergency response = emergencyController.createEmergency(new Emergency());

        assertEquals(emergency, response);
        verify(emergencyService, times(1)).save(any(Emergency.class));
    }

    @Test
    public void updateEmergency_shouldReturnUpdatedEmergency_whenEmergencyExists() {
        EmergencyDTO emergencyDTO = new EmergencyDTO();
        when(emergencyService.update(any(EmergencyDTO.class))).thenReturn(emergencyDTO);

        ResponseEntity<EmergencyDTO> response = emergencyController.updateEmergency(new EmergencyDTO());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(emergencyDTO, response.getBody());
        verify(emergencyService, times(1)).update(any(EmergencyDTO.class));
    }

    @Test
    public void acceptEmergency_shouldReturnUpdatedEmergency_whenEmergencyExists() {
        Long id = 1L;
        Emergency emergency = new Emergency();
        when(emergencyService.acceptEmergency(id)).thenReturn(emergency);

        ResponseEntity<Emergency> response = emergencyController.acceptEmergency(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(emergency, response.getBody());
        verify(emergencyService, times(1)).acceptEmergency(id);
    }

    @Test
    public void abort_shouldReturnUpdatedEmergency_whenEmergencyExists() {
        Long id = 1L;
        Emergency emergency = new Emergency();
        when(emergencyService.abortEmergency(id)).thenReturn(emergency);

        ResponseEntity<Emergency> response = emergencyController.abort(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(emergency, response.getBody());
        verify(emergencyService, times(1)).abortEmergency(id);
    }

    @Test
    public void hasActiveEmergency_shouldReturnActiveEmergency_whenExists() {
        Emergency emergency = new Emergency();
        when(emergencyService.hasAnActiveEmergencyReported()).thenReturn(emergency);

        Emergency response = emergencyController.hasActiveEmergency();

        assertEquals(emergency, response);
        verify(emergencyService, times(1)).hasAnActiveEmergencyReported();
    }

    @Test
    public void notifyNearbyUsers_shouldReturnOkResponse() {
        doNothing().when(emergencyService).notifyNearbyUsers(any(Emergency.class));

        ResponseEntity<String> response = emergencyController.notifyNearbyUsers(new Emergency());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Notifications sent", response.getBody());
        verify(emergencyService, times(1)).notifyNearbyUsers(any(Emergency.class));
    }
}
