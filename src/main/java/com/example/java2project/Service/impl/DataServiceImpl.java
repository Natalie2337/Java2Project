package com.example.java2project.Service.impl;

import com.example.java2project.Service.DataService;
import com.example.java2project.Utils.Util;
import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);

    @Autowired
    private DataMapper dataMapper;


    @Override
    public Integer getAverageViewCountOfTag(String tagName) {
        return dataMapper.getAverageViewCountOfTags(tagName);
    }

    @Override
    public Integer getAverageAnswerCountOfTag(String tagName) {
        return dataMapper.getAverageAnswerCountOfTags(tagName);
    }

    @Override
    public Double getTagProportion(String tagName) {
        Integer i = dataMapper.getTagCount(tagName);
//        logger.info(String.valueOf(i));
        Integer j = dataMapper.getTotalDataCount();
//        logger.info(String.valueOf(j));
        return (double) i / j;
    }

    @Override
    public Integer getTagCount(String tagName) {
        return dataMapper.getTagCount(tagName);
    }

    @Override
    public List<Tag> getTagList() {
        return dataMapper.getTags();
    }

    @Override
    public List<Data> getBugInformation() {
        return dataMapper.getBugInformation();
    }

    @Override
    public List<Data> getFatalErrorData() {
        List<Data> bugInformation = getBugInformation();
        return Util.filterFatalErrors(bugInformation);
    }

    @Override
    public List<Data> getSyntaxErrorData() {
        List<Data> bugInformation = getBugInformation();
        return Util.filterSyntaxErrors(bugInformation);
    }

    @Override
    public List<Data> getExceptionData() {
        List<Data> bugInformation = getBugInformation();
        return Util.filterExceptions(bugInformation);
    }

    @Override
    public Integer getViewCountOfException() {
        List<Data> data = getExceptionData();
        return data.stream().mapToInt(Data::getView_count).sum();
    }

    @Override
    public Integer getViewCountOfSyntaxError() {
        List<Data> data = getSyntaxErrorData();
        return data.stream().mapToInt(Data::getView_count).sum();
    }

    @Override
    public Integer getViewCountOfFatalError() {
        List<Data> data = getFatalErrorData();
        return data.stream().mapToInt(Data::getView_count).sum();
    }

    @Override
    public Map<String, Integer> getViewCountOfRuntimeException() {
        Map<String, Integer> res = new HashMap<>();
        List<Data> exceptionData = getExceptionData();
        List<String> ss = Arrays.asList("NullPointerException",
                "NumberFormatException", "UnsupportedOperationException", "IllegalArgumentException",
                "ClassCastException", "ArithmeticException", "IllegalStateException"
        );
        for (String s: ss) {
            String regex = String.format("\\b(?:%s)\\b", s);
            List<Data> e = Util.filterByRegex(exceptionData, regex);
            res.put(s, e.stream().mapToInt(Data::getView_count).sum());
        }
        return res;
    }


    @Override
    public Integer getTotalDataCount() {
        return dataMapper.getTotalDataCount();
    }


}
