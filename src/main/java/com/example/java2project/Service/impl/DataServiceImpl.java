package com.example.java2project.Service.impl;

import com.example.java2project.Service.DataService;
import com.example.java2project.Utils.Util;
import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            String regex = String.format("\\b(?i:%s)\\b", s);
            List<Data> e = Util.filterByRegex(exceptionData, regex);
            res.put(s, e.stream().mapToInt(Data::getView_count).sum());
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTopNTopicsPopularity(int n) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Tag> tagList = dataMapper.getTags();
        tagList.forEach(tag -> {
            tag.setTag_id(dataMapper.getAverageViewCountOfTags(tag.getTag_name()));
        });
        PriorityQueue<Tag> maxHeap = new PriorityQueue<>((Tag t1, Tag t2) -> t2.getTag_id() - t1.getTag_id());
        maxHeap.addAll(tagList);
        for (int i = 0; i < n; i++) {
            Tag t = maxHeap.poll();
            Map<String, Object> map = new HashMap<>();
            map.put("popularity", t.getTag_id());
            map.put("topic", t.getTag_name());
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTopNBugsPopularity(int n) {
        List<Data> bugData = getBugInformation();
        String regex = "\\b(?i:" +
                "NullPointerException|IOException|" +
                "SQLException|RuntimeException|ArithmeticException|" +
                "IllegalArgumentException|IllegalStateException|NumberFormatException|" +
                "IndexOutOfBoundsException|NoSuchMethodException|UnsupportedOperationException|" +
                "NoSuchFieldException|ClassCastException|ClassNotFoundException|" +
                "Syntax\\sError|Compilation\\sError|" +
                "Compile\\sTime\\sError|cannot\\sresolve\\ssymbol|" +
                "expected\\s\\S+|unexpected\\stoken|missing\\s\\S+|invalid\\smethod\\sdeclaration|" +
                "';'\\sexpected|illegal\\sstart\\sof\\sexpression|" +
                "OutOfMemoryError|StackOverflowError|Fatal\\sError|" +
                "NoClassDefFoundError|VirtualMachineError|ThreadDeath|" +
                "exception)\\b";

        List<Data> e = Util.filterByRegex(bugData, regex);
        Map<String, Integer> bugs = Util.matchByRegex(e, regex);

        List<Map.Entry<String, Integer>> topNEntries = bugs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())  // 根据值降序排序
                .limit(n)  // 限制为前n个元素
                .collect(Collectors.toList());


        List<Map<String, Object>> res = new ArrayList<>();
        topNEntries.forEach( stringIntegerEntry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("error", stringIntegerEntry.getKey());
            map.put("popularity", stringIntegerEntry.getValue());
            res.add(map);
        });
        return res;
    }

    @Override
    public Integer getBugPopularity(String bugName) {
        List<Data> bugData = getBugInformation();
        String regex = String.format("\\b(?i:%s)\\b", bugName);
        List<Data> e = Util.filterByRegex(bugData, regex);
        return e.stream().mapToInt(Data::getView_count).sum();
    }

    @Override
    public List<Map<String, Object>> getRelatedTopics(String topicName) {
        return null;
    }


    @Override
    public Integer getTotalDataCount() {
        return dataMapper.getTotalDataCount();
    }


}
