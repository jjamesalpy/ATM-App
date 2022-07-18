package com.jj.atm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jj.atm.app.model.DenominationModel;

public interface DenominationRepository extends JpaRepository<DenominationModel, Integer>{

}
