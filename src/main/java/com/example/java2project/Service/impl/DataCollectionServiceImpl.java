package com.example.java2project.Service.impl;

import com.example.java2project.Service.DataCollectionService;
import com.example.java2project.Utils.Util;
import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.DataCollections;
import com.example.java2project.pojo.Data.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);

    @Autowired
    private DataMapper dataMapper;

    @Override
    @Transactional
    public void collectData(String tagName) {
        AtomicInteger count = new AtomicInteger();
        int page = 1;
        List<Tag> tags = dataMapper.getTags();
        Map<String, Integer> map = tags.stream()
                .collect(Collectors.toMap(Tag::getTag_name, Tag::getTag_id));

        while (count.get() < 1000) {
            Optional<DataCollections> dataCollections = Util.getDataCollectionsFromRestApi(page, tagName);
            if (dataCollections.isPresent()){
                DataCollections d = dataCollections.get();
                List<Data> items = d.getItems();
                items.forEach(i -> {
                    List<String> javaTags = i.getTags();
                    try {
                        dataMapper.insertData(i);
                        javaTags.forEach(tag -> {
                            if (map.containsKey(tag)) {
                                dataMapper.insertDataTags(i.getQuestion_id(), map.get(tag));
                            }
                        });
                    } catch (Exception e) {
                        logger.error("database error occur", e);
                    }

                });
                count.addAndGet(d.getItems().size());
                if (!d.isHas_more()) {
                    break;
                }
            }
            logger.info(String.valueOf(page));
            page++;
        }
    }

    @Override
    @Transactional
    public void collectBug(String tagName) {
        AtomicInteger count = new AtomicInteger();
        int page = 1;
        List<Tag> tags = dataMapper.getTags();
        Map<String, Integer> map = tags.stream()
                .collect(Collectors.toMap(Tag::getTag_name, Tag::getTag_id));

        while (count.get() < 300) {
            Optional<DataCollections> dataCollections = Util.getDataCollectionsFromRestApi(page, tagName);
            if (dataCollections.isPresent()){
                DataCollections d = dataCollections.get();
                List<Data> items = d.getItems();
                items.forEach(i -> {
                    List<String> javaTags = i.getTags();
                    try {
                        dataMapper.insertBugData(i);
                    } catch (Exception e) {
                        logger.error("database error occur", e);
                    }

                });
                count.addAndGet(d.getItems().size());
                if (!d.isHas_more()) {
                    break;
                }
            }
            logger.info(String.valueOf(page));
            page++;
        }
    }
}