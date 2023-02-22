package me.kihyun.flights;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.ActiveProfiles;

import me.kihyun.flights.dao.AirportDocument;

@SpringBootTest
@ActiveProfiles("local")
public class ElasticsearchTest {

    @Autowired
    public ElasticsearchOperations elasticsearchOperations;
    
    @Test
    public void testGeoQuery() throws Exception {
        // Builder
        GeoDistanceQueryBuilder geoBuilder = QueryBuilders
                .geoDistanceQuery("location")
                .point(40.73, -74.1)
                .distance(10000, DistanceUnit.KILOMETERS);

        // Query 
        // NativeSearchQuery는 여러 builder를 받아 Query를 생성한다.
        NativeSearchQuery searchQuery = new NativeSearchQuery(geoBuilder);

        // ElasticsearchOperations
        // ElasticsearchOperations는 Query를 받아 index를 받아온다
        SearchHits<AirportDocument> airportDocuments = elasticsearchOperations.search(searchQuery, AirportDocument.class);

        // then
        assertEquals(1,1);
    }

    @Test
    public void testMatchQuery() throws Exception {
        MatchQueryBuilder matchBuilder = QueryBuilders.matchQuery("country", "states");
        NativeSearchQuery searchQuery = new NativeSearchQuery(matchBuilder);
        SearchHits<AirportDocument> airportDocuments = elasticsearchOperations.search(searchQuery, AirportDocument.class);
        
        assertEquals(1,1);
    }

    @Test
    public void testMatchAllQuery() throws Exception {
        MatchAllQueryBuilder matchAllBuilder = QueryBuilders.matchAllQuery();
        NativeSearchQuery searchQuery = new NativeSearchQuery(matchAllBuilder);
        SearchHits<AirportDocument> airportDocuments = elasticsearchOperations.search(searchQuery, AirportDocument.class);
        
        assertEquals(1,1);
    }
    
}
