package com.shashi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "mailid", length = 60)
    private String mailId;

    @Column(name = "pword", nullable = false)
    private String pword;

    @Column(name = "fname", nullable = false, length = 30)
    private String fname;

    @Column(name = "lname", length = 30)
    private String lname;

    @Column(name = "addr", length = 150)
    private String addr;

    @Column(name = "phno", nullable = false)
    private Long phno;

    public Admin() {}

    public Admin(String mailId, String pword, String fname, String lname, String addr, Long phno) {
        this.mailId = mailId;
        this.pword = pword;
        this.fname = fname;
        this.lname = lname;
        this.addr = addr;
        this.phno = phno;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Long getPhno() {
        return phno;
    }

    public void setPhno(Long phno) {
        this.phno = phno;
    }
}
