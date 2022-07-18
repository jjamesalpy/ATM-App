package com.jj.atm.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 * 
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jj.atm.app.errorHandler.CustomAtmException;
import com.jj.atm.app.model.DenominationModel;
import com.jj.atm.app.model.LoadedCashModel;
import com.jj.atm.app.repository.DenominationRepository;
import com.jj.atm.app.repository.LoadedCashRepository;

@Service
public class AtmCashVendingService {

	@Autowired
	private LoadedCashRepository loadedCashRepository;

	@Autowired
	private DenominationRepository denominationRepository;

	/**
	 * 
	 * @param loadedCashModel
	 * @return
	 */
	public LoadedCashModel loadCash(LoadedCashModel loadedCashModel) {
		loadedCashRepository.save(loadedCashModel);
		enrichAndSaveDenomination(loadedCashModel);
		return loadedCashModel;
	}

	/**
	 * 
	 * @param loadedCashModel
	 */
	private void enrichAndSaveDenomination(LoadedCashModel loadedCashModel) {
		DenominationModel dnm = new DenominationModel();
		dnm.setDenomCount20(loadedCashModel.getLoadDenom20());
		dnm.setDenomCount50(loadedCashModel.getLoadDenom50());
		denominationRepository.save(dnm);
	}

	/**
	 * @param integer
	 * @return
	 */
	public List<LoadedCashModel> getLoadedCash(Integer integer) {
		return loadedCashRepository.findAll();
	}

	/**
	 * 
	 * @param amount
	 * @throws CustomAtmException
	 */
	public LoadedCashModel resolveAndWithdrawCash(Integer amount) throws CustomAtmException {
		List<LoadedCashModel> loadedCashDetails = loadedCashRepository.findAll();
		LoadedCashModel lcmModel = loadedCashDetails.get(0);
		if (amount > lcmModel.getLoadAmount()) {
			throw new CustomAtmException(
					"The requested amount cannot be withdrawn, Please visit other ATM or request for a lesser amount");
		} else if (amount < lcmModel.getLoadAmount()) {
			return resolveDenominations(amount, lcmModel);
		} else {
			loadedCashRepository.deleteAll();
			lcmModel.setLoadAmount(0);
			lcmModel.setLoadDenom20(0);
			lcmModel.setLoadDenom50(0);
		}
		return lcmModel;
	}

	/**
	 * 
	 * @param amount
	 * @param lcmModel
	 * @return
	 * @throws CustomAtmException
	 */
	private LoadedCashModel resolveDenominations(Integer amount, LoadedCashModel lcmModel) throws CustomAtmException {
		List<DenominationModel> loadedCashDetails = denominationRepository.findAll();
		Integer denom50Count = 0;
		Integer denom20Count = 0;
		if (null != loadedCashDetails && (loadedCashDetails.size() > 0)) {
			DenominationModel dmnMOdel = loadedCashDetails.get(loadedCashDetails.size() - 1);
			denom50Count = dmnMOdel.getDenomCount50();
			denom20Count = dmnMOdel.getDenomCount20();
			List<List<Integer>> neededDenom = resolveDenominationCountNeeded(amount);
			if (null != neededDenom && neededDenom.size() > 0) {
				if (denom50Count > 0 && denom20Count > 0) {
					for (List<Integer> li : neededDenom) {
						if (li.get(0) < denom20Count && li.get(1) < denom50Count) {
							denom20Count = denom20Count - li.get(0);
							denom50Count = denom50Count - li.get(1);
							return setLoadSaveAndReturnDetails(lcmModel, dmnMOdel, amount, denom20Count, denom50Count);
						}
					}
				} else if (denom50Count == 0 && denom20Count > 0) {
					if (amount % 20 == 0) {
						denom20Count = (denom20Count - (amount / 20));
						return setLoadSaveAndReturnDetails(lcmModel, dmnMOdel, amount, denom20Count, 0);
					} else {
						throw new CustomAtmException("Please request amount with denominations of 20");
					}
				} else if (denom50Count > 0 && denom20Count == 0) {
					if (amount % 50 == 0) {
						denom50Count = (denom50Count - (amount / 50));
						return setLoadSaveAndReturnDetails(lcmModel, dmnMOdel, amount, 0, denom50Count);
					} else {
						throw new CustomAtmException("Please request amount with denominations of 50");
					}
				} else {
					throw new CustomAtmException("No cash left please visit other ATM");
				}
			} else {
				throw new CustomAtmException("Please request amount with denominations of either 20 or 50");
			}
		}
		return lcmModel;
	}

