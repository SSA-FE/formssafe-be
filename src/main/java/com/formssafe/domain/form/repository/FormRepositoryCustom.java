package com.formssafe.domain.form.repository;

import com.formssafe.domain.activity.dto.ActivityParam;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import java.util.List;

public interface FormRepositoryCustom {
    List<Form> findFormWithFiltered(SearchDto searchDto);

    List<Form> findFormByUserWithFiltered(ActivityParam.SearchDto searchDto, User author);
}
