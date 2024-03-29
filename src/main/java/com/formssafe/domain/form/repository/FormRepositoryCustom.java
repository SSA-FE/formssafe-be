package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import java.util.List;

public interface FormRepositoryCustom {

    List<Form> findFormAllFiltered(SearchDto searchDto);
}
