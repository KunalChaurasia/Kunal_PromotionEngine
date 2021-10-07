package com.test.prmotions.entities;

import java.util.List;

public class Promotion {

    List<PromoDetails> promoDetails ;
    private String promoName;
    private int promoPrice ;

    public List<PromoDetails> getPromoDetails() {
        return promoDetails;
    }

    public void setPromoDetails(List<PromoDetails> promoDetails) {
        this.promoDetails = promoDetails;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public int getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(int promoPrice) {
        this.promoPrice = promoPrice;
    }
}
