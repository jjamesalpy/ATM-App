package com.jj.atm.app.errorHandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AtmAppExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
    public String exception(Model theModel) {
        theModel.addAttribute("type", "Exception");
        return "exception";
    }
     
    @ExceptionHandler(value = CustomAtmException.class)
    public String customAtmException(CustomAtmException exception,Model theModel) {
    	theModel.addAttribute("errorDetails",exception.getMessage());
        return "customException";
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String illegalArgumentException(CustomAtmException exception) {
        return "itokke enthu";
    }
    
    
}
