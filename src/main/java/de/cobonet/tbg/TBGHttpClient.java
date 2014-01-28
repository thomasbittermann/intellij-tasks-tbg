package de.cobonet.tbg;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.log4j.Logger;

/**
 * Created by th on 28.01.14.
 */
public class TBGHttpClient extends HttpClient {

    private static final Logger LOG = Logger.getLogger("de.cobonet.tbg");

    protected String securityToken;

    static {
        Protocol easyHttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps);
    }

    public TBGHttpClient(final HttpClient httpClient, final String securityToken) {
        super(httpClient.getParams(), httpClient.getHttpConnectionManager());
        setHostConfiguration(httpClient.getHostConfiguration());
        this.securityToken = securityToken;
    }

    public final String getSecurityToken() {
        return securityToken;
    }

}
