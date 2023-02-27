package com.agoodidea.photoAlbum.service;

public interface MailService {

    Boolean sendEmailCode(String to, String code);
}
