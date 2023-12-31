package com.example.java2project.controller;

import com.example.java2project.Service.DataService;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;
import com.example.java2project.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 获取所有的tag
     * @return tag数组
     */
    @GetMapping("/tags")
    public Result getTags() {
        try {
            List<Tag> res = dataService.getTagList();
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * 计算tags的平均view-count
     *
     * @return 平均view-count
     */
    @GetMapping("/tags/view-count")
    public Result getAverageViewCountOfTag() {
        try {
            Map<String, Integer> res = new HashMap<>();
            List<Tag> tags = dataService.getTagList();
            tags.stream().forEach(tag -> {
                res.put(tag.getTag_name(), dataService.getAverageViewCountOfTag(tag.getTag_name()));
            });
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * 计算tag的平均answer-count
     *
     * @return 平均answer-count
     */
    @GetMapping("/tags/answer-count")
    public Result getAverageAnswerCountOfTag() {
        try {
            Map<String, Integer> res = new HashMap<>();
            List<Tag> tags = dataService.getTagList();
            tags.stream().forEach(tag -> {
                res.put(tag.getTag_name(), dataService.getAverageAnswerCountOfTag(tag.getTag_name()));
            });

            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }


    /**
     * @return tag在所有question中的比例
     */
    @GetMapping("/tags/proportion")
    public Result getTagProportion() {
        try {
            Map<String, Double> res = new HashMap<>();
            List<Tag> tags = dataService.getTagList();
            tags.stream().forEach(tag -> {
                res.put(tag.getTag_name(), dataService.getTagProportion(tag.getTag_name()));
            });
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }


    /**
     * 计算tag的平均view-count
     *
     * @param tag tag的名字
     * @return 平均view-count
     */
    @GetMapping("/tag/view-count")
    public Result getAverageViewCountOfTag(String tag) {
        try {
            Integer res = dataService.getAverageViewCountOfTag(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * 计算tag的平均answer-count
     *
     * @param tag tag的名字
     * @return 平均answer-count
     */
    @GetMapping("/tag/answer-count")
    public Result getAverageAnswerCountOfTag(String tag) {
        try {
            Integer res = dataService.getAverageAnswerCountOfTag(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }


    /**
     * @param tag tag的名字
     * @return tag在所有question中的比例
     */
    @GetMapping("/tag/proportion")
    public Result getTagProportion(String tag) {
        try {
            Double res = dataService.getTagProportion(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * @return syntax_error的view_count
     */
    @GetMapping("/error/syntax-error")
    public Result getSyntaxErrorData() {
        try {
            Integer i = dataService.getViewCountOfSyntaxError();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * @return fatal_error的view_count
     */
    @GetMapping("/error/fatal-error")
    public Result getFatalErrorData() {
        try {
            Integer i = dataService.getViewCountOfFatalError();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     * @return exception的view_count
     */
    @GetMapping("/error/exceptions")
    public Result getExceptionData() {
        try {
            Integer i = dataService.getViewCountOfException();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    /**
     *
     * @return RuntimeException的view_count
     */
    @GetMapping("/error/exceptions/runtime-exception")
    public Result getRuntimeExceptionData() {
        try {
            Map<String, Integer> map = dataService.getViewCountOfRuntimeException();
            return Result.success(map);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    // restapi
    @GetMapping("/topics/popularity/{topicName}")
    public Map<String, Object> getTopicPopularity(@PathVariable String topicName) {
        Map<String, Object> map = new HashMap<>();
        map.put("popularity", dataService.getAverageViewCountOfTag(topicName));
        map.put("topic", topicName);
        return map;
    }

    //restapi
    @GetMapping("/topics/popularity")
    public List<Map<String, Object>> getTopNTopicsPopularity(@RequestParam(name = "topN") Integer N) {
        return dataService.getTopNTopicsPopularity(N);
    }

    //restapi
    @GetMapping("/bugs/popularity/{bugName}")
    public Map<String, Object> getBugPopularity(@PathVariable String bugName) {
        Map<String, Object> map = new HashMap<>();
        map.put("popularity", dataService.getBugPopularity(bugName));
        map.put("topic", bugName);
        return map;
    }

    //restapi
    @GetMapping("/bugs/popularity")
    public List<Map<String, Object>> getTopNBugsPopularity(@RequestParam(name = "topN") Integer N) {
        return dataService.getTopNBugsPopularity(N);
    }

    //restapi
    @GetMapping("/topics/related")
    public Map<String, Object> getRelatedTopics(@RequestParam(name = "input") String topicName) {
        Map<String, Object> map = new HashMap<>();
        map.put("intimacy", dataService.getAverageViewCountOfTag(topicName));
        map.put("topic", topicName);
        return map;
    }



}
