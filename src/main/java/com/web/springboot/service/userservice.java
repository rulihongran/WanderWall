package com.web.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import cn.hutool.core.bean.BeanUtil;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.springboot.mapper.UserMapper;
import com.web.springboot.utils.TokenUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import io.minio.*;
import io.minio.messages.Item;
import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.springboot.common.Constants;
import com.web.springboot.controler.dto.userdto;
import com.web.springboot.entity.Friendship;
import com.web.springboot.entity.User;
import com.web.springboot.entity.graphLinksArray;
import com.web.springboot.entity.graphNodesArray;
import com.web.springboot.exception.ServiceException;

@Service
public class userservice {
    
    @Resource
    private MinioClient minioClient;

    @Autowired
    private UserMapper usermapper;

    public int save(User user)
    {
        if (user.getId() == null) 
        {
         return usermapper.insert(user);
       } 
       else {
         return usermapper.update(user);
     }
    }

    public List<User> findall(String username)
    {
        return usermapper.findall(username);
    }

    public List<User> findpage(Integer page, Integer size)
    {
        return usermapper.findpage(page, size);
    }


    public userdto login(userdto user)
    {
        // 用户密码 md5加密
        user.setPassword(SecureUtil.md5(user.getPassword()));
        User one = usermapper.login(user.getUsername(), user.getPassword());
        if (one != null) {
            BeanUtil.copyProperties(one, user, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            user.setToken(token);

            //String role = one.getRole(); // ROLE_ADMIN
            //设置用户的菜单列表
            //List<Menu> roleMenus = getRoleMenus(role);
            //user.setMenus(roleMenus);
            return user;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }


    public User register(User user) {
        // 用户密码 md5加密
        user.setPassword(SecureUtil.md5(user.getPassword()));
        User one = usermapper.Findusername(user.getUsername());
        if (one == null) {
            // one = new User();
            // BeanUtil.copyProperties(user, one, true);
            // // 默认一个普通用户的角色
            // one.setRole(RoleEnum.ROLE_STUDENT.toString());
            // if (one.getNickname() == null) {
            //     one.setNickname(one.getUsername());
            // }
            usermapper.insert(user);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    public User getbyId(Integer id)
    {
        return usermapper.getbyId(id);
    }

    public User getbyusername(String username)
    {
        return usermapper.Findusername(username);
    }

    public ObjectWriteResponse uploadbackground (String fileurl,String bucketName,String objectName)throws Exception
    {
        File file = new File(fileurl);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .stream(new FileInputStream(file), file.length(), -1)
        .build()
      );
        return objectWriteResponse;
    }

    public ObjectWriteResponse uploadavatar (String fileurl,String bucketName,String objectName)throws Exception
    {
        File file = new File(fileurl);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .stream(new FileInputStream(file), file.length(), -1)
        .build()
      );
        return objectWriteResponse;
    }

    public String downloadbackground (String bucketName,String objectName,String filesUploadPath)throws Exception
    {
         Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .build());
        AtomicReference<String> name = new AtomicReference<>("");
        listObjects.forEach( itemResult -> {
            try {
                Item item = itemResult.get();
                if (item.objectName().contains(objectName)) {
                    name.set(item.objectName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if(name.get()!=null)
        {
        File file = new File(filesUploadPath + "/" + name.get());
        if (!file.exists()) {
        minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(name.get())
                .filename(filesUploadPath + "/" + name.get())
                .build());
        }
       }
        return name.get();
    }

    public List<User> searchfriend (String usernameorid,String username)throws Exception
    {
        return usermapper.getfriendbyusernameorid(usernameorid,username);
    }

    public List<User> getfriends (String username)throws Exception
    {
        return usermapper.getfriends(username);
    }

    public void updateuserinfo (User user)throws Exception
    {
        usermapper.update(user);
    }
    
    public void deletefriend (String username,Integer friendid)throws Exception
    {
       usermapper.delete(username,friendid);  
    }

    public void addfriend (String username,Integer friendid,String friendname)throws Exception
    {
        Friendship friendship = new Friendship();
        friendship.setUsername(username);
        friendship.setFriend_id(friendid);;
        friendship.setFriend_username(friendname);
        usermapper.addfriend(friendship);
    }

    public Map<String,Object> friendrela(String username)
    {
        Map<String,graphNodesArray> m1= new HashMap<>();
        List<graphLinksArray> m2= new ArrayList<>();
        graphNodesArray g1 = new graphNodesArray();
        g1.setName(username); 
        g1.setType("User");
        m1.put(username,g1);
        
        // 使用普通循环代替 forEach
        List<String>cityList= new ArrayList<>();
        List<User> friendList = usermapper.getfriends(username);
        for (User user : friendList) {
            graphNodesArray g2 = new graphNodesArray();
            g2.setName(user.getUsername()); 
            g2.setType("Friend" + user.getId());
            m1.put(user.getUsername(), g2);
        }
        for (graphNodesArray value : m1.values()) {  
            List<String> citys = usermapper.findcitys(value.getName());
            for (String city : citys) {
                if (!cityList.contains(city)) {
                    cityList.add(city);
                }
                graphLinksArray g3 = new graphLinksArray();
                g3.setSource(value.getName());
                g3.setTarget(city);
                g3.setRela("去过");
                m2.add(g3);
            }
            }
        for (String city : cityList) {
            graphNodesArray g4 = new graphNodesArray();
            g4.setName(city);
            g4.setType("City");
            m1.put(city, g4);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("nodes", m1);
        map.put("links", m2);
        return map;
    } 
}
