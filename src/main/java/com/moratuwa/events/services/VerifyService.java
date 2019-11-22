package com.moratuwa.events.services;

import com.moratuwa.events.repositories.AdminRepository;
import com.moratuwa.events.repositories.StudentRepository;
import com.moratuwa.events.repositories.VerifyCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    @Autowired
    VerifyCodeRepository verifyCodeRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AdminRepository adminRepository;

    public void createCode(String email, String code, String userType) {
        verifyCodeRepository.createCode(email, code, userType);
    }

    public boolean verify(String email, String code, String userType) {
        try {

            String codeToVerify = verifyCodeRepository.getCode(email, userType);
            if (codeToVerify == null) {
                return false;
            }

            if (!code.equals(codeToVerify)) {
                return false;
            }

            if(userType.equalsIgnoreCase("user")){
                studentRepository.activateStudent(email);
            }else{
                adminRepository.activateAdmin(email);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

