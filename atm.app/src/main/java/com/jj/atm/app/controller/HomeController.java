package com.jj.atm.app.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jj.atm.app.errorHandler.CustomAtmException;
import com.jj.atm.app.model.LoadedCashModel;
import com.jj.atm.app.service.AtmCashVendingService;
import com.jj.atm.app.service.ValidationService;
/**
 * 
 * @author jj
 *
 */
@Controller
public class HomeController {

	@Autowired
	private AtmCashVendingService atmCashVendingService;

	@Autowired
	private ValidationService validationService;

	@RequestMapping("/")
	public String home() {
		return "home";
	}
	/**
	 * 
	 * @param loadedCashModel
	 * @param result
	 * @param model
	 * @return
	 * @throws CustomAtmException
	 */
	@PostMapping("/loadCash")
	public String loadCash(LoadedCashModel loadedCashModel, BindingResult result, Model model)
			throws CustomAtmException {
		String validateCashAndDenomination = validationService.validateCashAndDenomination(loadedCashModel);
		String errorDetails = validationService.validateLoadedCash();
		if (!validateCashAndDenomination.isEmpty()) {
			model.addAttribute("isInitVal", "Y");
			return seterror(validateCashAndDenomination, result, model);
		} else if (!errorDetails.isEmpty()) {
			return seterror(errorDetails, result, model);
		}
		atmCashVendingService.loadCash(loadedCashModel);
		atmCashVendingService.getLoadedCash(loadedCashModel.getLoadID());
		return "cashLoaded";
	}
	
	/**
	 * 
	 * @param loadedCashModel
	 * @return
	 */
	@RequestMapping("/homeLoadcash")
	public String exceptionLandingPage(LoadedCashModel loadedCashModel) {
		List<LoadedCashModel> loadCashList = atmCashVendingService.getLoadedCash(loadedCashModel.getLoadID());
		enrichRetrunModelObject(loadedCashModel, loadCashList);
		return "cashLoaded";
	}
	
	/**
	 * 
	 * @param amount
	 * @param loadedCashModel
	 * @return
	 * @throws CustomAtmException 
	 */
	@GetMapping("/withdrawCash")
	public String withdrawCash(@RequestParam("LoadAmount") Integer amount,LoadedCashModel loadedCashModel) throws CustomAtmException {
		LoadedCashModel retObj = null;
		if(Objects.nonNull(loadedCashModel)) {
			retObj = atmCashVendingService.resolveAndWithdrawCash(amount);
			copyProperties(retObj, loadedCashModel);
		}
		return "amountWithdrawn";
	}
	
	/**
	 * 
	 * @param loadedCashModel
	 * @return
	 * @throws CustomAtmException 
	 */
	@PostMapping("/addCash")
	public String addCash(LoadedCashModel loadedCashModel) throws CustomAtmException {
		String validateCashAndDenomination =validationService.validateCashAndDenomination(loadedCashModel);
		if (!validateCashAndDenomination.isEmpty()) {
			throw new CustomAtmException("Amount entered and the denominations is not correct");
		}
		LoadedCashModel loadCashList = atmCashVendingService.addCash(loadedCashModel);
		copyProperties(loadCashList, loadedCashModel);
		return "cashLoaded";
	}
	/**
	 * 
	 * @param loadedCashModel
	 * @param loadCashList
	 */
	private void enrichRetrunModelObject(LoadedCashModel loadedCashModel, List<LoadedCashModel> loadCashList) {
		if (null != loadCashList && loadCashList.size() > 0) {
			for (LoadedCashModel lcmod : loadCashList) {
				copyProperties(lcmod, loadedCashModel);
			}
		}
	}
	/**
	 * 
	 * @param loadedCashModel
	 * @param loadedCashModelEndObj
	 */
	private void copyProperties(LoadedCashModel loadedCashModel, LoadedCashModel loadedCashModelEndObj) {
		loadedCashModelEndObj.setLoadAmount(loadedCashModel.getLoadAmount());
		loadedCashModelEndObj.setLoadDenom20(loadedCashModel.getLoadDenom20());
		loadedCashModelEndObj.setLoadDenom50(loadedCashModel.getLoadDenom50());
	}
	/**
	 * 
	 * @param errorDetails
	 * @param result
	 * @param model
	 * @return
	 */
	private String seterror(String errorDetails, BindingResult result, Model model) {
		result.addError(new ObjectError("globalError", errorDetails));
		model.addAttribute("errorDetails", errorDetails);
		return "customException";
	}

}
