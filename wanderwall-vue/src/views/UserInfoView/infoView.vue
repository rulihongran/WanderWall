<template>
  <div >
    <el-card style="height: 100%;">
      <el-descriptions class="margin-top" title="简介" :column="2" border>
        <template #extra v-if=1>
        <el-button type="primary" size="small" @click="isEditing = !isEditing;saveProfile()">{{ isEditing ? '保存' : '修改个人信息' }}</el-button>
      </template>

         <!-- Image File ID Field -->
      <el-descriptions-item>
        <template #label>
          <i class="el-icon-picture-outline"></i>
          头像
        </template>
        <img v-if="!isEditing" class="img" :src="avatar" alt="" />
        <div v-else>
            <img v-if="avatar" :src="avatar" class="img" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <input type="file" ref="fileInput"  @change="handleFileChange" style="display: none">
            <el-button type="primary" @click="triggerFileInput">上传<i class="el-icon-upload el-icon--right"></i></el-button>
        </div>

      </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <el-icon><User /></el-icon> 账户名
          </template>
          <span v-if="!isEditing">{{ account }}</span>
          <el-input  v-else v-model="account" class="no-border-input"></el-input>
        </el-descriptions-item>
        <!-- 其他描述项也类似处理，省略... -->
        <el-descriptions-item>
          <template #label>
            <el-icon><UserFilled /></el-icon> 昵称
          </template>
          <span v-if="!isEditing">{{ nickname }}</span>
          <el-input v-else v-model="nickname" class="no-border-input"></el-input>
        </el-descriptions-item>
      
        <el-descriptions-item>
        <template #label>
          <el-icon><message /></el-icon> 邮箱Email
        </template>
        <span v-if="!isEditing">{{ email }}</span>
        <el-input v-else v-model="email" class="no-border-input"></el-input>
      </el-descriptions-item>
      
      <el-descriptions-item >
    <template #label>
      <el-icon><odometer /></el-icon> 生日
    </template>
    <span v-if="!isEditing">{{ formattedBirthday }}</span>
    <el-row v-else style="width: 300px;">
      <el-col :span="8">
        <el-tooltip content="输入年份" placement="top">
          <el-input
            v-model="birthdayYear"
            placeholder="年"
            size="small"
            class="no-border-input"
          ></el-input>
        </el-tooltip>
      </el-col>
      <el-col :span="8">
        <el-tooltip content="输入月份" placement="top">
          <el-input
            v-model="birthdayMonth"
            placeholder="月"
            size="small"
            class="no-border-input"
          ></el-input>
        </el-tooltip>
      </el-col>
      <el-col :span="8">
        <el-tooltip content="输入日期" placement="top">
          <el-input
            v-model="birthdayDay"
            placeholder="日"
            size="small"
            class="no-border-input"
          ></el-input>
        </el-tooltip>
      </el-col>
    </el-row>
  </el-descriptions-item>
      <!-- Mobile Phone Number Field -->
      <el-descriptions-item>
        <template #label>
          <el-icon><Iphone /></el-icon> 手机号码
        </template>
        <span v-if="!isEditing">{{ mobilePhoneNumber }}</span>
        <el-input v-else v-model="mobilePhoneNumber" class="no-border-input"></el-input>
      </el-descriptions-item>
      <!-- Area Field -->
      <el-descriptions-item>
        <template #label>
          <el-icon><map-location/></el-icon>地区
        </template>
        <span v-if="!isEditing">{{ area }}</span>
        <el-input v-else v-model="area" class="no-border-input"></el-input>
      </el-descriptions-item>
      <!-- Creation Date Field -->
      <!-- Sex Field -->
      <el-descriptions-item>
        <template #label>
          <el-icon><male/></el-icon><el-icon><female/></el-icon>性别
        </template>
        <span v-if="!isEditing">{{ sex }}</span>
        <el-select v-else v-model="sex" placeholder="请选择">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-descriptions-item>
      <!-- Hobby Field -->
      <el-descriptions-item>
        <template #label>
          <el-icon><Basketball /></el-icon>兴趣爱好
        </template>
        <span v-if="!isEditing">{{ hobby }}</span>
        <el-input v-else v-model="hobby" class="no-border-input"></el-input>
      </el-descriptions-item>
      <!-- Personal Signature Field -->
      <el-descriptions-item>
        <template #label>
          <el-icon><magic-stick /></el-icon>个性签名
        </template>
        <span v-if="!isEditing">{{ design }}</span>
        <el-input v-else v-model="design" class="no-border-input"></el-input>
      </el-descriptions-item>
      </el-descriptions>
      
      <el-descriptions-item>
        <span v-if="!isEditing">{{ hobby }}</span>
      </el-descriptions-item>
    </el-card>
  </div>
</template>

<script>
import { data } from 'jquery';


