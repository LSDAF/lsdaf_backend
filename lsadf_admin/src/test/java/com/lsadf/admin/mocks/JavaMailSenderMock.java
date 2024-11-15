package com.lsadf.admin.mocks;

import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Stack;

public class JavaMailSenderMock extends JavaMailSenderImpl implements JavaMailSender {

    private final Stack<MimeMessage> mimeMessageStack;

    public JavaMailSenderMock(Stack<MimeMessage> mimeMessageStack) {
        this.mimeMessageStack = mimeMessageStack;
    }

    @Override
    public void send(@NotNull MimeMessage mimeMessage) throws MailException {
        this.mimeMessageStack.push(mimeMessage);
    }
}
