package de.cobonet.tbg;

import java.util.Date;

public class TBGIssue {

    Integer id;
    String title;

    String description;
    Integer state;
    String issueNo;
    String postedBy;
    String assignedTo;
    Long createdAt;
    String status;
    Long lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Date getCreatedAtAsDate() {
        return new Date(createdAt);
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public Date getLastUpdatedAsDate() {
        return new Date(lastUpdated);
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    final static public String adjustJson(final String original) {
        return original
                .replaceAll("issue_no", "issueNo")
                .replaceAll("posted_by", "postedBy")
                .replaceAll("assigned_to", "assignedTo")
                .replaceAll("created_at", "createdAt")
                .replaceAll("last_updated", "lastUpdated");
    }

    @Override
    public String toString() {
        return "TBGIssue{" +
                "issueNo='" + issueNo + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
