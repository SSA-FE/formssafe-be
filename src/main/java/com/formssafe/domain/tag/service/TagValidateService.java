package com.formssafe.domain.tag.service;

import com.formssafe.global.constants.TagConstraints;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class TagValidateService {

    private TagValidateService() {
    }

    public void isValidTagNameLength(String tagName) {
        if (tagName == null || !TagConstraints.isValidTagNameLength(tagName)) {
            throw new BadRequestException(ErrorCode.INVALID_TAG_NAME_LENGTH, "Invalid tag name length: " + tagName);
        }
    }

    public void isValidTotalTagSize(int size) {
        if (!TagConstraints.isValidTotalTagSize(size)) {
            throw new BadRequestException(ErrorCode.INVALID_TOTAL_TAG_SIZE, "Invalid total tag size: " + size);
        }
    }
}
