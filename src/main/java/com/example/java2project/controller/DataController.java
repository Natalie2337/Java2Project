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
import java.util.function.Function;

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

    @GetMapping("/experimental/barChartData")
    public Result experimentalBarChartData() {
        List<Tag> rst = dataService.getTagList();
        List<Function<String, Number>> abstractMethods = List.of(
                dataService::getAverageAnswerCountOfTag,
                dataService::getAverageViewCountOfTag,
                dataService::getTagProportion
        );
        List<List<Number>> rst2 = rst.stream()
                .map(tag -> abstractMethods.stream().map(m -> m.apply(tag.getTag_name())).toList())
                .toList();
        List<String> methodNames = List.of("BaseAnswer", "BaseView", "BaseTag");
        BarChartData data;
        data = new BarChartData();
        data.tagNames = rst.stream().map(Tag::getTag_name).toList();
        var f = flip(rst2);
        var f2 = f.stream().map(DataController::normalize).toList();
        f = f2;
        if (f.size() != methodNames.size()) {
            throw new RuntimeException();
        }
        List<BarChartData.HotEntry> l = new ArrayList<>();
        for (int i = 0; i < methodNames.size(); i++) {
            List<Number> l2 = f.get(i);
            String str = methodNames.get(i);
            BarChartData.HotEntry h = new BarChartData.HotEntry();
            h.hotRuleName = str;
            h.hotValues = l2;
            l.add(h);
        }
        data.tagValues = l;
        return Result.success(data);
    }

    public static List<Number> normalize(List<Number> src) {
        var minn = src.stream().mapToDouble(Number::doubleValue).min().getAsDouble();
        var maxn = src.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
        List<Number> l = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            double v = src.get(i).doubleValue();
            v -= minn;
            double division = maxn - minn;
            double rst = v / division;
            if (rst >= 0 && rst <= 1) {

            } else {
                rst = -1;
            }
            l.add((Double ) rst);
        }
        return l ;
    }

    public static <T> List<List<T>> flip(List<List<T>> src) {
        List<List<T>> rt = new ArrayList<>();
        int column = src.size();
        if (column == 0) {
            return src;
        }
        var first = src.get(0);
        int row = first.size();
        for (var i : src) {
            if (i.size() != row) {
                throw new RuntimeException();
            }
        }
        for (int i = 0; i < row; ++i) {
            List<T> b;
            rt.add(b = new ArrayList<>());
            for (int j = 0; j < column; ++j) {
                var idx = src.get(j).get(i);
                b.add(idx);
            }
        }
        return rt;
    }

    public static void main(String[] args) {
        int row = 2;
        int col = 3;
        List<List<Number>> values = new ArrayList<>();
        for (int i = 0; i < row; ++i) {
            List<Number> f;
            values.add(f = new ArrayList<>());
            for (int j = 0; j < col; ++j) {
                f.add(i + j * 2);
            }
        }
        System.out.println(values);
        var flip2 = flip(values);
        System.out.println(flip2);
    }

    static class BarChartData {
        public List<String> tagNames;
        static class HotEntry {
            public String hotRuleName;
            public List<Number> hotValues;
        }
        public List<HotEntry> tagValues;
    }

    //下面是三个热度指标

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
        if (true) {
            return Result.success(tag.length());
        }
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
    @GetMapping("/bugs/popularity/{errorName}")
    public List<Map<String, Object>> getBugPopularity(@PathVariable String errorName) {
        return null;
    }

    //restapi
    @GetMapping("/bugs/popularity")
    public List<Map<String, Object>> getTopNBugsPopularity(@RequestParam(name = "topN") Integer N) {
        return dataService.getTopNTopicsPopularity(N);
    }

    //restapi
    @GetMapping("/topics/related")
    public Map<String, Object> getRelatedTopics(@RequestParam(name = "input") String topicName) {
        Map<String, Object> map = new HashMap<>();
        map.put("popularity", dataService.getAverageViewCountOfTag(topicName));
        map.put("topic", topicName);
        return map;
    }



}
