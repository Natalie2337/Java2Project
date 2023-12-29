package com.example.java2project.mapper;

import com.example.java2project.pojo.Data.Data;
import com.example.java2project.pojo.Data.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {
    @Insert("insert into questions (id, title, view_count, answer_count, score) " +
            "values (#{question_id}, #{title}, #{view_count}, #{answer_count}, #{score})")
    public void insertData(Data data);

    @Insert("insert into question_tags (question_id, tag_id)" +
            "VALUES (#{question_id}, #{tag_id})")
    public void insertDataTags(String question_id, int tag_id);

    @Insert("insert into bugs (id, title, view_count, answer_count, tag_id) " +
            "values (#{question_id}, #{title}, #{view_count}, #{answer_count}, 4)")
    public void insertBugData(Data data);

    @Select("select * from tags")
    public List<Tag> getTags();

    @Select("""
            select round(avg(view_count)) from
                (SELECT
                     q.*, t.*
                 FROM
                     questions q
                         JOIN
                     question_tags qt ON q.id = qt.question_id
                         JOIN
                     tags t ON qt.tag_id = t.tag_id) as main
            where tag_name = #{tag_name};
            """)
    public Integer getAverageViewCountOfTags(String tag_name);

    @Select("""
            select round(avg(answer_count)) from
            (SELECT
                q.*, t.*
            FROM
                questions q
                    JOIN
                question_tags qt ON q.id = qt.question_id
                    JOIN
                tags t ON qt.tag_id = t.tag_id) as main
            where tag_name = #{tag_name};
            """)
    public Integer getAverageAnswerCountOfTags(String tag_name);

    @Select("""
            select c from
            (select t.tag_id, c, tag_name from (select tag_id, count(*) as c from question_tags group by tag_id order by tag_id) t
            join tags on t.tag_id = tags.tag_id) as tag_count
            where tag_name = #{tag_name};
            """)
    public Integer getTagCount(String tag_name);

    @Select("select count(*) from questions")
    public Integer getTotalDataCount();

    @Select("""
            select * from bugs
            """)
    public List<Data> getBugInformation();
}
