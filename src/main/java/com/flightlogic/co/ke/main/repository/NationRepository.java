/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.repository;

import com.flightlogic.co.ke.main.entities.Nation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author jgithu
 */
@Repository
public interface NationRepository extends JpaRepository<Nation, String> {
     List<Nation> findAll();
}
