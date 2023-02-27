package com.agoodidea.photoAlbum.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoginByEmailService {

    UserDetails loginByEmail(String email);
}
