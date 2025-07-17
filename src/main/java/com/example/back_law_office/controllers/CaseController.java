package com.example.back_law_office.controllers;

import com.example.back_law_office.dtos.CaseDTO;
import com.example.back_law_office.dtos.CreateLegalActionDTO;
import com.example.back_law_office.dtos.LegalActionDTO;
import com.example.back_law_office.dtos.ListCasesDTO;
import com.example.back_law_office.services.CaseService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping("/add-legal-action")
    public ResponseEntity<?> addLegalActionToCase(@RequestParam(name = "caseId") Long caseId, @Valid @RequestBody CreateLegalActionDTO legalActionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        LegalActionDTO legalAction = caseService.addLegalActionToCase(caseId, legalActionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(legalAction);
    }

    @GetMapping("get-by-area/{areaId}")
    public ResponseEntity<List<ListCasesDTO>> getLegalActionsByAreaId(@PathVariable(name = "areaId") Long areaId) {
        List<ListCasesDTO> results = caseService.getCasesByArea(areaId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CaseDTO> getCaseById(@PathVariable(name = "id") Long id) {
        try{
            CaseDTO result = caseService.getCaseById(id);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
}