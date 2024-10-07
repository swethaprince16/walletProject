package com.project.templates;

import com.project.dto.MailComponents;
import org.springframework.stereotype.Service;

@Service
public class MailTemplates {

    public static MailComponents getWalletCreationMailContent() {
        return MailComponents.builder()
                .mailSubject("new wallet  Onboarding")
                .mailBody("Hi %s, \n" +
                        "Your Wallet account is created. Now you have unlimited access to features offered by the wallet. \n " +
                        "Your daily limit is Rs. 10000 and daily transaction limit is 10. You can update this anytime you want. ")
                .build();

    }

    public static MailComponents getTransactionSuccessMessage() {
        return MailComponents.builder()
                .mailSubject("Transaction completed")
                .mailBody("Hi %s, \n" +
                        "Your transaction with Id %s is completed")
                .build();

    }
}
