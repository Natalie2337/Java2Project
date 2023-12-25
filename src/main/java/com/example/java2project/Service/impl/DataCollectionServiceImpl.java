package com.example.java2project.Service.impl;

import com.example.java2project.Service.DataCollectionService;
import com.example.java2project.Utils.Util;
import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.DataCollections;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {

    @Autowired
    private DataMapper dataMapper;

    public void collectData() {
        int count = 0;
        int page = 1;
        while (count < 1000) {
            DataCollections dataCollections = Util.getDataCollectionsFromRestApi(page);
            List<Data> items = dataCollections.getItems();
            items.forEach(i -> dataMapper.insert(i));
            count += dataCollections.getItems().size();
            if (!dataCollections.isHas_more()) {
                break;
            }

        }
    }
}