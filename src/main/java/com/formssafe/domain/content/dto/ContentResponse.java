package com.formssafe.domain.content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.formssafe.domain.content.decoration.dto.DecorationResponse;
import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.dto.QuestionResponse;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.Question;
import com.formssafe.global.exception.type.DtoConvertException;
import com.formssafe.global.util.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public final class ContentResponse {
}
