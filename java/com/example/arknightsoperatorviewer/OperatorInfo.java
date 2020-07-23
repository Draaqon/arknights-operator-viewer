package com.example.arknightsoperatorviewer;

import java.io.Serializable;

public class OperatorInfo implements Serializable {

    private String name, type, profile, clinicalAnalysis, trust1, trust2, trust3, trust4, affiliation;
    private int stars;

    public OperatorInfo()
    {
        name = null;
        type = null;
        profile = null;
        clinicalAnalysis = null;
        trust1 = null;
        trust2 = null;
        trust3 = null;
        trust4 = null;
        affiliation = null;
        stars = 0;
    }

    public OperatorInfo(String name, String type, String profile, String clinicalAnalysis, String trust1, String trust2, String trust3, String trust4, String affiliation)
    {
        this.name = name;
        this.type = type;
        this.profile = profile;
        this.clinicalAnalysis = clinicalAnalysis;
        this.trust1 = trust1;
        this.trust2 = trust2;
        this.trust3 = trust3;
        this.trust4 = trust4;
        this.affiliation = affiliation;
        this.stars = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getProfile() {
        return profile;
    }

    public String getClinicalAnalysis() {
        return clinicalAnalysis;
    }

    public String getTrust1() {
        return trust1;
    }

    public String getTrust2() {
        return trust2;
    }

    public String getTrust3() {
        return trust3;
    }

    public String getTrust4() {
        return trust4;
    }

    public int getStars()
    {
        return stars;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setProfile(String profile)
    {
        this.profile = profile;
    }

    public void setClinicalAnalysis(String clinicalAnalysis)
    {
        this.clinicalAnalysis = clinicalAnalysis;
    }

    public void setTrust1(String trust1)
    {
        this.trust1 = trust1;
    }

    public void setTrust2(String trust2)
    {
        this.trust2 = trust2;
    }

    public void setTrust3(String trust3)
    {
        this.trust3 = trust3;
    }

    public void setTrust4(String trust4)
    {
        this.trust4 = trust4;
    }

    public void setStars(int stars)
    {
        this.stars = stars;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public static class Operator2Star extends OperatorInfo
    {
        public Operator2Star(String name, String type, String profile, String clinicalAnalysis, String trust1, String trust2, String trust3, String trust4, int stars, String affiliation)
        {
            super(name, type, profile, clinicalAnalysis, trust1, trust2, trust3, trust4, affiliation);
            this.setStars(stars);
        }
    }

    public static class Operator3Star extends OperatorInfo
    {
        public Operator3Star(String name, String type, String profile, String clinicalAnalysis, String trust1, String trust2, String trust3, String trust4, int stars, String affiliation)
        {
            super(name, type, profile, clinicalAnalysis, trust1, trust2, trust3, trust4, affiliation);
            this.setStars(3);
        }
    }

    public static class Operator45Star extends OperatorInfo
    {
        String promo;
        public Operator45Star(String name, String type, String profile, String clinicalAnalysis, String trust1, String trust2, String trust3, String trust4, int stars, String affiliation, String promo)
        {
            super(name, type, profile, clinicalAnalysis, trust1, trust2, trust3, trust4, affiliation);
            this.setStars(stars);
            this.promo = promo;
        }
        public String getPromo()
        {
            return promo;
        }

        public void setPromo(String promo)
        {
            this.promo = promo;
        }
    }
}
