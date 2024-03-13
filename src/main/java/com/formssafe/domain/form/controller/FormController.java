package com.formssafe.domain.form.controller;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest;
import com.formssafe.domain.form.dto.FormResponse.DetailDto;
import com.formssafe.domain.form.dto.FormResponse.ListDto;
import com.formssafe.domain.form.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/forms")
@Slf4j
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    @GetMapping
    Page<ListDto> getFormList(@ModelAttribute SearchDto param) {
        return formService.getList(param);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DetailDto getForm(@PathVariable Long id) {
        return formService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void createForm(@Valid @RequestBody FormRequest.CreateDto request) {
        formService.create(request);
    }

    @PatchMapping("/{id}/close")
    @ResponseStatus(HttpStatus.OK)
    void closeForm(@PathVariable Long id) {
        formService.close(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateForm(@PathVariable Long id,
                    @Valid @RequestBody FormRequest.CreateDto request) {
        formService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteForm(@PathVariable Long id) {
        formService.delete(id);
    }
}
