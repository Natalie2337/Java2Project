package com.example.java2project.controller;

import com.example.java2project.Service.DataService;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/tag/view-count")
    public Result getAverageViewCountOfTag(String tag) {
        try {
            Integer res = dataService.getAverageViewCountOfTag(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }

    }

    @GetMapping("/tag/answer-count")
    public Result getAverageAnswerCountOfTag(String tag) {
        try {
            Integer res = dataService.getAverageAnswerCountOfTag(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    @GetMapping("/tag/proportion")
    public Result getTagProportion(String tag) {
        try {
            Double res = dataService.getTagProportion(tag);
            return Result.success(res);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    @GetMapping("/error/syntax-error")
    public Result getSyntaxErrorData() {
        try {
            Integer i = dataService.getViewCountOfSyntaxError();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }
    @GetMapping("/error/fatal-error")
    public Result getFatalErrorData() {
        try {
            Integer i = dataService.getViewCountOfFatalError();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }
    @GetMapping("/error/exceptions")
    public Result getExceptionData() {
        try {
            Integer i = dataService.getViewCountOfException();
            return Result.success(i);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }

    @GetMapping("/error/exceptions/runtime-exception")
    public Result getRuntimeExceptionData() {
        try {
            Map<String, Integer> map = dataService.getViewCountOfRuntimeException();
            return Result.success(map);
        } catch (Exception e) {
            return Result.error("exception occur");
        }
    }


}
