package com.example.adsclicks.controllers;

import com.example.adsclicks.models.AdData;
import com.example.adsclicks.services.AdsDataService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/ads")
public class AdsDataController {

    @Autowired
    private AdsDataService adsDataService;

    /**
     * Generic endpoint to query data
     * @param metric required metric
     * @param groupingType optional grouping type
     * @param dataSource optional filter
     * @param campaign optional filter
     * @param startDate optional filter
     * @param endDate optional filter
     * @return
     */
    @GetMapping(value = "/{metric}/")
    private Object getMetric(
            @PathVariable("metric") Metric metric,
            @RequestParam(required = false) GroupingType groupingType,
            @RequestParam(required = false) String dataSource,
            @RequestParam(required = false) String campaign,
            @RequestParam (required = false) @DateTimeFormat(pattern = "MM-dd-yy") @ApiParam(example = "11-22-18") Date startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "MM-dd-yy") @ApiParam(example = "11-22-20") Date endDate) {

        Collector<AdData, ?, ?> metricCollector = getMetricCollector(metric);
        Stream<AdData> filteredAdsData = getFilteredAdsData(dataSource, campaign, startDate, endDate);

        if (groupingType == null)
            return filteredAdsData
                    .collect(metricCollector);

        return getGroupedResult(groupingType, metricCollector, filteredAdsData);
    }

    private Map<? extends Serializable, ?> getGroupedResult(GroupingType groupingType, Collector<AdData, ?, ?> metricCollector, Stream<AdData> filteredAdsData) {
        switch (groupingType) {
            case DATA_SOURCE_THAN_CAMPAIGN:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDataSource, groupingBy(AdData::getCampaign, metricCollector)));
            case CAMPAIGN_THAN_DATA_SOURCE:
                return filteredAdsData
                        .collect(groupingBy(AdData::getCampaign, groupingBy(AdData::getDataSource, metricCollector)));
            case DATA_SOURCE:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDataSource, metricCollector));
            case CAMPAIGN:
                return filteredAdsData
                        .collect(groupingBy(AdData::getCampaign, metricCollector));
            default:
                return filteredAdsData
                        .collect(groupingBy(AdData::getDate, metricCollector));
        }
    }

    private Collector<AdData, ?, ?> getMetricCollector(Metric metric) {
        switch (metric) {
            case IMPRESSIONS:
                return summarizingInt(AdData::getImpressions);
            case CTR:
                return summarizingDouble(AdData::getCtr);
            default:
                return summarizingInt(AdData::getClicks);
        }
    }

    private Stream<AdData> getFilteredAdsData(String dataSource, String campaign, Date startDate, Date endDate) {
        return adsDataService.getAdsData()
                .stream()
                .filter(adData -> (dataSource == null || adData.getDataSource().equals(dataSource)) &&
                        (campaign == null || adData.getCampaign().equals(campaign)) &&
                        (startDate == null || adData.getDate().after(startDate)) &&
                        (endDate == null || adData.getDate().before(endDate)));
    }
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
