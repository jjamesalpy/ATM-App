package com.jj.atm.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "ATM_Denomination") 
public class DenominationModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer DenomID;
	
	public Integer DenomCount50;
	
	public Integer DenomCount20;
	
	public DenominationModel(Integer denomID, Integer denomCount50, Integer denomCount20) {
		super();
		DenomID = denomID;
		DenomCount50 = denomCount50;
		DenomCount20 = denomCount20;
	}
	public DenominationModel() {}
	public Integer getDenomID() {
		return DenomID;
	}
	public void setDenomID(Integer denomID) {
		DenomID = denomID;
	}
	
	public Integer getDenomCount50() {
		return DenomCount50;
	}
	public void setDenomCount50(Integer denomCount50) {
		DenomCount50 = denomCount50;
	}
	public Integer getDenomCount20() {
		return DenomCount20;
	}
	public void setDenomCount20(Integer denomCount20) {
		DenomCount20 = denomCount20;
	}
	@Override
	public String toString() {
		return "DenominationModel [DenomID=" + DenomID + ", DenomCount50=" + DenomCount50 + ", DenomCount20="
				+ DenomCount20 + "]";
	}

	
}
