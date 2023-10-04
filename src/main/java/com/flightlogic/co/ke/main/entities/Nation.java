/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *
 * @author jgithu
 */
@Entity
@Table(name = "Nationalities", indexes = {
    @Index(name = "PRIMARY", columnList = "Id", unique = false)})
public class Nation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int Id;

    @Column(name = "NationCode")
    private String NationCode;

    @Column(name = "NationName")
    private String NationName;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNationCode() {
        return NationCode;
    }

    public void setNationCode(String NationCode) {
        this.NationCode = NationCode;
    }

    public String getNationName() {
        return NationName;
    }

    public void setNationName(String NationName) {
        this.NationName = NationName;
    }
}
