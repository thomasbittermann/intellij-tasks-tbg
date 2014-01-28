package de.cobonet.tbg;

/**
 * Created by th on 23.01.14.
 */
public class TBGQuery {

    private String queryString;

    public TBGQuery(final String query) {
        this.queryString = query;
    }

    public String build() {
        return queryString == null ? "" : queryString;
    }

}
