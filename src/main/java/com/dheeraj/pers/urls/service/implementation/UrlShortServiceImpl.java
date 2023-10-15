package com.dheeraj.pers.urls.service.implementation;

import com.dheeraj.pers.urls.dao.UrlMapRepo;
import com.dheeraj.pers.urls.model.UrlEncodeRequest;
import com.dheeraj.pers.urls.model.UrlShortMapEntity;
import com.dheeraj.pers.urls.service.UrlShortService;
import com.dheeraj.pers.urls.util.UrlEncDec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class UrlShortServiceImpl implements UrlShortService {

    @Autowired
    private UrlMapRepo urlMapRepo;

    @Value("${public.url.server}")
    private String publicUrlServer;

    @Override
    public String encodeUrl(UrlEncodeRequest urlEncodeRequest) {
        UrlShortMapEntity urlShortMapEntity = new UrlShortMapEntity();
        urlShortMapEntity.setUrl(urlEncodeRequest.getUrl());
        urlMapRepo.save(urlShortMapEntity);
        long generatedId = urlShortMapEntity.getId();
        return publicUrlServer + UrlEncDec.encodeFromNumber(generatedId);
    }

    @Override
    public String decodeUrl(String target) {
        UrlShortMapEntity urlShortMapEntityTemp = new UrlShortMapEntity();
        urlShortMapEntityTemp.setUrl("www.google.com");
        return urlMapRepo.findById(UrlEncDec.decodeFromBase64(target.trim())).orElse(urlShortMapEntityTemp).getUrl();
    }
}
