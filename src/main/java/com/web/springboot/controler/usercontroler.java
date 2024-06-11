package com.web.springboot.controler;
import com.web.springboot.entity.User;
import com.web.springboot.entity.Friendship;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.web.springboot.common.Constants;
import com.web.springboot.common.result;
import com.web.springboot.controler.dto.userdto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import com.web.springboot.mapper.UserMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.web.springboot.service.MinIOService;
import com.web.springboot.service.userservice;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.units.qual.s;
import org.springframework.http.MediaType;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.SetBucketPolicyArgs;
import io.minio.http.Method;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/user")
public class usercontroler {

   

    @Autowired
    private userservice userservice;

    @Resource
    private MinioClient minioClient;

    @Value("${files.upload.path}")
    private String filesUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @PostMapping
    public Integer save(@RequestBody User user) {
       return  userservice.save(user);
    }

    @PostMapping("/login")
    public result login(@RequestBody userdto user) throws Exception{
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return result.error(Constants.CODE_400, "参数错误");
        }
        userdto dto = userservice.login(user);
        {
            String bucketName = "userinfo";
            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isBucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("bucket已经存在，不需要创建.");
            }
            String policyJsonString = "{\"Version\" : \"2012-10-17\",\"Statement\":[{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
        //创建存储桶的时候，设置该存储桶里面的文件的访问策略，运行公开的读；
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policyJsonString)//json串，里面是访问策略
                .build());
        }
        return result.success(dto);
    }

    @PostMapping("/upload/background")
    public result uploadbackground(@RequestParam ("file") MultipartFile  file,@RequestParam("username") String username) throws Exception {
         String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        String url;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile);
        url = "/file/" + fileUUID;
        userservice.uploadbackground(filesUploadPath + fileUUID,"userinfo",username+StrUtil.DOT + type);
        return result.success(url);
    }

    @PostMapping("/upload/avatar")
    public result uploadavatar(@RequestParam ("file") MultipartFile  file,@RequestParam("username") String username) throws Exception {
         String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID()+"_"+"avatar"+ StrUtil.DOT + type;
        String url;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile);
        url = "/file/" + fileUUID;
        userservice.uploadbackground(filesUploadPath + fileUUID,"userinfo",username+"_"+"avatar"+StrUtil.DOT + type);
        return result.success(url);
    }

    @PostMapping("/update/userinfo")
    public result updateuserinfo(@RequestBody User user) throws Exception {
        System.out.println(user);
        userservice.updateuserinfo(user);
        return result.success();
    }


    @RequestMapping(value = "/download/background",method = RequestMethod.POST,produces = MediaType.IMAGE_PNG_VALUE)
    private String getPic(@RequestParam("username") String username) throws Exception {
    username=userservice.downloadbackground("userinfo",username,filesUploadPath);
        if(username.isEmpty())
        {
            return null;
        }
        FileInputStream picInput = new FileInputStream(filesUploadPath+username);

         String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("userinfo")
                .object(username)
                .expiry(3, TimeUnit.MINUTES)
                .method(Method.GET)
                .build());
       
        return presignedObjectUrl;
        //return picInput.readAllBytes();
    }

    @PostMapping("/register")
    public result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return result.error(Constants.CODE_400, "参数错误");
        }
        return result.success(userservice.register(user));
    }

    @RequestMapping
    public User findAll() {
        return userservice.findall();
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) 
    {
        return userservice.delete(id);          
    }

    @RequestMapping("/page")
    public List<User> findpage(@RequestParam(value = "page", defaultValue = "1") Integer page,
    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userservice.findpage( page,size);
    }

    @GetMapping("/{username}")
    public User findbyusername(@PathVariable String username) {
        return userservice.getbyusername(username);
    }

    @PostMapping("/friend/searchfriend")
    public result  searchfriend(@RequestParam("usernameorid") String usernameorid,@RequestParam("username") String username) throws Exception {
        if (StrUtil.isBlank(usernameorid)) {
            return result.error(Constants.CODE_400, "参数错误");
        }
        List<User> list = userservice.searchfriend(usernameorid,username);
        return result.success(list);
    }

    @PostMapping("/friend/getfriend")
    public result  getfriends(@RequestParam("username") String username) throws Exception {
        if (StrUtil.isBlank(username)) {
            return result.error(Constants.CODE_400, "参数错误");
        }    
        return result.success(userservice.getfriends(username));
    }
}
