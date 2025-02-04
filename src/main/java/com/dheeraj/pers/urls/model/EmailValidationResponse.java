package com.dheeraj.pers.urls.model;

import lombok.Data;

@Data
public class EmailValidationResponse {
    private String email;
    private boolean validSyntax;
    private boolean validDomain;
    private boolean disposableDomain;
    private boolean isInSpamForum;
    private int sanityScore;

    public void setSanityScore(int sanityScore) {
        this.sanityScore = Math.min(100, Math.max(sanityScore, 0));
    }
}
