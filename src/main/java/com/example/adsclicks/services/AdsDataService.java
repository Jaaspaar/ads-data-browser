package com.example.adsclicks.services;

import com.example.adsclicks.loaders.AdsDataLoader;
import com.example.adsclicks.models.AdData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdsDataService {
    private final List<AdData> adsData;

    @Autowired
    public AdsDataService(AdsDataLoader adsDataLoader) {
        this.adsData = adsDataLoader.loadAdsData();
        System.out.println("Data loaded");
    }

    /**
     * Returns ads data
     * @return ads data
     */
    public List<AdData> getAdsData() {
        return adsData;
    }
}
