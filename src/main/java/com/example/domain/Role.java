package com.example.domain;


import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority, Serializable {
User, Admin;

@Override
public String getAuthority() {
	return name();
}
}
