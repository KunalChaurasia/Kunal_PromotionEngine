package com.test.prmotions.serivce;

import com.test.prmotions.entities.Promotion;

import java.util.List;

public interface PromoService {

    void addPromo(Promotion promotion);

    void deletePromo(String id);

    void updatePromo(Promotion promotion);

    Promotion  findPromoById(String id);

    List<Promotion> getAllPromotions();

}
