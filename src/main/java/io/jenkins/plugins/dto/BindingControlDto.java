package io.jenkins.plugins.dto;

import io.jenkins.plugins.constants.MessageConst;
import io.jenkins.plugins.constants.ResultConst;
import io.jenkins.plugins.enums.StatusColor;

public class BindingControlDto {
    private String status;
    private String normalStatus;
    private String color;
    private String title;
    private String description;
    private String jobLink;
    private String webUrl;
    private String timeZoneId;
    private String buildNumber;
    private String commitId;
    private String branchName;
    public BindingControlDto(String status, String title, String description, String timeZoneId, String buildNumber, String commitId, String branchName, String jobLink, String webUrl) {
        this.status = status;
        switch (status){
            case ResultConst.SUCCESS:
                this.color = StatusColor.GREEN.getColor();
                this.normalStatus = MessageConst.SUCCESS;
                break;
            case ResultConst.ABORTED:
                this.color = StatusColor.GRAY.getColor();
                this.normalStatus = MessageConst.ABORTED;
                break;
            case ResultConst.FAILURE:
                this.color = StatusColor.RED.getColor();
                this.normalStatus = MessageConst.FAILURE;
                break;
            case ResultConst.UNSTABLE:
                this.color = StatusColor.YELLOW.getColor();
                this.normalStatus = MessageConst.UNSTABLE;
                break;
            default:
                this.color = StatusColor.RED.getColor();
                this.normalStatus = MessageConst.FAILURE;
                break;
        }
        this.title = title;
        this.description = description;
        this.timeZoneId = timeZoneId;
        this.buildNumber = buildNumber;
        this.commitId = commitId;
        this.branchName = branchName;
        this.jobLink = jobLink;
        this.webUrl = webUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNormalStatus() {
        return normalStatus;
    }

    public void setNormalStatus(String normalStatus) {
        this.normalStatus = normalStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
