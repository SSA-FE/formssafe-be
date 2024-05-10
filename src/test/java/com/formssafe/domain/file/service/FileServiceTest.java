package com.formssafe.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.file.dto.FileResponseDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[S3 서비스 사용]")
class FileServiceTest extends IntegrationTestConfig {
    private final FileService fileService;
    private final EntityManager em;

    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    public FileServiceTest(final FileService fileService, final EntityManager em) {
        this.fileService = fileService;
        this.em = em;
    }

    @Test
    void 사용자가_파일_이름에_해당하는_저장소에서_PresignedUrl을_받는다() throws Exception {
        //given
        String prefix = "image";
        String fileName = "test-file.jpg";
        String expectedUrl = "https://example.com/test-file.txt";

        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL(expectedUrl));

        //when
        FileResponseDto fileResponseDto = fileService.createPresignedUrl(prefix, fileName);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(fileResponseDto).isNotNull();
            assertThat(fileResponseDto.path()).isEqualTo(expectedUrl);
        });
    }
}