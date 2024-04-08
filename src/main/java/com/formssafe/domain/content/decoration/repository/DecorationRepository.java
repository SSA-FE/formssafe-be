package com.formssafe.domain.content.decoration.repository;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.form.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DecorationRepository extends JpaRepository<Decoration, Long> {

    @Modifying
    @Query("delete from Decoration d where d.form = :form")
    void deleteAllByForm(@Param("form") Form form);
}
