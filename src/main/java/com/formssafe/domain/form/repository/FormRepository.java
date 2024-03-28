package com.formssafe.domain.form.repository;

import com.formssafe.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<Form, Long>, FormRepositoryCustom {

}
