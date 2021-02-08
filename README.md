# Ads Data Browser

Application exposes generic endpoint to query ads data.

### Exemplary instance
An exemplary instance is hosted at Google Cloud.

### Swagger UI
Swagger UI can be accessed at https://ads-data-browser.ew.r.appspot.com/swagger-ui/#/ads-data-controller/getMetricUsingGET

### Usage examples

To get information about total clicks (alongside other aggregated metrics) for given data source and date range

https://ads-data-browser.ew.r.appspot.com/ads/CLICKS/?dataSource=Google%20Ads&endDate=11-22-20&groupingType=DATA_SOURCE&startDate=11-22-18

To get ads Click-Through Rate per data source and campaign 

https://ads-data-browser.ew.r.appspot.com/ads/CTR/?groupingType=DATA_SOURCE_THAN_CAMPAIGN

To get impressions over time (daily)

https://ads-data-browser.ew.r.appspot.com/ads/IMPRESSIONS/?groupingType=DATE

### Install
```mvn install```

### Run
```mvn spring-boot:run```

### TODO
1. Research library for in memory objects querying
2. Extend endpoint by providing option to define aggregation function type rather than 
   computing result of all aggregation functions for every call
   
3. Fix date formatting in output to exclude time part
   
4. Add custom error page
4. Improve code documentation (Javadoc) and swagger documentation