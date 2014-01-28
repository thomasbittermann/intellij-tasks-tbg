package de.cobonet.tbg;

import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.junit.Test;

/**
 * Created by th on 23.01.14.
 */
public class TBGUtilsTest {

    final static String BASE_URL       = "http://demo.opensourcecms.com/buggenie/thebuggenie/test";
    final static String GET_ISSUES_URL = "http://demo.opensourcecms.com/buggenie/thebuggenie/test/list/issues/json";
    final static String USERNAME       = "administrator";
    final static String PASSWORD       = "demo123";
    final static String API_KEY        = "434f76e63ad5e2b3e64a14a7e26186e1038ed661";

    @Test
    public void testBuildUrlGetIssues() throws Exception {
        final String url = TBGUtils.buildUrlGetIssues(BASE_URL, null);
        Assert.assertEquals(GET_ISSUES_URL, url);
    }

    @Test
    public void testBuildUrlGetIssuesWithNullQuery() throws Exception {
        final String url = TBGUtils.buildUrlGetIssues(BASE_URL + "/", null);
        Assert.assertEquals(GET_ISSUES_URL, url);
    }

    @Test
    public void testBuildUrlGetIssuesWithEmptyQuery() throws Exception {
        final String url = TBGUtils.buildUrlGetIssues(BASE_URL + "/", new TBGQuery(null));
        Assert.assertEquals(GET_ISSUES_URL, url);
    }

    @Test
    public void testCreatePasswordHash() throws Exception {
        final String expected = "$2a$07$434f76e63ad5e2b3e64a1uFalLv6jhoc364BcqV59MqkX5xc7cdAW";
        final String hash = TBGUtils.createPasswordHash(PASSWORD, API_KEY);
        Assert.assertEquals(expected, hash);
    }

    @Test
    public void testCreateEncodedPasswordHash() throws Exception {
        final String expected = "%242a%2407%24434f76e63ad5e2b3e64a1uFalLv6jhoc364BcqV59MqkX5xc7cdAW";
        final String hash = TBGUtils.createEncodedPasswordHash(PASSWORD, API_KEY);
        Assert.assertEquals(expected, hash);
    }

    @Test
    public void testCreateTbg3Token() throws Exception {
        final String expected = "tbg3_password=%242a%2407%24434f76e63ad5e2b3e64a1uFalLv6jhoc364BcqV59MqkX5xc7cdAW; tbg3_username=administrator;";
        final String token = TBGUtils.createTbg3Token(TBGUtils.createEncodedPasswordHash(PASSWORD, API_KEY), USERNAME);
        Assert.assertEquals(expected, token);
    }

    @Test
    public void testListIssues() throws Exception {
        TBGHttpClient httpClient = new TBGHttpClient(new HttpClient(), TBGUtils.createTbg3Token(TBGUtils.createEncodedPasswordHash(PASSWORD, API_KEY), USERNAME));
        TBGIssueList list = TBGUtils.listIssues(BASE_URL, new TBGQuery(null), httpClient);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.getCount() > 0);
    }
}
