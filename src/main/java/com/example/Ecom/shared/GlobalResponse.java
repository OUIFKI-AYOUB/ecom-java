package com.example.Ecom.shared;


import lombok.Getter;

import java.util.List;

@Getter
public class GlobalResponse<T> {

    public static  final String Success = "Success";
    public  static  final String Error = "Error";

    private final String status;
    private final T data;
    private final List<ErrorItem> errors;


    public GlobalResponse(List<ErrorItem> errors) {
        this.status = Error;
        this.data = null;
        this.errors = errors;
    }

    public GlobalResponse(T data) {
        this.status = Success;
        this.data = data;
        this.errors = null;
    }
    public record ErrorItem(String message) {}


}
