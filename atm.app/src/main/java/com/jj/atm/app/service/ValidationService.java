package com.jj.atm.app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jj.atm.app.errorHandler.CustomAtmException;
import com.jj.atm.app.model.LoadedCashModel;
import com.jj.atm.app.repository.DenominationRepository;
import com.jj.atm.app.repository.LoadedCashRepository;

@Service
public class ValidationService {
	
	@Autowired
	private LoadedCashRepository loadedCashRepository;
	
	@Autowired
	private DenominationRepository denominationRepository;
	
	/**
	 * Validate the cash is already loaded or not
	 * @return
	 */
	public String validateLoadedCash(){	
		List<LoadedCashModel> loadedCashModelQueried = loadedCashRepository.findAll();	
		if(null!=loadedCashModelQueried && loadedCashModelQueried.size()>0) {
			return "Cash is already loaded to the system,.! either add or withdraw cash" ;
		}
		else return "";
	}
	
	/**
	 * Validate the provided amount and denomination is correct
	 * @param loadedCashModel 
	 * @return
	 */
	public String validateCashAndDenomination(LoadedCashModel loadedCashModel) {
		if(Objects.nonNull(loadedCashModel)) {
			Integer sumFromDenom  =  ((loadedCashModel.LoadDenom20 * 20) + (loadedCashModel.LoadDenom50 * 50));
			if(!(loadedCashModel.getLoadAmount().equals(sumFromDenom))) {
				return "Amount entered and the denominations is not correct";
			}
			else {
				return "";
			}
		}
		return "";
	}
	
	
}
