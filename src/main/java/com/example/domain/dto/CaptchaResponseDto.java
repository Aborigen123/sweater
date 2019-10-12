package com.example.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {//запит до гугла який буде вертати нам json. В цьому класі ми опишемо поля які ми будемо принімати і обробляти всі остальні будемо ігнорити 

private boolean success;
@JsonAlias("error-codes")//назва поля json response
private Set<String> errorCoders;
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public Set<String> getErrorCoders() {
	return errorCoders;
}
public void setErrorCoders(Set<String> errorCoders) {
	this.errorCoders = errorCoders;
}


}
