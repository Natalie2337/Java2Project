package com.example.java2project.Utils;

import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.DataCollections;
import com.example.java2project.pojo.Data.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Optional<DataCollections> getDataCollectionsFromRestApi(int page, String tag) {
        try {
            String API_KEY = "NHGvcuqdZu5z9TSJd9ppng((";
            // 定义API端点URL
            String apiUrl = String.format("https://api.stackexchange.com/2.3/questions" +
                    "?page=%d&order=desc&sort=votes&tagged=%s&site=stackoverflow&key=%s", page, tag, API_KEY);

            // 创建HttpClient实例
            HttpClient httpClient = HttpClients.createDefault();

            // 创建HttpGet请求
            HttpGet httpGet = new HttpGet(apiUrl);

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpGet);

            // 获取响应代码
            int statusCode = response.getStatusLine().getStatusCode();

            // 检查是否成功获取数据
            if (statusCode == 200) {
                // 从响应中获取JSON数据
                String jsonResponse = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                DataCollections myPartialEntity = objectMapper.readValue(jsonResponse, DataCollections.class);
                return Optional.ofNullable(myPartialEntity);
            } else {
                System.out.println("Failed to retrieve data. Response Code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // 过滤并保留异常
    public static List<Data> filterExceptions(List<Data> lines) {
        return filterByRegex(lines, "\\b(?i:" +
                "NullPointerException|IOException|" +
                "SQLException|RuntimeException|ArithmeticException|" +
                "IllegalArgumentException|IllegalStateException|NumberFormatException|" +
                "IndexOutOfBoundsException|NoSuchMethodException|UnsupportedOperationException|" +
                "NoSuchFieldException|ClassCastException|ClassNotFoundException|exception)\\b");
    }

    // 过滤并保留语法错误
    public static List<Data> filterSyntaxErrors(List<Data> lines) {
        return filterByRegex(lines, "\\b(?i:Syntax\\sError|Compilation\\sError|Compile\\sTime\\sError|cannot\\sresolve\\ssymbol|expected\\s\\S+|unexpected\\stoken|missing\\s\\S+|invalid\\smethod\\sdeclaration|';'\\sexpected|illegal\\sstart\\sof\\sexpression)\\b");
    }

    // 过滤并保留致命错误
    public static List<Data> filterFatalErrors(List<Data> lines) {
        return filterByRegex(lines, "\\b(?i:OutOfMemoryError|StackOverflowError|Fatal\\sError|NoClassDefFoundError|VirtualMachineError|ThreadDeath)\\b");
    }

    // 使用正则表达式过滤列表
    public static List<Data> filterByRegex(List<Data> lines, String regex) {
        List<Data> filteredLines = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        for (Data line : lines) {
            Matcher matcher = pattern.matcher(line.getTitle());
            if (matcher.find()) {
                filteredLines.add(line);
            }
        }
        return filteredLines;
    }


    /**
     *
     * @param lines 数据
     * @param regex 正则表达式匹配
     * @return 每个string出现的次数
     */
    public static Map<String, Integer> matchByRegex(List<Data> lines, String regex) {
        Map<String, Integer> match = new HashMap<>();
        Pattern pattern = Pattern.compile(regex);

        for (Data line : lines) {
            Matcher matcher = pattern.matcher(line.getTitle());
            if (matcher.find()) {
                String exp = matcher.group().toLowerCase();
                if (!exp.equals("exception")) {
                    match.put(exp, match.getOrDefault(exp, 0) + line.getView_count());
                }
            }
        }
        return match;
    }

}
