package me.kihyun.flights.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FlightDTO {
    private String from;
    private String to;
    private long startSeconds;
    private short quantity;
    private byte hour;
    private int price;
}
