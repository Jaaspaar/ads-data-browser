package com.example.adsclicks.services;

import com.example.adsclicks.loaders.AdsDataLoader;
import com.example.adsclicks.models.AdData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdsDataServiceTest {
    private AdsDataService adsDataService;
    private List<AdData> adsData;

    @Before
    public void setUp() {
        AdsDataLoader adsDataLoader = Mockito.mock(AdsDataLoader.class);
        adsData = Mockito.mock(List.class);
        Mockito.when(adsDataLoader.loadAdsData()).thenReturn(adsData);

        adsDataService = new AdsDataService(adsDataLoader);
    }

    @Test
    public void should_return_ads_data_list_when_getAdsData_is_called() {
        assertThat(adsDataService.getAdsData(), is(equalTo(adsData)));
    }
}
