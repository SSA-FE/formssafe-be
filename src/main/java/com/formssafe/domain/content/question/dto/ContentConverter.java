package com.formssafe.domain.content.question.dto;

import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionOption;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DtoConvertException;
import com.formssafe.global.util.JsonConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContentConverter implements Converter<Object[], Content> {

    @Override
    public Content convert(Object[] source) {
        String type = (String) source[1];
        if (DecorationType.exists(type)) {
            return convertToDecoration(source);
        } else if (DescriptiveQuestionType.exists(type)) {
            return convertToDescriptiveQuestion(source);
        } else if (ObjectiveQuestionType.exists(type)) {
            return convertToObjectiveQuestion(source);
        } else {
            throw new DtoConvertException(ErrorCode.UNEXPECTED_CONTENT_TYPE, "올바르지 않은 Content type 입니다 : " + type);
        }
    }

    private Decoration convertToDecoration(Object[] source) {
        return Decoration.of((String) source[0],
                DecorationType.valueOf((String) source[1]),
                (String) source[3],
                (int) source[7]);
    }

    private DescriptiveQuestion convertToDescriptiveQuestion(Object[] source) {
        return DescriptiveQuestion.of((String) source[0],
                DescriptiveQuestionType.valueOf((String) source[1]),
                (String) source[2],
                (String) source[3],
                (boolean) source[5],
                (boolean) source[6],
                (int) source[7]);
    }

    private ObjectiveQuestion convertToObjectiveQuestion(Object[] source) {
        return ObjectiveQuestion.of((String) source[0],
                ObjectiveQuestionType.valueOf((String) source[1]),
                (String) source[2],
                (String) source[3],
                JsonConverter.toList((String) source[4], ObjectiveQuestionOption.class),
                (boolean) source[5],
                (boolean) source[6],
                (int) source[7]);
    }
}