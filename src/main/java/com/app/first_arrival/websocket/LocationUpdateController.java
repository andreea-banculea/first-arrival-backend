//package com.app.first_arrival.websocket;
//
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//public class LocationUpdateController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @PostMapping("/updateLocation")
//    public ResponseEntity<Void> updateLocation(@RequestBody LocationUpdateDTO locationUpdate) {
//        messagingTemplate.convertAndSend("/topic/emergency/" + locationUpdate.getEmergencyId(), locationUpdate);
//        return ResponseEntity.ok().build();
//    }
//}
//
//
