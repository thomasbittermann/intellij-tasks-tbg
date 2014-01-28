package de.cobonet.tbg;

import net.minidev.json.JSONValue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Created by th on 24.01.14.
 */
public class TBGClientImpl implements TBGClient {

    private final static Logger LOG = Logger.getLogger("de.cobonet.tbg");

    private String baseUrl;
    private HttpClient httpClient;

    TBGClientImpl(@NotNull final String baseUrl, @NotNull final HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public TBGIssueList listIssues(final TBGQuery query) {

        TBGIssueList issueList = null;

        try {

            HttpGet getRequest = new HttpGet(TBGUtils.buildUrlGetIssues(baseUrl, query));

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            String jsonResponse = EntityUtils.toString(entity)
                    .replaceAll("issue_no", "issueNo")
                    .replaceAll("posted_by", "postedBy")
                    .replaceAll("assigned_to", "assignedTo")
                    .replaceAll("created_at", "createdAt")
                    .replaceAll("last_updated", "lastUpdated");

            issueList = JSONValue.parse(jsonResponse, TBGIssueList.class);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return issueList;
    }

}
