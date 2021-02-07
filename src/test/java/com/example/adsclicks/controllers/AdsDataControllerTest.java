package com.example.adsclicks.controllers;


import com.example.adsclicks.models.AdData;
import com.example.adsclicks.services.AdsDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(AdsDataController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AdsDataApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AdsDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdsDataService adsDataService;

    @Before
    public void setUp() throws ParseException {
        var dateFormat = new SimpleDateFormat("MM-dd-yy");
        List<AdData> adsData = List.of(
            new AdData("Google Ads", "Adventmarkt Touristik", 7, 1000, dateFormat.parse("11-12-18")),
            new AdData("Google Ads", "GDN_Retargeting", 27, 2000, dateFormat.parse("11-12-20")),
            new AdData("Twitter Ads", "Adventmarkt Touristik", 7, 1000, dateFormat.parse("11-12-18")),
            new AdData("Twitter Ads", "GDN_Retargeting", 27, 2000, dateFormat.parse("11-12-20"))
        );
        Mockito.when(adsDataService.getAdsData()).thenReturn(adsData);
    }

    @Test
    public void should_return_info_about_clicks_when_CLICKS_passed_as_metric() throws Exception {
        mvc.perform(get("/ads/CLICKS/"))
                .andExpect(content().string("{\"count\":4,\"sum\":68,\"min\":7,\"max\":27,\"average\":17.0}"));
    }

    @Test
    public void should_return_info_about_impressions_when_IMPRESSIONS_passed_as_metric() throws Exception {
        mvc.perform(get("/ads/IMPRESSIONS/"))
                .andExpect(content().string("{\"count\":4,\"sum\":6000,\"min\":1000,\"max\":2000,\"average\":1500.0}"));
    }

    @Test
    public void should_return_info_about_ctr_when_CTR_passed_as_metric() throws Exception {
        mvc.perform(get("/ads/CTR/"))
                .andExpect(content().string("{\"count\":4,\"sum\":0.040999999999999995,\"min\":0.007,\"max\":0.0135,\"average\":0.010249999999999999}"));
    }

    @Test
    public void should_return_info_about_clicks_grouped_by_data_source_and_campaign_when_CLICKS_passed_as_metric_and_DATA_SOURCE_THAN_CAMPAIGN_as_groupingType() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=DATA_SOURCE_THAN_CAMPAIGN"))
                .andExpect(content().string("{\"Google Ads\":{\"GDN_Retargeting\":{\"count\":1,\"sum\":27,\"min\":27,\"max\":27,\"average\":27.0},\"Adventmarkt Touristik\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0}},\"Twitter Ads\":{\"GDN_Retargeting\":{\"count\":1,\"sum\":27,\"min\":27,\"max\":27,\"average\":27.0},\"Adventmarkt Touristik\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0}}}"));
    }

    @Test
    public void should_return_info_about_clicks_grouped_by_campaign_and_data_source_when_CLICKS_passed_as_metric_and_CAMPAIGN_THAN_DATA_SOURCE_as_groupingType() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=CAMPAIGN_THAN_DATA_SOURCE"))
                .andExpect(content().string("{\"GDN_Retargeting\":{\"Google Ads\":{\"count\":1,\"sum\":27,\"min\":27,\"max\":27,\"average\":27.0},\"Twitter Ads\":{\"count\":1,\"sum\":27,\"min\":27,\"max\":27,\"average\":27.0}},\"Adventmarkt Touristik\":{\"Google Ads\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0},\"Twitter Ads\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0}}}"));
    }

    @Test
    public void should_return_info_about_clicks_grouped_by_data_source_when_CLICKS_passed_as_metric_and_DATA_SOURCE_as_groupingType() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=DATA_SOURCE"))
                .andExpect(content().string("{\"Google Ads\":{\"count\":2,\"sum\":34,\"min\":7,\"max\":27,\"average\":17.0},\"Twitter Ads\":{\"count\":2,\"sum\":34,\"min\":7,\"max\":27,\"average\":17.0}}"));
    }

    @Test
    public void should_return_info_about_clicks_grouped_by_campaign_when_CLICKS_passed_as_metric_and_CAMPAIGN_as_groupingType() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=CAMPAIGN"))
                .andExpect(content().string("{\"GDN_Retargeting\":{\"count\":2,\"sum\":54,\"min\":27,\"max\":27,\"average\":27.0},\"Adventmarkt Touristik\":{\"count\":2,\"sum\":14,\"min\":7,\"max\":7,\"average\":7.0}}"));
    }

    @Test
    public void should_return_info_about_clicks_grouped_by_date_when_CLICKS_passed_as_metric_and_DATE_as_groupingType() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=DATE"))
                .andExpect(content().string("{\"2018-11-11T23:00:00.000+00:00\":{\"count\":2,\"sum\":14,\"min\":7,\"max\":7,\"average\":7.0},\"2020-11-11T23:00:00.000+00:00\":{\"count\":2,\"sum\":54,\"min\":27,\"max\":27,\"average\":27.0}}"));
    }

    @Test
    public void should_return_filtered_info_when_data_source_and_campaign_filters_are_used() throws Exception {
        //&dataSource=Google%20Ads&campaign=GDN_Retargeting&startDate=10-12-18&endDate=10-12-19
        mvc.perform(get("/ads/CLICKS/?groupingType=DATA_SOURCE_THAN_CAMPAIGN&dataSource=Google Ads&campaign=GDN_Retargeting"))
                .andExpect(content().string("{\"Google Ads\":{\"GDN_Retargeting\":{\"count\":1,\"sum\":27,\"min\":27,\"max\":27,\"average\":27.0}}}"));
    }

    @Test
    public void should_return_filtered_info_when_date_filters_are_used() throws Exception {
        mvc.perform(get("/ads/CLICKS/?groupingType=DATA_SOURCE_THAN_CAMPAIGN&startDate=10-12-18&endDate=10-12-19"))
                .andExpect(content().string("{\"Google Ads\":{\"Adventmarkt Touristik\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0}},\"Twitter Ads\":{\"Adventmarkt Touristik\":{\"count\":1,\"sum\":7,\"min\":7,\"max\":7,\"average\":7.0}}}"));
    }
}