	/**
	 * 
	 * @param lcmModel
	 * @param dmnMOdel
	 * @param amount
	 * @param denom20Count
	 * @param denom50Count
	 * @return
	 */
	private LoadedCashModel setLoadSaveAndReturnDetails(LoadedCashModel lcmModel, DenominationModel dmnMOdel,
			Integer amount, Integer denom20Count, Integer denom50Count) {
		lcmModel.setLoadAmount(lcmModel.getLoadAmount() - amount);
		lcmModel.setLoadDenom20(denom20Count);
		lcmModel.setLoadDenom50(denom50Count);
		loadedCashRepository.save(lcmModel);
		dmnMOdel.setDenomCount20(lcmModel.getLoadDenom20());
		dmnMOdel.setDenomCount50(lcmModel.getLoadDenom50());
		denominationRepository.save(dmnMOdel);
		return lcmModel;
	}

	/**
	 * 
	 * @param nums
	 * @param target
	 * @return
	 */
	public List<List<Integer>> combinationSum(Integer[] denominations, Integer amount) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		Arrays.sort(denominations);
		recurseAndTrackDenomination(denominations, 0, amount, new ArrayList<Integer>(), result);
		return result;
	}

	/**
	 * 
	 * @param cand
	 * @param start
	 * @param target
	 * @param list
	 * @param result
	 */
	public void recurseAndTrackDenomination(Integer[] denominations, Integer start, Integer amount,
			ArrayList<Integer> list, List<List<Integer>> result) {
		if (amount < 0) {
			return;
		}
		if (amount == 0)
			result.add(new ArrayList<Integer>(list));
		for (int i = start; i < denominations.length; i++) {
			list.add(denominations[i]);
			recurseAndTrackDenomination(denominations, i, amount - denominations[i], list, result);
			list.remove(list.size() - 1);
		}
	}

	/**
	 * 
	 * @param amount
	 * @return
	 */
	private List<List<Integer>> resolveDenominationCountNeeded(Integer amount) {
		List<List<Integer>> returnCount = new ArrayList<List<Integer>>();
		List<Integer> output2 = null;
		List<List<Integer>> pair20And50 = new ArrayList<>();
		Integer[] denomination = { 20, 50 };
		returnCount = combinationSum(denomination, amount);
		for (List<Integer> sublist : returnCount) {
			Integer count20 = 0;
			Integer count50 = 0;
			output2 = new ArrayList<Integer>();
			for (Integer me : sublist) {
				if (me == 20) {
					count20++;
				} else if (me == 50) {
					count50++;
				}
			}
			output2.add(count20);
			output2.add(count50);
			pair20And50.add(output2);
		}
		return pair20And50;
	}
	
	/**
	 * 
	 * @param loadedCashModel
	 * @return
	 */
	public LoadedCashModel addCash(LoadedCashModel loadedCashModel) {
		LoadedCashModel lmmodel= loadedCashRepository.findAll().get(0);
		DenominationModel dmmodel= denominationRepository.findAll().get(0);
		lmmodel.setLoadAmount(lmmodel.getLoadAmount()+loadedCashModel.getLoadAmount());		
		lmmodel.setLoadDenom20(lmmodel.getLoadDenom20()+loadedCashModel.getLoadDenom20());
		lmmodel.setLoadDenom50(lmmodel.getLoadDenom50()+loadedCashModel.getLoadDenom50());
		loadedCashRepository.save(lmmodel);
		dmmodel.setDenomCount20(dmmodel.getDenomCount20()+ loadedCashModel.getLoadDenom20());
		dmmodel.setDenomCount50(dmmodel.getDenomCount50()+ loadedCashModel.getLoadDenom50());
		denominationRepository.save(dmmodel);
		return lmmodel;
	}
}
