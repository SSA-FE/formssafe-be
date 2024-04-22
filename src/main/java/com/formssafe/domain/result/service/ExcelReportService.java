package com.formssafe.domain.result.service;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.entity.Question;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormCommonService;
import com.formssafe.domain.form.service.FormValidateService;
import com.formssafe.domain.result.dto.ResultExportResponse.ResultExportRow;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.entity.SubmissionResponse;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.error.type.FormssafeException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExcelReportService {
    private final FormCommonService formCommonService;
    private final FormValidateService formValidateService;
    private final ResultExcelExportService resultExcelExportService;
    private final SubmissionRepository submissionRepository;

    public void execute(HttpServletResponse response, Long formId, LoginUserDto loginUser) {
        Form form = formCommonService.findForm(formId);
        formValidateService.validAuthor(form, loginUser.id());
        formValidateService.validNotTempForm(form);

        List<String> headers = getHeaders(form);
        List<ResultExportRow> body = getBody(form);
        String fileName = "form_result";
        export(response, fileName, headers, body);
    }

    private List<String> getHeaders(Form form) {
        List<String> questions = Stream.concat(form.getDescriptiveQuestionList().stream(),
                        form.getObjectiveQuestionList().stream())
                .sorted(Comparator.comparingInt(Content::getPosition))
                .map(Question::getTitle)
                .toList();

        List<String> headers = new ArrayList<>();

        headers.add("응답 시각");
        headers.add("응답자");
        headers.addAll(questions);

        return headers;
    }

    private List<ResultExportRow> getBody(Form form) {
        List<Submission> submissionList = submissionRepository.findAllByFormIdWithAll(form.getId());
        return submissionList.stream().map(this::createResultExportResponse).toList();
    }

    private ResultExportRow createResultExportResponse(Submission s) {
        List<SubmissionResponse> list = new ArrayList<>(s.getObjectiveSubmissionList());
        list.addAll(s.getDescriptiveSubmissionList());
        list.sort(Comparator.comparingInt(SubmissionResponse::getPosition));
        return convertResponse(s, list);
    }

    private ResultExportRow convertResponse(Submission submission, List<SubmissionResponse> submissionResponses) {
        List<String> responses = submissionResponses.stream().map(this::getResponseString).toList();

        return new ResultExportRow(submission.getSubmitTime(), submission.getUser().getNickname(), responses);
    }

    private String getResponseString(SubmissionResponse submissionResponse) {
        if (submissionResponse instanceof ObjectiveSubmission os) {
            return os.getSelection();
        } else if (submissionResponse instanceof DescriptiveSubmission ds) {
            return ds.getContent();
        } else {
            throw new IllegalStateException("Unsupported type: " + submissionResponse.getClass().getName());
        }
    }

    private void export(HttpServletResponse response, String fileName, List<String> headers,
                        List<ResultExportRow> body) {
        initResponse(response, fileName);

        try (XSSFWorkbook sheets = resultExcelExportService.createSheets(); OutputStream outputStream = response.getOutputStream();) {
            resultExcelExportService.exportToExcel(sheets, "응답", headers, body);
            sheets.write(outputStream);
        } catch (IOException e) {
            throw new FormssafeException("엑셀 파일 생성 중 에러가 발생했습니다.", e);
        }
    }

    private void initResponse(HttpServletResponse response, String fileName) {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }
}
