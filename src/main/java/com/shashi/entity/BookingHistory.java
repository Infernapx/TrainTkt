package com.shashi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "history")
public class BookingHistory {

    @Id
    @Column(name = "transid", length = 40)
    private String transId;

    @Column(name = "mailid", nullable = false, length = 60)
    private String mailId;

    @Column(name = "tr_no", nullable = false)
    private Long trNo;

    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;

    @Column(name = "from_stn", nullable = false, length = 30)
    private String fromStn;

    @Column(name = "to_stn", nullable = false, length = 30)
    private String toStn;

    @Column(name = "seats", nullable = false)
    private Integer seats;

    @Column(name = "amount", nullable = false)
    private Double amount;

    public BookingHistory() {}

    public BookingHistory(String transId, String mailId, Long trNo, LocalDate journeyDate, String fromStn, String toStn, Integer seats, Double amount) {
        this.transId = transId;
        this.mailId = mailId;
        this.trNo = trNo;
        this.journeyDate = journeyDate;
        this.fromStn = fromStn;
        this.toStn = toStn;
        this.seats = seats;
        this.amount = amount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public Long getTrNo() {
        return trNo;
    }

    public void setTrNo(Long trNo) {
        this.trNo = trNo;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
