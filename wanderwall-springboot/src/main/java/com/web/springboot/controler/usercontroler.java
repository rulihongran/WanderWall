package com.web.springboot.controler;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.web.springboot.common.MapCpConstants;
import com.web.springboot.entity.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.web.springboot.common.Constants;
import com.web.springboot.common.result;
import com.web.springboot.controler.dto.userdto;


import com.web.springboot.entity.vo.MapByProvinceVO;
import com.web.springboot.entity.vo.ScatterDataVO;
import com.web.springboot.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.web.springboot.service.userservice;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.SetBucketPolicyArgs;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/user")
public class usercontroler {

    @Autowired
    private userservice userservice;

    //syb
    @Value("${minio.endpoint}")
    private String endpoint;

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
        userservice.uploadbackground(filesUploadPath + fileUUID,"userinfo",username+"_"+"background"+StrUtil.DOT + type);
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
        
        userservice.uploadbackground(filesUploadPath + fileUUID,"userinfo",username+"_"+"avatar"+StrUtil.DOT + type);
        return result.success(username+"_"+"avatar"+ StrUtil.DOT + type);
    }

    @PostMapping("/update/userinfo")
    public result updateuserinfo(@RequestBody User user) throws Exception {
        userservice.updateuserinfo(user);
        return result.success();
    }


    @RequestMapping(value = "/download/background",method = RequestMethod.POST,produces = MediaType.IMAGE_PNG_VALUE)
    private String getPic(@RequestParam("username") String username) throws Exception {
    username=userservice.downloadbackground("userinfo",username+"_"+"background",filesUploadPath);
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


    @RequestMapping(value = "/download/avatar",method = RequestMethod.POST,produces = MediaType.IMAGE_PNG_VALUE)
    private String getavatar(@RequestParam("bucket") String bucket, @RequestParam("object") String object) throws Exception {
       
        //FileInputStream picInput = new FileInputStream(filesUploadPath+username);
        String presignedObjectUrl=null;
        Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .build());
        AtomicReference<String> name = new AtomicReference<>("");
        listObjects.forEach( itemResult -> {
            try {
                Item item = itemResult.get();
                if (item.objectName().contains(object)) {
                    name.set(item.objectName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if(name.get()!=null)
        {
             presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
            .bucket("userinfo")
            .object(object)
            .expiry(3, TimeUnit.MINUTES)
            .method(Method.GET)
            .build());
       }

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

    @RequestMapping("/findAll")
    public result findAll(@RequestParam String username) throws Exception{
        return result.success(userservice.findall(username));
    }


    @PostMapping("/deletefriend")
    public result deletefriend(@RequestParam String username, @RequestParam Integer fiendid) throws Exception {
        userservice.deletefriend(username, fiendid);
        return result.success();
    }

    @PostMapping("/addfriend")
    public result addfriend(@RequestParam String username, @RequestParam Integer fiendid, @RequestParam String friendname) throws Exception {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser==null){
            return result.error();
        }
        userservice.addfriend(currentUser.getId(),currentUser.getUsername(), fiendid,friendname);
        return result.success();
    }

    @PostMapping("/friend/rela")
    public result friendrela(@RequestParam String username) throws Exception {
        Map<String,Object>map=userservice.friendrela(username);
        return result.success(map);
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

    //syb
    @PostMapping("/upload/blog_pic")
    public result upload_blog_pic(@RequestParam ("file") MultipartFile  file,@RequestParam("username") String username) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID()+"_"+"blog_pic"+ StrUtil.DOT + type;
        String url;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile);
        userservice.upload_blog_pic(filesUploadPath + fileUUID,"userinfo",username+"_"+"blog_pic"+StrUtil.DOT + type);
        return result.success();
    }

    //syb
    @PostMapping("/upload/gallery_pic")
    public result gallery(@RequestParam ("file") MultipartFile  file,@RequestParam("username") String username,@RequestParam("filename") String filename) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        // System.out.println(username);
        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + "_"+"gallery_pic" + StrUtil.DOT + type;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile);
        userservice.upload_gallery_pic(filesUploadPath + fileUUID,"userinfo",filename + "_"+"gallery_pic" + StrUtil.DOT + type);
        //need repairing
        return result.success(endpoint + "/userinfo/" + filename + "_" + "gallery_pic" + StrUtil.DOT + type);
    }
    //syb
    @PostMapping("/update/gallery_info")
    public result update_user_gallery_info(@RequestBody Gallery gallery) throws Exception {
        userservice.save_gallery(gallery);
        System.out.println(gallery);
        return result.success();
    }

    @PostMapping("/deleteGallery")
    public result deleteGallery(@RequestBody Gallery gallery){
        userservice.deleteGallery(gallery.getSrc());
        return result.success();
    }

    //syb
    @PostMapping("/upload/blog_cover_pic")
    public result upload_blog_cover_pic(@RequestParam ("file") MultipartFile  file,@RequestParam("username") String username,@RequestParam("filename") String filename) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        // System.out.println(username);
        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + "_"+"blog_cover_pic" + StrUtil.DOT + type;
        File uploadFile = new File(filesUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.transferTo(uploadFile);
        userservice.upload_blog_cover_pic(filesUploadPath + fileUUID,"userinfo",filename + "_"+"blog_cover_pic" + StrUtil.DOT + type);
        //need repairing
        System.out.println(endpoint + "/userinfo/" + filename + "_" + "blog_cover_pic" + StrUtil.DOT + type);
        return result.success(endpoint + "/userinfo/" + filename + "_" + "blog_cover_pic" + StrUtil.DOT + type);
    }

    //syb
    @PostMapping("/update/blog")
    public result update_blog(@RequestBody Blog blog) throws Exception {
        userservice.update_blog(blog);
        return result.success();
    }

    //syb
    @PostMapping("/insert/blog")
    public result insert_blog(@RequestBody Blog blog) throws Exception {
        User currentUser = TokenUtils.getCurrentUser();
        blog.setId(currentUser.getId());
        blog.setUsername(currentUser.getUsername());
        userservice.insert_blog(blog);
        return result.success();
    }
    //syb
    @RequestMapping(value = "/get_gallery_pic",method = RequestMethod.POST,produces = MediaType.IMAGE_PNG_VALUE)
    private String get_gallery_Pic(@RequestParam("filename") String filename) throws Exception {
        filename=userservice.download_gallery("userinfo",filename,filesUploadPath);
        if(filename.isEmpty())
        {
            return null;
        }
        
        //FileInputStream picInput = new FileInputStream(filesUploadPath+username);
        
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket("userinfo")
                .object(filename + "_gallery_pic")
                .expiry(3, TimeUnit.MINUTES)
                .method(Method.GET)
                .build());
       
        return presignedObjectUrl;
        //return picInput.readAllBytes();
    }
    //syb-gallery
    @GetMapping("/get_gallery/{username}")
    public List<Gallery> get_gallery(@PathVariable String username) {
        return userservice.get_gallery_by_username(username);
    }

    //syb-blog
    @GetMapping("/get_blog/{blog_id}")
    public Blog get_blog(@PathVariable Integer blog_id) {
        return userservice.get_blog_by_blog_id(blog_id);
    }

    @GetMapping("/get_blog_list")
    public List<Blog> get_blog_list() {
        User currentUser = TokenUtils.getCurrentUser();
        return userservice.get_blog_by_user(currentUser.getId());
    }

    @GetMapping("/delete_blog/{blog_id}")
    public Integer delete_blog(@PathVariable Integer blog_id) {
        return userservice.delete_blog(blog_id);
    }

    private void buildTimeByBlog(Blog blog){
        if (blog.getTime()!=null && blog.getTime().equals("The Past Month")){
            //最近一个月
            DateTime dateTime = DateUtil.lastMonth();
            String format = DateUtil.format(dateTime, "yyyy-MM-dd");
            blog.setTime(format);
        }else if (blog.getTime()!=null && blog.getTime().equals("This Past Year")){
            //最近一年
            DateTime dateTime = DateUtil.offset(new Date(), DateField.YEAR,-1);
            String format = DateUtil.format(dateTime, "yyyy-MM-dd");
            blog.setTime(format);
        }else{
            blog.setTime(null);
        }
    }

    @PostMapping("/getMapByProvince")
    public List<MapByProvinceVO> getMapByProvince(@RequestBody Blog blog) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser==null || StringUtils.isBlank(blog.getProvince())){
            return Arrays.asList();
        }
        //时间查询构建
        this.buildTimeByBlog(blog);
        return userservice.getMapByProvince(currentUser.getId(),blog.getProvince(),blog.getTime(),blog.getLocation());
    }

    @PostMapping("/getMapByUser")
    public Map<String,Object> getMapByUser(@RequestBody Blog blog) {
        Map<String,Object> result=new HashMap<>();
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser==null){
            return result;
        }
        //时间查询构建
        this.buildTimeByBlog(blog);
        //每个省份去过的次数（该省份下各个城市去过次数的总和）
        List<MapByProvinceVO> provinceFreqData = userservice.getMapByUser(currentUser.getId(),blog.getTime(),blog.getLocation());
        result.put("provinceFreqData",provinceFreqData);

        //去过的地点数据，用于高亮。{ value: [116.407, 39.904], name: '北京' },//经纬度坐标 城市
        List<ScatterDataVO> scatterData=new ArrayList<>();
        for (MapByProvinceVO provinceFreqDatum : provinceFreqData) {
            ScatterDataVO scatterDataVO = MapCpConstants.scatterDataList.stream().filter(s -> provinceFreqDatum.getName().contains(s.getName())).findFirst().orElse(null);
            if (scatterDataVO!=null){
                scatterData.add(scatterDataVO);
                //名字去省用简称
                provinceFreqDatum.setName(scatterDataVO.getName());
            }
        }
        result.put("scatterData",scatterData);

        //去过的路线数据，按时间排序。coords: [[116.407, 39.904], [121.473, 31.230]],//起点经纬度坐标 终点经纬度坐标
        List<Map<String,List<List<Float>>>> routeData=new ArrayList<>();
        for (int i=0;i<provinceFreqData.size();i++){
            //当前点
            MapByProvinceVO curr = provinceFreqData.get(i);
            //下一点
            if ((i+1)<provinceFreqData.size()){
                MapByProvinceVO next = provinceFreqData.get(i+1);
                ScatterDataVO currVO = MapCpConstants.scatterDataList.stream().filter(s -> curr.getName().contains(s.getName())).findFirst().orElse(null);
                ScatterDataVO nextVO = MapCpConstants.scatterDataList.stream().filter(s -> next.getName().contains(s.getName())).findFirst().orElse(null);
                if (currVO!=null && nextVO!=null){
                    Map<String,List<List<Float>>> coords=new HashMap<>();
                    List<List<Float>> floatList=Arrays.asList(currVO.getValue(),nextVO.getValue());
                    coords.put("coords",floatList);
                    routeData.add(coords);
                }
            }
        }
        result.put("routeData",routeData);

        return result;
    }

    @PostMapping("/getMapPolar")
    public Map<String,Integer> getMapPolar(@RequestBody Blog blog) {
        Map<String,Integer> result=new HashMap<>();
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser==null){
            return result;
        }
        //时间查询构建
        this.buildTimeByBlog(blog);
        //去过城市总数，如果传了省份，就算省里面的，否则全国的
        List<MapByProvinceVO> mapByProvince = userservice.getMapByProvince(currentUser.getId(), blog.getProvince(), blog.getTime(), blog.getLocation());
        result.put("city_data",mapByProvince.size());
        //统计其朋友数量，计算排名，默认第一名
        Integer people_data=1;
        List<Friendship> friendList = userservice.getFriendship(currentUser.getId());
        for (Friendship friend : friendList) {
            List<MapByProvinceVO> mp = userservice.getMapByProvince(friend.getFriend_id(), blog.getProvince(), blog.getTime(), blog.getLocation());
            if (mp.size()>mapByProvince.size()){
                //后退一名
                people_data=people_data+1;
            }
        }
        result.put("people_data",people_data);

        return result;
    }

    @PostMapping("/getReport")
    public Map<String,Object> getReport(@RequestBody Blog blog) {
        Map<String,Object> result=new HashMap<>();
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser==null){
            return result;
        }

        int year = DateUtil.year(new Date());
        result.put("year",year);
        List<MapByProvinceVO> mapByProvince = userservice.getMapByProvince(currentUser.getId(), null, year+"-01-01", null);
        result.put("city_num",mapByProvince.size());
        MapByProvinceVO mapByProvinceVO = mapByProvince.stream().sorted(Comparator.comparing(MapByProvinceVO::getValue).reversed()).findFirst().orElse(null);
        if (mapByProvinceVO!=null){
            result.put("fav_area",mapByProvinceVO.getName());
        }
        //朋友去过相同地方统计
        List<String> citys = mapByProvince.stream().map(MapByProvinceVO::getName).collect(Collectors.toList());
        Map<String,List<String>> fCityMap=new HashMap<>();
        List<Friendship> friendList = userservice.getFriendship(currentUser.getId());
        for (Friendship friend : friendList) {
            List<MapByProvinceVO> mp = userservice.getMapByProvince(friend.getFriend_id(), blog.getProvince(), blog.getTime(), blog.getLocation());
            List<MapByProvinceVO> fCitys = mp.stream().filter(m -> citys.contains(m.getName())).collect(Collectors.toList());
            fCityMap.put(friend.getFriend_username(),fCitys.stream().map(MapByProvinceVO::getName).collect(Collectors.toList()));
        }
        String friendName="";
        int friendCitySize=0;
        List<String> citysGo=new ArrayList<>();
        for (String f:fCityMap.keySet()){
            if(fCityMap.get(f).size()>friendCitySize){
                friendName=f;
                friendCitySize=fCityMap.get(f).size();
                citysGo=fCityMap.get(f);
            }
        }
        //common_city:{friend:"鱼籽酱虾球",city_name:"南京,扬州,杭州...",city_num:3,}
        JSONObject object=new JSONObject();
        object.put("friend",friendName);
        object.put("city_name",citysGo.stream().collect(Collectors.joining()));
        object.put("city_num",friendCitySize);
        result.put("common_city",object);
        //一天之中blog最多的一个地方
        MapByProvinceVO report = userservice.getReport(currentUser.getId(), year + "-01-01");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("date",report==null?"":report.getDate());
        jsonObject.put("city",report==null?"":report.getName());
        jsonObject.put("blog_num",report==null?"":report.getPostCount());
        result.put("special_travel",jsonObject);

        return result;
    }
}
