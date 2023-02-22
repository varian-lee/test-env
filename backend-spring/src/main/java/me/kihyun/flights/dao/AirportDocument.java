package me.kihyun.flights.dao;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document(indexName = "my-airports-1")
public class AirportDocument {
    @Id
    @Field(type = FieldType.Keyword, name = "name")
    String name;
    
    @NonNull
    @Field(type = FieldType.Text, name = "country")
    String[] country;
    
    @Field(type = FieldType.Short, name = "popularity")
    short popularity;
    
    @Field(type = FieldType.Object)
    @GeoPointField
    GeoPoint location;
    
}
