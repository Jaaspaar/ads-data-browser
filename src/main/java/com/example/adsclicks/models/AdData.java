package com.example.adsclicks.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.util.Date;

public class AdData {
    @CsvBindByName(column = "Datasource")
    private String dataSource;
    @CsvBindByName(column = "Campaign")
    private String campaign;
    @CsvBindByName(column = "Clicks")
    private int clicks;
    @CsvBindByName(column = "Impressions")
    private int impressions;
    @CsvDate(value = "MM/DD/yy")
    @CsvBindByName(column = "Daily")
    private Date date;

    /**
     * Used by CSV mapper
     */
    public AdData() {
    }

    /**
     * Used for tests
     */
    public AdData(String dataSource, String campaign, int clicks, int impressions, Date date) {
        this.dataSource = dataSource;
        this.campaign = campaign;
        this.clicks = clicks;
        this.impressions = impressions;
        this.date = date;
    }

    public String getCampaign() {
        return campaign;
    }

    public String getDataSource() {
        return dataSource;
    }

    public int getClicks() {
        return clicks;
    }

    public int getImpressions() {
        return impressions;
    }

    public Date getDate() {
        return date;
    }

    public double getCtr() {
        return getClicks()/(double)getImpressions();
    }
}
