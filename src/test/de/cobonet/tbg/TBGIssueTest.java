package de.cobonet.tbg;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by th on 27.01.14.
 */
public class TBGIssueTest {

    @Test
    public void testAdjustJsonIssueNo() throws Exception {
        final String expected = "issueNo";
        Assert.assertEquals(expected, TBGIssue.adjustJson("issue_no"));
    }

    @Test
    public void testAdjustJsonPostedBy() throws Exception {
        final String expected = "postedBy";
        Assert.assertEquals(expected, TBGIssue.adjustJson("posted_by"));
    }

    @Test
    public void testAdjustAssignedTo() throws Exception {
        final String expected = "assignedTo";
        Assert.assertEquals(expected, TBGIssue.adjustJson("assigned_to"));
    }

    @Test
    public void testAdjustJsonCreatedAt() throws Exception {
        final String expected = "createdAt";
        Assert.assertEquals(expected, TBGIssue.adjustJson("created_at"));
    }

    @Test
    public void testAdjustJsonLastUpdated() throws Exception {
        final String expected = "lastUpdated";
        Assert.assertEquals(expected, TBGIssue.adjustJson("last_updated"));
    }

}
