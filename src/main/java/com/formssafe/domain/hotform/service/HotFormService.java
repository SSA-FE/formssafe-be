package com.formssafe.domain.hotform.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.hotform.entity.HotForm;
import com.formssafe.domain.hotform.repository.HotFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotFormService {
    private final HotFormRepository hotFormRepository;

    @Transactional
    public void saveHotForm(List<Form> forms, LocalDateTime now) {
        for (Form form : forms) {
            hotFormRepository.save(HotForm.builder()
                    .form(form)
                    .saveTime(now)
                    .build());
        }
    }

    public List<Form> getHotForms() {
        return hotFormRepository.getTop10HotForms(LocalDateTime.now()).stream()
                .map(HotForm::getForm)
                .toList();
    }
}
