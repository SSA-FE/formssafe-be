package com.formssafe.domain.form.dto;

import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.SortType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class FormParam {

    @Schema(description = "설문 목록 조회 시 검색 파라미터")
    public record SearchDto(@Schema(description = "검색어")
                            String keyword,
                            @Schema(description = "정렬 기준", defaultValue = "create date", allowableValues = {
                                    "createDate",
                                    "endDate", "responseCnt"})
                            String sort,
                            @Schema(description = "카테고리")
                            List<String> category,
                            @Schema(description = "설문 상태", allowableValues = {"not started", "progress", "done",
                                    "rewarded"})
                            String status,
                            @Schema(description = "태그")
                            List<String> tag,
                            @Schema(description = "마지막 formId")
                            Long top) {
        public SortType sortTypeConvertToEnum(String sortType){
            switch (sortType){
                case "createDate":
                    return SortType.CREATE_DATE;
                case "endDate":
                    return SortType.END_DATE;
                case "responseCnt":
                    return SortType.RESPONSE_CNT;
                default:
                    throw new IllegalArgumentException("sortType 입력 오류");
            }
        }
        @Override
        public String toString(){
            return "keyword : "+ keyword + ", sort : " + sort + ", category : "+category + ", status : " + status + ", tag : " + tag + ", top : "+ top;
        }
    }
}
