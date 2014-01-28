package de.cobonet.tbg;

import java.util.Map;

public class TBGIssueList {

    int count;
    Map<Integer, TBGIssue> issues;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<Integer, TBGIssue> getIssues() {
        return issues;
    }

    public void setIssues(Map<Integer, TBGIssue> issues) {
        this.issues = issues;
    }

}
