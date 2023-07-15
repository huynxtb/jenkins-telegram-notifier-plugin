package io.jenkins.plugins.util;

import io.jenkins.plugins.dto.BindingControlDto;
import io.jenkins.plugins.dto.TelegramApiResponseDto;
import io.jenkins.plugins.exception.AppException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WebhookCaller {
    private final String accessToken;
    private final String chatId;
    public WebhookCaller(String accessToken, String chatId) {
        this.accessToken = accessToken;
        this.chatId = chatId;
    }

    public TelegramApiResponseDto send(BindingControlDto dto) throws AppException {
        try {
            String htmlMsg = "<strong>"+dto.getTitle()+"</strong>\n" +
                    "Build Result: " + dto.getNormalStatus() + "\n" +
                    "Build At: " + StringHelper.toDateTimeNow(dto.getTimeZoneId()) + "\n" +
                    "Build Number: #" + dto.getBuildNumber() + "\n" +
                    "Branch: " + dto.getBranchName() + "\n" +
                    "Commit ID: " + dto.getCommitId() + "\n" +
                    "Job Link: <a href=\""+dto.getJobLink()+"\">"+dto.getJobLink()+"</a>\n" +
                    "Site Link: <a href=\""+dto.getWebUrl()+"\">"+dto.getWebUrl()+"</a>\n" +
                    "Description: "+dto.getDescription();
            String telegramApi = "https://api.telegram.org/bot" + this.accessToken + "/sendMessage";
            String parameters = "?text=" + URLEncoder.encode(htmlMsg, StandardCharsets.UTF_8.toString()) + "&parse_mode=HTML" + "&chat_id=" + this.chatId;
            String finalRequest = telegramApi + parameters;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(finalRequest);
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode()!=200)
                return new TelegramApiResponseDto(response.getStatusLine().getStatusCode() + "", finalRequest);

            return new TelegramApiResponseDto(response.getStatusLine().getStatusCode() + "", finalRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return new TelegramApiResponseDto("Unknown Error", e.getMessage());
        }
    }
}
