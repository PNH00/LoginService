package com.spring.LoginService.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse<T> {

    private Date timestamp;
    private String success;
    private int code;
    private String status;
    private String message;
    private T result;
}
