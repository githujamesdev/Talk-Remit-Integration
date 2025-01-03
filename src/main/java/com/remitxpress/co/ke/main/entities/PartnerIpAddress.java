/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.remitxpress.co.ke.main.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jgithu
 */
//CREATE TABLE [dbo].[PartnerIpAddress](
//	[Id] [bigint] IDENTITY(1,1) NOT NULL,
//	[PartnerId] [varchar](50) NOT NULL,
//	[IpAddress] [varchar](20) NOT NULL,
//	[Status] [bit] NOT NULL,
@Entity
@Table(name = "PartnerIpAddress")
public class PartnerIpAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String PartnerId;
    private String IpAddress;
    private Integer Status;

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String PartnerId) {
        this.PartnerId = PartnerId;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String IpAddress) {
        this.IpAddress = IpAddress;
    }

    public Integer isStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    
    
}
