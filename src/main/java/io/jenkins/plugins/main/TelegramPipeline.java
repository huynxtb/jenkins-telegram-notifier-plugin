package io.jenkins.plugins.main;

import hudson.Extension;
import hudson.model.TaskListener;
import io.jenkins.plugins.constants.AppConst;
import io.jenkins.plugins.dto.*;
import io.jenkins.plugins.exception.AppException;
import io.jenkins.plugins.util.StringHelper;
import io.jenkins.plugins.util.Validation;
import io.jenkins.plugins.util.WebhookCaller;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import javax.inject.Inject;
import java.util.ArrayList;

public class TelegramPipeline extends AbstractStepImpl {
    private final String accessToken;
    private final String chatId;
    private String title;
    private String jobLink;
    private String branchName;
    private String commitId;
    private String description;
    private String result;
    private String webUrl;
    private int buildNumber;
    private String timeZone;

    @DataBoundConstructor
    public TelegramPipeline(String accessToken, String chatId) {
        this.accessToken = accessToken;
        this.chatId = chatId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
    public String getChatId() {
        return this.chatId;
    }

    public String getBranchName() {
        return this.branchName;
    }

    @DataBoundSetter
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getTitle() {
        return this.title;
    }

    @DataBoundSetter
    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobLink() {
        return this.jobLink;
    }

    @DataBoundSetter
    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getDescription() {
        return this.description;
    }

    @DataBoundSetter
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommitId() {
        return this.commitId;
    }

    @DataBoundSetter
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    @DataBoundSetter
    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public String getWebUrl() {
        return this.webUrl;
    }

    @DataBoundSetter
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getBuildNumber() {
        return this.buildNumber;
    }

    @DataBoundSetter
    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    @DataBoundSetter
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public static class TelegramNotifierPipelineExecution extends AbstractSynchronousNonBlockingStepExecution<Void> {

        private static final long serialVersionUID = 1L;
        @Inject
        transient TelegramPipeline pipeline;

        @StepContextParameter
        private transient TaskListener listener;

        @Override
        protected Void run() throws AppException {
            String timeZoneId = (pipeline.getTimeZone() != null) ? pipeline.getTimeZone() : "UTC";
            listener.getLogger().println("Starting Telegram Notifier");

            if (pipeline.getAccessToken() == null)
                throw new AppException("Access Token is required. Following this way: telegramNotifier accessToken: 'ACCESS_TOKEN'");

            if (pipeline.getTitle() == null)
                throw new AppException("Title is required. Following this way: telegramNotifier title: 'YOUR_TITLE'");

            if (pipeline.getResult() == null)
                throw new AppException("Result is required. Following this way: telegramNotifier result: currentBuild.currentResult");

            if (pipeline.getJobLink() != null) {
                if (!Validation.isUrl(pipeline.getJobLink()))
                    throw new AppException("Job Link invalid.");
            }

            if (pipeline.getWebUrl() != null) {
                if (!Validation.isUrl(pipeline.getWebUrl()))
                    throw new AppException("Web Url invalid.");
            }

            BindingControlDto dto = new BindingControlDto(pipeline.getResult()
                    , pipeline.getTitle()
                    , pipeline.getDescription()
                    , timeZoneId
                    , String.valueOf(pipeline.getBuildNumber())
                    , pipeline.getCommitId()
                    , pipeline.getBranchName()
                    , pipeline.getJobLink()
                    , pipeline.getWebUrl()
            );

            try {
                WebhookCaller caller = new WebhookCaller(pipeline.getAccessToken(), pipeline.getChatId());

                TelegramApiResponseDto responseDto = caller.send(dto);

                listener.getLogger().println(AppConst.APP_NAME + " " + AppConst.VERSION + " - " + AppConst.AUTHOR);
                listener.getLogger().println("Sending notification to Telegram.");
                listener.getLogger().println("Response: " + responseDto.getResponse());
                listener.getLogger().println("Request: " + responseDto.getRequest());
            } catch (Exception e) {
                e.printStackTrace(listener.getLogger());
            }
            return null;
        }
    }

    @Extension
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(TelegramNotifierPipelineExecution.class);
        }

        @Override
        public String getFunctionName() {
            return "telegramNotifier";
        }

        @Override
        public String getDisplayName() {
            return "Send a message to Telegram";
        }
    }
}
