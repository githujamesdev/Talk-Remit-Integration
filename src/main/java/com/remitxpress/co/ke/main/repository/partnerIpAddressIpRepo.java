/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.remitxpress.co.ke.main.repository;

import com.remitxpress.co.ke.main.entities.PartnerIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jgithu
 */
@Repository
public interface partnerIpAddressIpRepo extends JpaRepository<PartnerIpAddress, Long> {

    @Query("SELECT COUNT(p) FROM PartnerIpAddress p WHERE p.IpAddress = :ip AND p.PartnerId = :partnerName AND p.Status = :status")
    long countByIpAddressAndPartnerName(@Param("ip") String ip, @Param("partnerName") String partnerName, @Param("status") Integer status);

}
