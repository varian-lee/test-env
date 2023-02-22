package me.kihyun.flights.dao;

import lombok.Getter;
import me.kihyun.flights.dto.FlightDTO;

@Getter
public class FlightDAO {
    private String fromTo;
    private long startSeconds;
    private String qtyHourPrice;

    public void setFromTo(String from, String to) {
        this.fromTo = from + ":" + to;
    }

    public void setStartSeconds(long startSeconds) {
        this.startSeconds = startSeconds;
    }

    public void setQtyHourPrice(short quantity, byte hour, int price) {
        this.qtyHourPrice = String.join(" ", String.valueOf(quantity), String.valueOf(hour), String.valueOf(price));
    }

    public FlightDAO(FlightDTO dto) {
        this.fromTo = dto.getFrom() + ":" + dto.getTo();
        this.startSeconds = dto.getStartSeconds();
        this.qtyHourPrice = String.join(" ", String.valueOf(dto.getQuantity()), String.valueOf(dto.getHour()), String.valueOf(dto.getPrice()));
    }

    public FlightDTO convertToDTO(){
        FlightDTO dto = new FlightDTO();

        String from = this.fromTo.split(":")[0];
        String to = this.fromTo.split(":")[1];
        short quantity = Short.valueOf(this.qtyHourPrice.split(" ")[0]);
        byte hour = Byte.valueOf(this.qtyHourPrice.split(" ")[1]);
        int price = Integer.valueOf(this.qtyHourPrice.split(" ")[2]);

        dto.setFrom(from);
        dto.setTo(to);
        dto.setStartSeconds(this.startSeconds);
        dto.setQuantity(quantity);
        dto.setHour(hour);
        dto.setPrice(price);

        return dto;
    }
}