/*需要后端实现传入和存入下面的示例数据*/
export default {
  name: "infoView",
  data() {
    return {
      username:JSON.parse(localStorage.getItem("username"))?JSON.parse(localStorage.getItem("username")):"Suicidal Capybara",
      isEditing: false,
      /* 从后端传过来一下数据就可以，*/
      selectedfile: null, // 新的文件对象
      avatar: '//game.gtimg.cn/images/lol/act/img/champion/Annie.png',
      account:JSON.parse(localStorage.getItem("username"))?JSON.parse(localStorage.getItem("username")):"Suicidal Capybara",
      birthdayYear: '1995',
      birthdayMonth: '5',
       birthdayDay: '5',
      email: 'user@example.com',
      mobilePhoneNumber: '1234567890',
      area: '北京',
      nickname: '马小跳',
      sex: '男',
      work: '工程师',
      hobby: '阅读, 旅游',
      design: '生活就像海洋，只有意志坚强的人才能到达彼岸。',
      bucket:'userinfo',
      object:JSON.parse(localStorage.getItem("username")).toString()+"_avatar",
    };
  },
  created() {
    this.load();
  },
  computed: {
  formattedBirthday() {
    if (this.birthdayYear && this.birthdayMonth && this.birthdayDay) {
      return `${this.birthdayYear}-${this.birthdayMonth}-${this.birthdayDay}`;
    } else {
      return '';
    }
  },
},
  methods: {
    handleAvatarSuccess(res, file) {
      this.avatar = URL.createObjectURL(file.raw);
    },
    saveProfile() {
    if (!this.isEditing) {
      this.$http({
        url: '/user/update/userinfo',
        method: 'post',
        data: { username:this.username, nickname:this.nickname, sex:this.sex, birsday:this.birthdayYear+"-"+this.birthdayMonth+"-"+this.birthdayDay, email:this.email, phone:this.mobilePhoneNumber, address:this.area, work:this.work, habit:this.hobby, signature:this.design, bucket:this.bucket, object:this.object},
      }).then(res =>  { 
        console.log(this.fansAndFollows);
       });
    }
  },
    load() {
        this.$http.get("/user/"+this.username).then(res => {
          console.log(res.signature)
        this.signature = res.signature? res.signature :this.signature
        this.nickname = res.nickname? res.nickname :this.nickname
        this.sex = res.sex? res.sex :this.sex
        this.str=res.birsday? res.birsday :this.str
        if(this.str!=null){
        this.birthdayYear =this.str.split("-")[0]? this.str.split("-")[0] :this.birthdayYear
        this.birthdayMonth = this.str.split("-")[1]? this.str.split("-")[1] :this.birthdayMonth
        this.birthdayDay = this.str.split("-")[2]? this.str.split("-")[2] :this.birthdayDay
        }
        this.email = res.email? res.email :this.email
        this.mobilePhoneNumber = res.phone? res.phone :this.mobilePhoneNumber
        this.area = res.address? res.address :this.area
        this.work = res.work? res.work :this.work
        this.hobby = res.habit? res.habit :this.hobby
        this.design = res.signature? res.signature :this.design
        this.bucket=res.bucket? res.bucket :this.bucket
        this.object=res.object? res.object :this.object
        // this.backgroundImage=res.backgroundImage? res.backgroundImage :this.backgroundImage
        // this.tableData = res.data.records
        // this.total = res.dsignatureata.total
        this.$http({
        url: '/user/download/avatar',
        method: 'post',
        params: {bucket: this.bucket, object: this.object},
        //responseType:'blob'
      }).then(res =>  {
         console.log(res);
          const url = res;
          // let blob = new window.Blob([res], {type: 'image/png'});
          // console.log(blob);
          // let url = window.URL.createObjectURL(blob);
          // this.src = url;
          this.avatar=url?url:this.backgroundImage;
      }
    );
      });
    //   this.$http({
    //     url: '/user/download/background',
    //     method: 'post',
    //     params: {username: this.username},
    //     //responseType:'blob'
    //   }).then(res =>  {
    //       const url = res;
    //       this.backgroundImage=url;
    //       console.log(url);
    //   }
    // );
      }, 
    triggerFileInput() {
      this.$refs.fileInput.value = '';
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      this.selectedfile = event.target.files[0];
      const file=this.$refs.fileInput.files[0];
      const formData = new FormData();
      formData.append('username', this.username);
      formData.append('file', file);
      console.log(this.$refs.fileInput.files[0]);
      // 使用 axios 或其他 HTTP 请求库发送文件到后台
      
      this.$http({
        url: '/user/upload/avatar',
        method: 'post',
        data: formData,
        headers: {'Content-Type': "multipart/form-data"},
      }).then(res =>  {
        this.object=res.data;
          console.log(res.data); 
      }
    );
  if (this.selectedfile) {
    // 创建一个 FileReader 对象
    const reader = new FileReader();
    // 当文件读取完成时触发
    reader.onload = (e) => {
      // 将读取的结果转换为 URL 并赋值给 avatar
      this.avatar = e.target.result;
    };
    // 读取文件内容
    reader.readAsDataURL(this.selectedfile);
  }
}

  },
};
</script>

<style scoped>
/* 样式保持不变 */
.img {
  width: 80px;
  height: 80px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  line-height: 80px;
  text-align: center;
}
.no-border-input ::v-deep .el-input__inner {
  border: none;
}
.no-border-datepicker ::v-deep .el-date-editor {
  border: none;
}

</style>
