package com.dheeraj.pers.urls.service.implementation;
import com.dheeraj.pers.urls.dao.DisposableDomainRepo;
import com.dheeraj.pers.urls.model.EmailValidationRequest;
import com.dheeraj.pers.urls.model.EmailValidationResponse;
import com.dheeraj.pers.urls.service.EmailValidationService;
import com.dheeraj.pers.urls.util.ApiCaller;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
public class EmailValidationServiceImpl implements EmailValidationService {

    private static final Logger logger = LogManager.getLogger(EmailValidationServiceImpl.class);

    @Autowired
    private DisposableDomainRepo disposableDomainRepo;

    @Autowired
    private ApiCaller apiCaller;

    @Override
    public EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest) {
        logger.info("Starting email validation for: {}", emailValidationRequest.getEmail());
        EmailValidationResponse response = new EmailValidationResponse();
        response.setEmail(emailValidationRequest.getEmail());
        response.setValidSyntax(validateEmailSyntax(emailValidationRequest.getEmail()));

        CompletableFuture<Boolean> syntaxValidateFuture = CompletableFuture.supplyAsync(() -> validateEmailDNS(emailValidationRequest.getEmail()));
        CompletableFuture<Boolean> disposableDomainFuture = CompletableFuture.supplyAsync(() -> validateDisposableDomain(emailValidationRequest.getEmail()));
        CompletableFuture<Boolean> publicSpamForumFuture = CompletableFuture.supplyAsync(() -> isInPublicSpamForum(emailValidationRequest.getEmail()));

        try {
            CompletableFuture.allOf(syntaxValidateFuture, disposableDomainFuture, publicSpamForumFuture).join();
            response.setValidDomain(syntaxValidateFuture.get());
            response.setDisposableDomain(disposableDomainFuture.get());
            response.setInSpamForum(publicSpamForumFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error during email validation: {}", e.getMessage());
        }

        if (response.isValidSyntax()) response.setSanityScore(response.getSanityScore() + 40);
        if (response.isValidDomain()) response.setSanityScore(response.getSanityScore() + 20);
        if (!response.isDisposableDomain()) response.setSanityScore(response.getSanityScore() + 20);
        if (!response.isInSpamForum()) response.setSanityScore(response.getSanityScore() + 20);

        logger.info("Email validation completed for: {}. Response: {}", emailValidationRequest.getEmail(), response);
        return response;
    }

    private boolean validateEmailSyntax(String email) {
        logger.debug("Validating email syntax for: {}", email);
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        boolean isValid = Pattern.matches(emailRegex, email);
        logger.debug("Email syntax validation result for {}: {}", email, isValid);
        return isValid;
    }

    private boolean validateEmailDNS(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        logger.debug("Validating email DNS for domain: {}", domain);
        boolean isValid = isDomainValid(domain);
        logger.debug("Email DNS validation result for domain {}: {}", domain, isValid);
        return isValid;
    }

    private boolean isDomainValid(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            lookup.setResolver(new SimpleResolver());
            lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] records = lookup.getAnswers();
                boolean isValid = records != null && records.length > 0;
                logger.debug("Domain {} is valid: {}", domain, isValid);
                return isValid;
            }
            logger.debug("Domain {} is invalid: no MX records found", domain);
            return false;
        } catch (Exception e) {
            logger.error("Error validating domain {}: {}", domain, e.getMessage());
            return false;
        }
    }

    private static boolean validateEmailSMTP(String smtpServer, int smtpPort, String senderEmail, String recipientEmail) {
        try {
            logger.debug("Validating email SMTP for recipient: {}", recipientEmail);
            Socket socket = new Socket(smtpServer, smtpPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String response = reader.readLine();
            logger.info("Server: " + response);

            writer.println("HELO clientdomain.com");
            response = reader.readLine();
            logger.info("Server: " + response);

            writer.println("MAIL FROM:<" + senderEmail + ">");
            response = reader.readLine();
            logger.info("Server: " + response);

            writer.println("RCPT TO:<" + recipientEmail + ">");
            response = reader.readLine();
            logger.info("Server: " + response);

            if (response.startsWith("250")) {
                logger.info("The email address {} is valid.", recipientEmail);
                return true;
            } else if (response.startsWith("550")) {
                logger.info("The email address {} is invalid: user unknown.", recipientEmail);
                return false;
            } else if (response.startsWith("450")) {
                logger.info("The email address {} is invalid: mailbox unavailable temporarily.", recipientEmail);
                return false;
            } else if (response.startsWith("421")) {
                logger.info("The email address {} is invalid: service not available.", recipientEmail);
                return false;
            } else {
                logger.info("The email address {} could not be validated: {}", recipientEmail, response);
                return false;
            }

        } catch (IOException e) {
            logger.error("Error validating email SMTP for {}: {}", recipientEmail, e.getMessage());
            return false;
        }
    }

    private boolean validateDisposableDomain(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        logger.info("Checking if domain is disposable: {}", domain);
        boolean isDisposable = disposableDomainRepo.findByDomain(domain).isPresent();
        logger.debug("Disposable domain check result for {}: {}", domain, isDisposable);
        return isDisposable;
    }

    private boolean isInPublicSpamForum(String email) {
        String username = email.substring(0, email.indexOf('@'));
        String url = "https://api.stopforumspam.org/api";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("json", "");
        logger.debug("Checking if email is in public spam forum: {}", email);
        JsonNode rootNode = apiCaller.callApi(url, HttpMethod.GET, params);
        if (rootNode == null) {
            logger.debug("No response from spam forum API for email: {}", email);
            return false;
        }
        JsonNode emailNode = rootNode.path("email");
        JsonNode usernameNode = rootNode.path("username");
        int frequencyEmail = emailNode.path("frequency").asInt();
        int frequencyUsername = usernameNode.path("frequency").asInt();
        boolean isInSpamForum = frequencyUsername > 0 || frequencyEmail > 0;
        logger.debug("Spam forum check result for {}: {}", email, isInSpamForum);
        return isInSpamForum;
    }
}