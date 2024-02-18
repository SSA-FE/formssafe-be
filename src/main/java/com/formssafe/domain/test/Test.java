package com.formssafe.domain.test;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "test")
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id
    private String id;
    private String name;
    private int age;
}
