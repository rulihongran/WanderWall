package com.web.springboot.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.springboot.entity.*;

import java.util.List;

import com.web.springboot.entity.vo.MapByProvinceVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user where user.id not in (SELECT f.friend_id from friendship as f where  f.username = #{username}) and user.username!= #{username}")
    List<User> findall(String username);

    @Insert("INSERT INTO user(username,nickname,password,sex,phone,email,address,signature,birsday,work,habit,bucket,object) VALUES(#{username},#{nickname},#{password},#{sex},#{phone},#{email},#{address},#{signature},#{birsday},#{work},#{habit},#{bucket},#{object})")
    int insert(User user);

    @Insert("INSERT INTO friendship (userid,username, friend_id, friend_username) VALUES (#{userid},#{username}, #{friend_id}, #{friend_username})")
    int addfriend(Friendship friendship);

    @Update("UPDATE user SET nickname=if (#{nickname} IS NOT NULL, #{nickname}, nickname),password=if (#{password} IS NOT NULL, #{password}, password),sex=if (#{sex} IS NOT NULL, #{sex}, sex),phone=if (#{phone} IS NOT NULL, #{phone}, phone),email=if (#{email} IS NOT NULL, #{email}, email),address=if (#{address} IS NOT NULL, #{address}, address),signature=if (#{signature} IS NOT NULL, #{signature}, signature),birsday=if (#{birsday} IS NOT NULL, #{birsday}, birsday),work=if (#{work} IS NOT NULL, #{work}, work),habit=if (#{habit} IS NOT NULL, #{habit}, habit),bucket=if (#{bucket} IS NOT NULL, #{bucket}, bucket),object=if (#{object} IS NOT NULL, #{object}, object) WHERE username=#{username} ")
    int update(User user);

    @Delete("DELETE FROM friendship WHERE username=#{username} and friend_id=#{id}")
    Integer delete( @Param("username") String username ,@Param("id")  int id);

    @Select("SELECT * FROM user limit #{page},#{size}")
    List<User> findpage(int page, int size);

    @Select("SELECT * FROM user where username = #{username} and password = #{password}")
    User login(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM user where username = #{username}")
    User Findusername(@Param("username") String username);

    @Select("SELECT * FROM user where id = #{id}")
    User getbyId(@Param("id") int id);

    @Select("select * from user where user.id in (SELECT f.friend_id from friendship as f where (f.friend_username = #{friend_usernameorid} or f.friend_id = #{friend_usernameorid}) and f.username = #{username} )  ")
    List<User> getfriendbyusernameorid( @Param("friend_usernameorid") String friend_usernameorid, @Param("username") String username);

    @Select("select * from user where user.id in (SELECT f.friend_id from friendship as f where f.username = #{username})  ")
    List<User> getfriends(@Param("username") String username);

    @Select("select distinct city from blog where username = #{username}")
    List<String> findcitys(String username);

    
    //new save -syb
    @Insert("INSERT INTO blog(id,cover,province,city,area,title,date,username) VALUES(#{id},#{cover},#{province},#{city},#{area},#{title},#{date},#{username})")
    @Options(useGeneratedKeys = true, keyProperty = "blog_id")
    int insert_blog(Blog blog);

    @Delete("DELETE FROM blog WHERE blog_id=#{blog_id}")
    Integer delete_blog( @Param("blog_id")  int blog_id);

    @Insert("INSERT INTO blog_content(Blog_id,type,content,url) VALUES(#{Blog_id},#{type},#{content},#{url})")
    @Options(useGeneratedKeys = true, keyProperty = "Paragraph_id")
    int insert_blog_content(Blog_content blog);

    @Delete("DELETE FROM blog_content WHERE Blog_id=#{Blog_id}")
    Integer delete_blog_content( @Param("Blog_id")  int Blog_id);

    //Insert
    @Insert("INSERT INTO gallery(username,dsc,src,isediting) VALUES(#{username},#{dsc},#{src},#{isediting})")
    int insert_gallery(Gallery gallery);

    //update
    @Update("UPDATE gallery SET dsc=if (#{dsc} IS NOT NULL, #{dsc}, dsc) WHERE (username=#{username} and src=#{src}) ")
    int update_gallery(Gallery gallery);

    @Delete("DELETE FROM gallery WHERE src=#{src}")
    Integer delete_gallery(String src);

    @Update("UPDATE blog SET cover=if (#{cover} IS NOT NULL, #{cover}, cover),id=if (#{id} IS NOT NULL, #{id}, id),username=if (#{username} IS NOT NULL, #{username}, username),province=if (#{province} IS NOT NULL, #{province}, province),city=if (#{city} IS NOT NULL, #{city}, city),area=if (#{area} IS NOT NULL, #{area}, area),title=if (#{title} IS NOT NULL, #{title}, title),date=if (#{date} IS NOT NULL, #{date}, date) WHERE blog_id=#{blog_id} ")
    int update_blog(Blog blog);
    //
    @Select("SELECT * FROM gallery where username = #{username}")
    List<Gallery> get_gallery_by_username(@Param("username") String username);
    //
    @Select("SELECT * FROM blog where blog_id = #{blog_id}")
    Blog get_blog_by_blog_id(Integer blog_id);

    @Select("SELECT * FROM blog where id = #{userId}")
    List<Blog> get_blog_by_user(Integer userId);

    @Select("SELECT * FROM blog_content where Blog_id = #{Blog_id}")
    List<Blog_content> get_blog_content(Integer Blog_id);

    @Select("<script>" +
            "SELECT city as name,COUNT(DISTINCT date) as value,COUNT(1) postCount FROM `blog` " +
            "WHERE id=#{userId} " +
            "<if test= \"province!=null and province!='' \">" +
            "   AND province LIKE CONCAT(#{province},'%') " +
            "   <if test= \"location!=null and location!='' \">" +
            "       AND city LIKE CONCAT('%',#{location},'%') " +
            "   </if>" +
            "</if>" +
            "<if test= \"province==null or province=='' \">" +
            "   <if test= \"location!=null and location!='' \">" +
            "       AND province LIKE CONCAT('%',#{location},'%') " +
            "   </if>" +
            "</if>" +
            "<if test= \"time!=null and time!='' \">" +
            "   AND date <![CDATA[ >= ]]> #{time}" +
            "</if>" +
            "GROUP BY city" +
            "</script>"
    )
    List<MapByProvinceVO> getMapByProvince(Integer userId, String province,String time,String location);

    @Select("<script>" +
            "SELECT province as name,COUNT(DISTINCT date) as value,COUNT(1) postCount,MAX(date) date FROM `blog` " +
            "WHERE id=#{userId} " +
            "<if test= \"location!=null and location!='' \">" +
            "   AND province LIKE CONCAT('%',#{location},'%') " +
            "</if>" +
            "<if test= \"time!=null and time!='' \">" +
            "   AND date <![CDATA[ >= ]]> #{time}" +
            "</if>" +
            "GROUP BY province ORDER BY date" +
            "</script>"
    )
    List<MapByProvinceVO> getMapByUser(Integer userId,String time,String location);

    @Select("SELECT * FROM friendship WHERE userid=#{userId};")
    List<Friendship> getFriendship(Integer userId);

    @Select("<script> SELECT city as name,date,COUNT(1) postCount FROM `blog` WHERE id=#{userId} AND date <![CDATA[ >= ]]> #{time} GROUP BY city,date ORDER BY postCount DESC LIMIT 1 </script>")
    MapByProvinceVO getReport(Integer userId,String time);
}

