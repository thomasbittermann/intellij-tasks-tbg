package de.cobonet.tbg;

import de.cobonet.ext.crypto.BCrypt;
import net.minidev.json.JSONValue;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by th on 23.01.14.
 */
final public class TBGUtils {

    private static final Logger LOG = Logger.getLogger("de.cobonet.tbg");

    final static public TBGIssueList listIssues(final String baseUrl, final TBGQuery tbgQuery, final TBGHttpClient httpClient) {
        String jsonResponse = perfomRestCall(buildUrlGetIssues(baseUrl, tbgQuery), httpClient);
        assert jsonResponse != null;
        return JSONValue.parse(TBGIssue.adjustJson(jsonResponse), TBGIssueList.class);
    }

    final static public GetMethod createGet(final String uri) {
        final GetMethod get = new GetMethod(uri);
        get.setDoAuthentication(true);
        get.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        get.addRequestHeader("accept", "application/json");
        return get;
    }

    final static public String perfomRestCall(final String uri, final TBGHttpClient httpClient) {

        String json = null;

        HttpMethodBase method = createGet(uri);
        method.addRequestHeader("Cookie", httpClient.getSecurityToken());

        try {
            int rc = httpClient.executeMethod(method);

            if (rc != HttpStatus.SC_OK) {
                throw new RuntimeException(String.format("Failed: %s [http error %d]", method.getStatusLine(), rc));
            }

            json = new String(method.getResponseBody(), "UTF-8");

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            method.releaseConnection();
        }

        return json;
    }

    final static public String buildUrlGetIssues(final String baseUrl, final TBGQuery query) {
        StringBuilder sb = new StringBuilder(trimSlash(baseUrl));
        sb.append(TBGConstants.URI_GET_ISSUES);
        if (query != null) {
            sb.append(query.build());
        }
        return sb.toString();
    }

    final static public String trimSlash(final String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }

    final static public String createPasswordHash(final String password, final String apiKey) {
        final String salt = new StringBuilder("$2a$07$").append(apiKey).append("$").toString();
        String hash = null;
        try {
            hash = new String(BCrypt.hashpw(password, salt).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        }
        return hash;
    }

    final static public String createEncodedPasswordHash(final String password, final String apiKey) {
        String hash = createPasswordHash(password, apiKey);
        try {
            hash = URLEncoder.encode(hash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e);
        }
        return hash;
    }

    final static public String createTbg3Token(final String encryptedPassword, final String username) {
        return "tbg3_password=" + encryptedPassword + "; tbg3_username=" + username + ";";
    }

}
