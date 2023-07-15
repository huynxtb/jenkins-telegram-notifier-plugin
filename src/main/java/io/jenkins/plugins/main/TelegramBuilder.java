package io.jenkins.plugins.main;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import io.jenkins.plugins.constants.AppConst;
import io.jenkins.plugins.constants.ResultConst;
import io.jenkins.plugins.dto.*;
import io.jenkins.plugins.exception.AppException;
import io.jenkins.plugins.util.StringHelper;
import io.jenkins.plugins.util.Validation;
import io.jenkins.plugins.util.WebhookCaller;
import jenkins.model.JenkinsLocationConfiguration;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import java.io.IOException;
import java.util.ArrayList;

public class TelegramBuilder extends Notifier {
    private final String accessToken;
    private final String chatId;
    private final String branchName;
    private final String title;
    private final String webUrl;
    private final String description;
    private final String timeZone;

    @DataBoundConstructor
    public TelegramBuilder(String accessToken, String chatId, String branchName, String title, String description, String webUrl, String timeZone) {
        this.accessToken = accessToken;
        this.chatId = chatId;
        this.title = title;
        this.description = description;
        this.webUrl = webUrl;
        this.branchName = branchName;
        this.timeZone = timeZone;
    }

    public String getAccessToken() {return this.accessToken;}
    public String getChatId() {return this.chatId;}

    public String getBranchName() {return this.branchName;}

    public String getTitle() {return this.title;}

    public String getWebUrl() {return this.webUrl;}

    public String getDescription() {return this.description;}

    public String getTimeZone() {return this.timeZone;}

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        final EnvVars env = build.getEnvironment(listener);
        String timeZoneId = "UTC";

        if (this.timeZone != null) {
            if(this.timeZone.length() > 0){
                timeZoneId = this.timeZone;
            }
        }

        String buildNumber = "" + env.get("BUILD_NUMBER");
        String commitId = env.get("COMMIT_ID");
        JenkinsLocationConfiguration globalConfig = JenkinsLocationConfiguration.get();
        String result = "";
        Result buildResult = build.getResult();
        String jobLink = globalConfig.getUrl() + build.getUrl();

        if (buildResult != null && !buildResult.isCompleteBuild()) return true;
        if (buildResult != null && buildResult.isBetterOrEqualTo(Result.SUCCESS)) result = ResultConst.SUCCESS;
        if (buildResult != null && buildResult.isWorseThan(Result.SUCCESS)) result = ResultConst.UNSTABLE;
        if (buildResult != null && buildResult.isWorseThan(Result.UNSTABLE)) result = ResultConst.FAILURE;

        BindingControlDto dto = new BindingControlDto(result
                , this.title
                , this.description
                , timeZoneId
                , buildNumber
                , commitId
                , this.branchName
                , jobLink
                , this.webUrl
        );

        if (globalConfig.getUrl() != null){
            if(!Validation.isUrl(jobLink))
                try {
                    throw new AppException("Job Link invalid.");
                } catch (AppException e) {
                    throw new RuntimeException(e);
                }
        }else dto.setJobLink("");

        if (this.webUrl != null){
            if(!Validation.isUrl(this.webUrl))
                try {
                    throw new AppException("Web Url invalid.");
                } catch (AppException e) {
                    throw new RuntimeException(e);
                }
            dto.setWebUrl(this.webUrl);
        }

        try {
            WebhookCaller caller = new WebhookCaller(this.accessToken, this.chatId);
            caller.send(dto);

            listener.getLogger().println(AppConst.APP_NAME + " " + AppConst.VERSION + " - " + AppConst.AUTHOR);
            listener.getLogger().println("Sending notification to Telegram.");

        } catch (Exception e) {
            e.printStackTrace(listener.getLogger());
            return false;
        }

        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public FormValidation doCheckWebUrl(@QueryParameter String value) {
            if (value != null)
                if (value.length() > 0)
                    if (!Validation.isUrl(value))
                        return FormValidation.error("Please enter a valid Web URL.");
            return FormValidation.ok();
        }

        public String getDisplayName() {
            return AppConst.APP_NAME;
        }

        public String getVersion() {
            return AppConst.VERSION;
        }
    }
}
