package io.jenkins.plugins.dto;

public class TelegramApiResponseDto {
    private String response;
    private String request;

    public TelegramApiResponseDto(String response, String request) {
        this.response = response;
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
