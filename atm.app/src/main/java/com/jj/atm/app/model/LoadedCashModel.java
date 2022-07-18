package com.jj.atm.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Loaded_Cash") 
public class LoadedCashModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer LoadID;
	
	public Integer LoadAmount;
	
	public Integer LoadDenom50;
	
	public Integer LoadDenom20;

	
	public LoadedCashModel(Integer loadID, Integer loadAmount, Integer loadDenom50, Integer loadDenom20) {
		super();
		LoadID = loadID;
		LoadAmount = loadAmount;
		LoadDenom50 = loadDenom50;
		LoadDenom20 = loadDenom20;
	}
	public LoadedCashModel() {}
	public Integer getLoadID() {
		return LoadID;
	}

	public void setLoadID(Integer loadID) {
		LoadID = loadID;
	}

	public Integer getLoadAmount() {
		return LoadAmount;
	}

	public void setLoadAmount(Integer loadAmount) {
		LoadAmount = loadAmount;
	}

	public Integer getLoadDenom50() {
		return LoadDenom50;
	}

	public void setLoadDenom50(Integer loadDenom50) {
		LoadDenom50 = loadDenom50;
	}

	public Integer getLoadDenom20() {
		return LoadDenom20;
	}

	public void setLoadDenom20(Integer loadDenom20) {
		LoadDenom20 = loadDenom20;
	}
	
	@Override
	public String toString() {
		return "LoadedCashModel [LoadID=" + LoadID + ", LoadAmount=" + LoadAmount + ", LoadDenom50=" + LoadDenom50
				+ ", LoadDenom20=" + LoadDenom20 + "]";
	}


}
