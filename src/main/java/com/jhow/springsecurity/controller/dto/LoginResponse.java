package com.jhow.springsecurity.controller.dto;

public record LoginResponse(String acessToken, Long expiresIn) {

}
