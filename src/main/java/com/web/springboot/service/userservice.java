package com.web.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
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

    public User findall()
    {
        return usermapper.findall();
    }

    public int delete(Integer id)
    {
      return usermapper.delete(id);
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

}
