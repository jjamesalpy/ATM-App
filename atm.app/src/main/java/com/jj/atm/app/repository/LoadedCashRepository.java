package com.jj.atm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jj.atm.app.model.LoadedCashModel;

public interface LoadedCashRepository extends JpaRepository<LoadedCashModel, Integer>{

}
