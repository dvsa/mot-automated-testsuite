package uk.gov.dvsa.mot.framework.elasticsearch;

import java.util.ArrayList;

/**
 * Elastic Search Result Class.
 */
public class ElasticSearchResult {
    /** The search term used in the search. */
    String searchTerm;

    /** The total count of results returned. */
    int totalCount;

    /** The List of the top ten results returned from the query. */
    ArrayList<String> topTenList;

    /**
     * Constructor for ElasticSearrchResult.
     * @param searchTerm the search term used.
     * @param totalCount the total count of results.
     * @param topTenList the list of the top ten results.
     */
    public ElasticSearchResult(String searchTerm, int totalCount, ArrayList<String> topTenList) {
        this.searchTerm = searchTerm;
        this.totalCount = totalCount;
        this.topTenList = topTenList;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<String> getTopTenList() {
        return topTenList;
    }

    public void setTopTenList(ArrayList<String> topTenList) {
        this.topTenList = topTenList;
    }

    @Override
    public String toString() {
        return "ElasticSearchResult{"
                + "searchTerm='"
                + searchTerm
                + '\''
                + ", totalCount="
                + totalCount
                + ", topTenList="
                + topTenList
                + '}';
    }
}
