package com.example.java2project;

import com.example.java2project.Service.DataCollectionService;
import com.example.java2project.controller.DataController;
import com.example.java2project.mapper.DataMapper;
import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Java2ProjectApplicationTests {

    @Autowired
    private DataController controller;

    @Autowired
    private DataCollectionService dataCollectionService;

    @Autowired
    private DataMapper dataMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetTags() {
        List<Tag> list = dataMapper.getTags();
        list.forEach(System.out::println);
    }

    @Test
    void testInsertData() {

    }

    @Test
    void testGetTagDataProportions() {
        String tagName = "exception";

    }

}
