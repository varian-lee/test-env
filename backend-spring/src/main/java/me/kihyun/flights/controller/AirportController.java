package me.kihyun.flights.controller;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.kihyun.flights.dao.AirportDocument;

@RestController
public class AirportController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    
    @Autowired
    public ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/airport")
    public ResponseEntity<?> getAirports(@RequestParam(required = false) String country, @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
        logger.error("로깅 발생!");
        NativeSearchQuery searchQuery;
        if (country == null && lat == null && lon == null) {
            MatchAllQueryBuilder matchAllBuilder = QueryBuilders.matchAllQuery();
            searchQuery = new NativeSearchQuery(matchAllBuilder);
        } else {
            if (country == null && (lat == null || lon == null)) {
                return new ResponseEntity<>("Parameter 'lat & lon' both needed.", HttpStatus.BAD_REQUEST);
            } 
            
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            
            if (country != null) {
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("country", country);
                boolQueryBuilder = boolQueryBuilder.must(matchQueryBuilder);
            }
            if (lat != null && lon != null) {
                GeoDistanceQueryBuilder geoQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                                                                        .point(lat, lon)
                                                                        .distance(1000, DistanceUnit.KILOMETERS);
                boolQueryBuilder = boolQueryBuilder.must(geoQueryBuilder);
            }

            searchQuery = new NativeSearchQuery(boolQueryBuilder);
        }

        SearchHits<AirportDocument> airportDocuments = elasticsearchOperations.search(searchQuery, AirportDocument.class);
        List<AirportDocument> resultList = new ArrayList<>();
        for (SearchHit<AirportDocument> hit : airportDocuments.getSearchHits()) {
            resultList.add(hit.getContent());
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    // 업데이트, 생성, 삭제 는 나중에 하는걸로 ㅎㅎ

    // @PostMapping("/airport")
    // public ResponseEntity<?> createAirport() {
    // }

    // @PutMapping("/airport")
    // public ResponseEntity<?> updateAirport() {
    // }

    // @DeleteMapping("/airport")
    // public ResponseEntity<?> deleteAirport() {
    // }

}