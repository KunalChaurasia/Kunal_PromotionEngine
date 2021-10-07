package com.test.prmotions.serivce.processors;

import com.test.prmotions.entities.PromoDetails;
import com.test.prmotions.entities.Promotion;
import com.test.prmotions.entities.SKU;
import com.test.prmotions.serivce.PromoService;
import com.test.prmotions.serivce.impl.SKUService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromotionEngine {

    SKUService skuService;

    PromoService promoService;

    public int process(Map<String , Integer> orders) {
        int sum = 0;
        List<SKU> skuList = skuService.getAllSKUs();
        List<Promotion> promotions = promoService.getAllPromotions();

        Map<String, Integer> priceMap = skuList.stream().collect(Collectors.toMap(SKU::getName, SKU::getPrice));

        Map<String, Integer> promoPriceMap = promotions.stream().collect(Collectors.toMap(e -> e.getPromoName(), e -> e.getPromoPrice()));
        // skuNme -> List<Promotion>
        // A -> PromoA
        // C -> promoC
        // D -> promoC
        //reverse Map the values, since promotions are mutually exclusive don't need a list
        Map<String, String> skuNameToPromoMap = new HashMap<>();
        promotions.forEach(promotion -> {
            promotion.getPromoName();
            promotion.getPromoDetails().forEach(promoDetails ->
            {
                skuNameToPromoMap.put(promoDetails.getSkuName(), promotion.getPromoName());
            });
        });

        Map<String, Map<String, Integer>> promoToPromoDetails = null;
        // promoA >      A , 3
        // promoB >      B , 2
        //
        // promoC >      C , 1
        //               D , 1


        for (Map.Entry<String, Integer> orderEntry : orders.entrySet()) {

            String skuName = orderEntry.getKey();
            int orderCount = orderEntry.getValue();
            String promoId = skuNameToPromoMap.get(skuName);
            if (null == promoId) {
                sum = sum + (priceMap.get(skuName) * orderCount);
            } else {

                boolean applyPromo = isApplyPromo(orders, promoToPromoDetails, promoId);

                if (applyPromo) {

                    Map<String, Integer> promoSKUCountMap = promoToPromoDetails.get(promoId);
                    List<String> skuNameList = new ArrayList<>(promoSKUCountMap.keySet());
                    List<Integer> requiredSKUCountList = new ArrayList<>(promoSKUCountMap.values());

                    int sizeOfList = skuNameList.size();
                    if (1 == sizeOfList) {
                        String skuNameFromPromo = skuNameList.get(0);
                        int skuCountFromPromo = requiredSKUCountList.get(0);
                        int inputOrderCount = orders.get(skuNameFromPromo);

                        int price = promoPriceMap.get(promoId);
                        int countOfPromosToApply = inputOrderCount / skuCountFromPromo;
                        int balanceCount = inputOrderCount % skuCountFromPromo;

                        sum = sum + (price * countOfPromosToApply) + (priceMap.get(skuName) * balanceCount);

                    }
                    //cases like C+D
                    else {
                        for (int i = 0; i < sizeOfList; i++) {
                            String skuNameFromPromo = skuNameList.get(0);
                            int skuCountFromPromo = requiredSKUCountList.get(0);
                            int inputOrderCount = orders.get(skuNameFromPromo);

                            //GET count of promos to apply
                            //calculate price for promo applies from order
                            // reduce the sku count for which promo has been applied
                            //calculte price for the normal sku count
                        }
                    }

                }else{
                        sum = sum + (priceMap.get(skuName) * orderCount);
                    }
                }
            }
            return sum;
        }



    private boolean isApplyPromo(Map<String, Integer> orders, Map<String, Map<String, Integer>> promoToPromoDetails, String promoId) {
        boolean applyPromo = false;
        Map<String , Integer > promoSKUCountMap = promoToPromoDetails.get(promoId);
        for (Map.Entry<String,Integer> entry1: promoSKUCountMap.entrySet()) {

           String skuNameFromPromo = entry1.getKey();
           int skuCountFromPromo  = entry1.getValue();
           int inputOrderCount = orders.get(skuNameFromPromo);
           if(inputOrderCount >= skuCountFromPromo ){
               applyPromo = true;
           }
           else{
               applyPromo = false;
               break;
           }
        }
        return applyPromo;
    }
}
