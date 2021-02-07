package com.example.adsclicks.loaders;

import com.example.adsclicks.models.AdData;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Service
public class AdsDataLoader {
    public static final String ADS_DATA_FILE_NAME = "ads-data.csv";

    /**
     * Loads ads data
     * @return ads data
     */
    public List<AdData> loadAdsData() {
        return Collections.unmodifiableList(loadAdsDataFromCsv(ADS_DATA_FILE_NAME));
    }

    private List<AdData> loadAdsDataFromCsv(String fileName) {
        try {
            CsvToBeanBuilder<AdData> beanBuilder = new CsvToBeanBuilder<>(new InputStreamReader(new FileInputStream(fileName)));

            return beanBuilder
                    .withType(AdData.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return null;
    };
}
