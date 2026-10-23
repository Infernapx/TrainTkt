package com.shashi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "train")
public class Train {

    @Id
    @Column(name = "tr_no")
    private Long trNo;

    @Column(name = "tr_name", nullable = false, length = 70)
    private String trName;

    @Column(name = "from_stn", nullable = false, length = 30)
    private String fromStn;

    @Column(name = "to_stn", nullable = false, length = 30)
    private String toStn;

    @Column(name = "seats", nullable = false)
    private Integer seats;

    @Column(name = "fare", nullable = false)
    private Double fare;

    public Train() {}

    public Train(Long trNo, String trName, String fromStn, String toStn, Integer seats, Double fare) {
        this.trNo = trNo;
        this.trName = trName;
        this.fromStn = fromStn;
        this.toStn = toStn;
        this.seats = seats;
        this.fare = fare;
    }

    public Long getTrNo() {
        return trNo;
    }

    public void setTrNo(Long trNo) {
        this.trNo = trNo;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public String getFromStn() {
        return fromStn;
    }

    public void setFromStn(String fromStn) {
        this.fromStn = fromStn;
    }

    public String getToStn() {
        return toStn;
    }

    public void setToStn(String toStn) {
        this.toStn = toStn;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }
}
