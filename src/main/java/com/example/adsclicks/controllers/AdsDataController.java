package com.example.adsclicks.controllers;

import com.example.adsclicks.models.AdData;
import com.example.adsclicks.services.AdsDataLoader;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/ads")
public class AdsDataController {

    @Autowired
    private AdsDataLoader adsDataLoader;

    @GetMapping(value = "/{metric}/")
    public Object getMetric(
            @PathVariable("metric") Metric metric,
            @RequestParam(required = false) GroupingType groupingType,
            @RequestParam(required = false) String dataSource,
            @RequestParam(required = false) String campaign,
            @RequestParam (required = false) @DateTimeFormat(pattern = "MM-dd-yy") @ApiParam(example = "11-22-18") Date startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "MM-dd-yy") @ApiParam(example = "11-22-20") Date endDate) {

        Collector<AdData, ?, ?> metricCollector = summarizingInt(AdData::getClicks);
        switch (metric) {
            case CLICKS:
                metricCollector = summarizingInt(AdData::getClicks);
                break;
            case IMPRESSIONS:
                metricCollector = summarizingInt(AdData::getImpressions);
                break;
            case CTR:
                metricCollector = summarizingDouble(AdData::getCtr);
                break;
        }
        Stream<AdData> filteredAdsData = adsDataLoader.getAdsData()
                .stream()
                .filter(adData -> (dataSource == null || adData.getDataSource().equals(dataSource)) &&
                        (campaign == null || adData.getCampaign().equals(campaign)) &&
                        (startDate == null || adData.getDate().after(startDate)) &&
                        (endDate == null || adData.getDate().before(endDate)));
                //.collect(groupingBy(AdData::getDataSource, summingInt(AdData::getClicks)));

        if (groupingType == null)
            return filteredAdsData
                    .collect(metricCollector);

        switch (groupingType) {
            case DATA_SOURCE_THAN_CAMPAIGN:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDataSource, groupingBy(AdData::getCampaign, summarizingInt(AdData::getClicks))));
            case CAMPAIGN_THAN_DATA_SOURCE:
                return filteredAdsData
                        .collect(groupingBy(AdData::getCampaign, groupingBy(AdData::getDataSource, summarizingInt(AdData::getClicks))));
            case DATA_SOURCE:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDataSource, summarizingInt(AdData::getClicks)));
            case CAMPAIGN:
                return filteredAdsData
                        .collect(groupingBy(AdData::getCampaign, summarizingInt(AdData::getClicks)));
            default:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDate, metricCollector));
        }
//        return RestPreconditions.checkFound(service.findById(id));
    }

//    @GetMapping(value = "/total-clicks/{dataSource}/{startDate}/{endDate}")
//    public int getTotalClickByDataSourceAndDateRange2() {
//        return 1;
//    }
}

enum Metric {
    CLICKS,
    IMPRESSIONS,
    CTR
}

enum GroupingType {
    DATA_SOURCE_THAN_CAMPAIGN,
    CAMPAIGN_THAN_DATA_SOURCE,
    DATA_SOURCE,
    CAMPAIGN,
    DATE,
}
