package com.example.back_law_office.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.back_law_office.dtos.MessageDTO;
import com.example.back_law_office.models.Procedure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.back_law_office.config.RabbitMQConfig;
import com.example.back_law_office.models.Area;
import com.example.back_law_office.models.LegalAction;
import com.example.back_law_office.models.User;
import com.example.back_law_office.repositories.AreaRepository;
import com.example.back_law_office.repositories.ProcedureRepository;
import com.example.back_law_office.repositories.UserRepository;




@Service
public class NotificationProducer {
    
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ProcedureRepository procedureRepository;


    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(MessageDTO message) throws JsonProcessingException {
        String jsonMessage = new ObjectMapper().writeValueAsString(message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, jsonMessage);
    }

    public void sendLegalActionNotification(LegalAction legalAction) throws JsonProcessingException {

        Procedure procedure = procedureRepository.findById(legalAction.getProcedure().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Procedure not found"));
        Area area = areaRepository.findById(procedure.getArea().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Area not found"));
        System.out.println("Area: " + area);
        User user = userRepository.findById(area.getAssistant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));    
        String email = user.getEmail();
        
        MessageDTO message = new MessageDTO();
        message.setTo(email);
        message.setSubject("Se ha creado una nueva acción legal");
        message.setBody("Se creo una nueva accion legal con id: " + legalAction.getId() + " y procedimiento: " + legalAction.getProcedure().getName());
        sendNotification(message);
        
    }

    
}
