package com.formssafe.domain.content.service;

import com.formssafe.global.constants.ContentConstraints;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class ContentValidateService {

    private ContentValidateService() {
    }

    public void validContentTitleLength(String title) {
        if (title == null || !ContentConstraints.isValidContentTitleLength(title)) {
            throw new BadRequestException(ErrorCode.INVALID_CONTENT_TITLE_LENGTH,
                    "Invalid content title length: " + title);
        }
    }

    public void validContentDescriptionLength(String description) {
        if (description != null && !ContentConstraints.isValidContentDescriptionLength(description)) {
            throw new BadRequestException(ErrorCode.INVALID_CONTENT_DESCRIPTION_LENGTH,
                    "Invalid content description length: " + description.length());
        }
    }
}
