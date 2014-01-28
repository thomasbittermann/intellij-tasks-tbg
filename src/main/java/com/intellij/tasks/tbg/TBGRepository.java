/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.tasks.tbg;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.tasks.Comment;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.TaskType;
import com.intellij.tasks.impl.BaseRepository;
import com.intellij.tasks.impl.BaseRepositoryImpl;
import com.intellij.util.xmlb.annotations.Tag;
import de.cobonet.tbg.*;
import icons.TBGIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Thomas Bittermann <thomas@cobonet.de>
 */
@Tag("TBG")
public class TBGRepository extends BaseRepositoryImpl {

    private static final Logger LOG = Logger.getInstance(TBGRepository.class);

    private String apiKey;

    /**
     * for serialization
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public TBGRepository() {
    }

    public TBGRepository(final TBGRepositoryType type) {
        super(type);
    }

    @Override
    public BaseRepository clone() {
        return new TBGRepository(this);
    }

    private TBGRepository(TBGRepository other) {
        super(other);
        setUsername(other.myUsername);
        setPassword(other.myPassword);
    }

    @Override
    protected TBGHttpClient getHttpClient() {
        return new TBGHttpClient(getUrl(), TBGUtils.createTbg3Token(getEncodedPassword(), getUsername()), super.getHttpClient());
    }

    @Override
    public Task[] getIssues(@Nullable String query, int max, long since) throws Exception {

        final TBGQuery tbgQuery = new TBGQuery(query);
        TBGIssueList taskList = TBGUtils.listIssues(getUrl(), tbgQuery, getHttpClient());

        if (taskList == null || taskList.getCount() == 0) {
            return null;
        }

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (TBGIssue tbgIssue : taskList.getIssues().values()) {
            tasks.add(createTask(tbgIssue));
        }

        return tasks.toArray(new Task[tasks.size()]);
    }

    @Nullable
    private Task createTask(final TBGIssue tbgIssue) {

        return new Task() {

            @Override
            public boolean isIssue() {
                return tbgIssue.getIssueNo().startsWith("Bug");
            }

            @Nullable
            @Override
            public String getIssueUrl() {
                StringBuilder sb = new StringBuilder(getUrl());
                sb.append("/issues/").append(tbgIssue.getIssueNo());
                return sb.toString();
            }

            @NotNull
            @Override
            public String getId() {
                return tbgIssue.getIssueNo();
            }

            @NotNull
            @Override
            public String getSummary() {
                return tbgIssue.getTitle();
            }

            public String getDescription() {
                return null;
            }

            @NotNull
            @Override
            public Comment[] getComments() {
                return new Comment[0];
            }

            @NotNull
            @Override
            public Icon getIcon() {
                return TBGIcons.TBGIcon16;
            }

            @NotNull
            @Override
            public TaskType getType() {
                TaskType type = TaskType.OTHER;
                if (tbgIssue.getIssueNo().startsWith("Bug")) {
                    type = TaskType.BUG;
                } else if (tbgIssue.getIssueNo().startsWith("Enhancement")) {
                    type = TaskType.FEATURE;
                }
                return type;
            }

            @Nullable
            @Override
            public Date getUpdated() {
                return tbgIssue.getLastUpdatedAsDate();
            }

            @Nullable
            @Override
            public Date getCreated() {
                return tbgIssue.getCreatedAtAsDate();
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public TaskRepository getRepository() {
                return TBGRepository.this;
            }

            @Override
            public String getPresentableName() {
                return getId() + ": " + getSummary();
            }

        };
    }

    @Nullable
    @Override
    public Task findTask(String id) throws Exception {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof TBGRepository)) return false;

        TBGRepository that = (TBGRepository) o;

        if (this.isConfigured() && that.isConfigured()) {
            if (this.getUrl().equals(that.getUrl())
                    && this.getUsername().equals(that.getUsername())
                    && this.getPassword().equals(that.getPassword())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getEncodedPassword() {
        return TBGUtils.createEncodedPasswordHash(getPassword(), getApiKey());
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /*
    @Override
    protected void configureHttpClient(HttpClient httpClient) {
        // Apache Commons HttpClient 3.x
        static {
            Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory)new EasySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyhttps);
        }

        // Apache HttpClient 4.x
        try {
            URIBuilder uriBuilder = new URIBuilder(getUrl());
            if ("https".equals(uriBuilder.getScheme())) {
                SSLSocketFactory sslSocketFactory = new SSLSocketFactory(
                        new TrustStrategy() {
                            @Override
                            public boolean isTrusted(final X509Certificate[] chain, String authType) throws CertificateException { return true; }
                        }, new AllowAllHostnameVerifier());
                int port = uriBuilder.getPort();
                port = port == -1 ? 443 : port;
                httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", port, sslSocketFactory));
            }
        } catch (Exception e) {
            LOG.error(e);
        }

        // Apache HttpClient >= 4.3
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        //httpClient.setCookieStore(cookieStore);
        //httpClient.setCredentialsProvider(credentialsProvider);
    }
    */

}
