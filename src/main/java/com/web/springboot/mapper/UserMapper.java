package com.web.springboot.mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.springboot.entity.User;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user ")
    User findall();

    @Insert("INSERT INTO user(username,nickname,password,sex,phone,email,address,signature,birsday,work,habit,bucket,object) VALUES(#{username},#{nickname},#{password},#{sex},#{phone},#{email},#{address},#{signature},#{birsday},#{work},#{habit},#{bucket},#{object})")
    int insert(User user);

    @Update("UPDATE user SET username=#{username},nickname=if (#{nickname} IS NOT NULL, #{nickname}, nickname),password=if (#{password} IS NOT NULL, #{password}, password),sex=if (#{sex} IS NOT NULL, #{sex}, sex),phone=if (#{phone} IS NOT NULL, #{phone}, phone),email=if (#{email} IS NOT NULL, #{email}, email),address=if (#{address} IS NOT NULL, #{address}, address),signature=if (#{signature} IS NOT NULL, #{signature}, signature),birsday=if (#{birsday} IS NOT NULL, #{birsday}, birsday),work=if (#{work} IS NOT NULL, #{work}, work),habit=if (#{habit} IS NOT NULL, #{habit}, habit),bucket=if (#{bucket} IS NOT NULL, #{bucket}, bucket),object=if (#{object} IS NOT NULL, #{object}, object) WHERE id=#{id} ")
    int update(User user);

    @Delete("DELETE FROM user WHERE id=#{id}")
    Integer delete( @Param("id")  int id);

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
}
