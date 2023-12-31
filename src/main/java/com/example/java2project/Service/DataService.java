package com.example.java2project.Service;

import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;

import java.util.List;
import java.util.Map;

public interface DataService {
    Integer getAverageViewCountOfTag(String tagName);
    Integer getAverageAnswerCountOfTag(String tagName);
    Double getTagProportion(String tagName);

    public Integer getTagCount(String tagName);

    Integer getTotalDataCount();
    public List<Tag> getTagList();
    public List<Data> getBugInformation();

    public List<Data> getFatalErrorData();
    public List<Data> getSyntaxErrorData();
    public List<Data> getExceptionData();
    public Integer getViewCountOfException();
    public Integer getViewCountOfSyntaxError();
    public Integer getViewCountOfFatalError();
    public Map<String, Integer> getViewCountOfRuntimeException();
    public List<Map<String, Object>> getTopNTopicsPopularity(int n);
    public List<Map<String, Object>> getTopNBugsPopularity(int n);



}
