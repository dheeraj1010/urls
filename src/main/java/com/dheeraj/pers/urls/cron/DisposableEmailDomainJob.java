package com.dheeraj.pers.urls.cron;

import com.dheeraj.pers.urls.dao.DisposableDomainRepo;
import com.dheeraj.pers.urls.dao.entity.DisposableEmailDomain;
import com.dheeraj.pers.urls.exception.RestExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class DisposableEmailDomainJob {

    private static final Logger logger = LogManager.getLogger(DisposableEmailDomainJob.class);



    @Autowired
    private DisposableDomainRepo disposableDomainRepo;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${disposable.mail.url}")
    private String dispDomainGithubUrl;

    @PostConstruct
    public void init() {
        if(disposableDomainRepo.count() < 1500){
            logger.info("No disposable domains found in the database. Fetching from the source.");
            fetchAndPersistDisposableEmailDomains();
        }

    }

    @Scheduled(cron = "${disposable.mail.refresh.cron}")
    public void fetchAndPersistDisposableEmailDomains() {

        List<String> domains = fetchDisposableEmailDomains();
        logger.info("Total disposable domains fetched: {}", domains.size());
        int insertCount = 0;
        for (String domain : domains) {
            if (!disposableDomainRepo.findByDomain(domain).isPresent()) {
                DisposableEmailDomain disposableEmailDomain = new DisposableEmailDomain();
                disposableEmailDomain.setDomain(domain);
                disposableDomainRepo.save(disposableEmailDomain);
                insertCount++;
            }
        }
        logger.info("Total disposable domains inserted: {}", insertCount);
    }


    public List<String> fetchDisposableEmailDomains() {
        logger.info("Fetching disposable email domains from: {}", dispDomainGithubUrl);
        String response = restTemplate.getForObject(dispDomainGithubUrl, String.class);
        return Arrays.asList(response.split("\n"));
    }

}
