package com.web.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;

import com.web.springboot.common.MapCpConstants;
import com.web.springboot.entity.*;
import com.web.springboot.entity.vo.MapByProvinceVO;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void addfriend (Integer userid,String username,Integer friendid,String friendname)throws Exception
    {
        Friendship friendship = new Friendship();
        friendship.setUsername(username);
        friendship.setUserid(userid);
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
                if (StringUtils.isBlank(city)){
                    continue;
                }
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
            if (StringUtils.isBlank(city)){
                continue;
            }
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
    //syb
    public ObjectWriteResponse upload_blog_pic(String file_url, String bucketName, String objectName)throws Exception
    {
        File file = new File(file_url);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new FileInputStream(file), file.length(), -1)
                .build()
        );
        return objectWriteResponse;
    }

    //syb
    public ObjectWriteResponse upload_gallery_pic(String file_url, String bucketName, String objectName)throws Exception
    {
        File file = new File(file_url);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new FileInputStream(file), file.length(), -1)
                .build()
        );
        return objectWriteResponse;
    }

    //syb
    public ObjectWriteResponse upload_blog_cover_pic(String file_url, String bucketName, String objectName)throws Exception
    {
        File file = new File(file_url);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new FileInputStream(file), file.length(), -1)
                .build()
        );
        return objectWriteResponse;
    }

    //syb
    @Transactional
    public int insert_blog(Blog blog)
    {
        //如果是直辖市，第二级也存第三级
        if (MapCpConstants.city.contains(blog.getProvince())){
            blog.setCity(blog.getArea());
        }
        int i = usermapper.insert_blog(blog);
        //保存内容
        List<Blog_content> paragh = blog.getParagh();
        for (Blog_content blog_content : paragh) {
            blog_content.setBlog_id(blog.getBlog_id());
            usermapper.insert_blog_content(blog_content);
        }
        return i;
    }

    //syb
    public int save_gallery(Gallery gallery)
    {
        // if (gallery.getSrc() == null) {
        return usermapper.insert_gallery(gallery);
        // } else{
        //     return usermapper.update_gallery(gallery);
        // }
        
    }

    public int deleteGallery(String src)
    {
         if (src == null) {
             return 0;
         }
        return usermapper.delete_gallery(src);
    }

    //syb
    @Transactional
    public int update_blog(Blog blog)
    {
        if (blog.getBlog_id()==null)
            return 0;
        //保存内容，先删再存
        usermapper.delete_blog_content(blog.getBlog_id());
        List<Blog_content> paragh = blog.getParagh();
        for (Blog_content blog_content : paragh) {
            blog_content.setBlog_id(blog.getBlog_id());
            usermapper.insert_blog_content(blog_content);
        }
        //如果是直辖市，第二级也存第三级
        if (MapCpConstants.city.contains(blog.getProvince())){
            blog.setCity(blog.getArea());
        }

        return usermapper.update_blog(blog);
    }

    @Transactional
    public int delete_blog(Integer id)
    {
        if (id==null)
            return 0;
        usermapper.delete_blog_content(id);
        return usermapper.delete_blog(id);
    }

    public String download_gallery(String bucketName,String objectName,String filesUploadPath) throws Exception {
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
    //syb
    public List<Gallery> get_gallery_by_username(String username) {
        return usermapper.get_gallery_by_username(username);
    }
    //syb
    public Blog get_blog_by_blog_id(Integer blog_id) {
        Blog blog = usermapper.get_blog_by_blog_id(blog_id);
        if (blog!=null){
            List<Blog_content> blog_content = usermapper.get_blog_content(blog_id);
            blog.setParagh(blog_content);
        }
        return blog;
    }
    public List<Blog> get_blog_by_user(Integer userId) {
        List<Blog> blog_by_user = usermapper.get_blog_by_user(userId);
        for (Blog blog : blog_by_user) {
            List<Blog_content> blog_content = usermapper.get_blog_content(blog.getBlog_id());
            String collect = blog_content.stream().map(Blog_content::getContent).collect(Collectors.joining("；"));
            blog.setContent(collect);
        }
        return blog_by_user;
    }

    public List<MapByProvinceVO> getMapByProvince(Integer userId, String province,String time,String location) {
        return usermapper.getMapByProvince(userId,province,time,location);
    }

    public List<MapByProvinceVO> getMapByUser(Integer userId,String time,String location) {
        return usermapper.getMapByUser(userId,time,location);
    }

    public List<Friendship> getFriendship(Integer userId) {
        return usermapper.getFriendship(userId);
    }

    public MapByProvinceVO getReport(Integer userId,String time){
        return usermapper.getReport(userId,time);
    }
}
