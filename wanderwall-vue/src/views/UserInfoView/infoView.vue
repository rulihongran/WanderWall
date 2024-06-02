<template>
  <div >
    <el-card style="height: 100%;">
      <el-descriptions class="margin-top" title="简介" :column="2" border>
        <template #extra>
          <el-button type="primary" size="small" @click="isEditing = !isEditing">{{ isEditing ? '保存' : '修改个人信息' }}</el-button>
        </template>
        <el-descriptions-item>
          <template #label>
            <el-icon><PictureFilled /></el-icon> 头像
          </template>
          <img v-if="!isEditing" class="img" :src="avatar" alt="" />
          <el-upload v-else class="avatar-uploader" action="https://jsonplaceholder.typicode.com/posts/" :show-file-list="false" :on-success="handleAvatarSuccess">
            <el-icon class="avatar-uploader-icon">+</el-icon>
          </el-upload>
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
    </el-card>
  </div>
</template>

<script>
/*需要后端实现传入和存入下面的示例数据*/
export default {
  name: "infoView",
  data() {
    return {
      isEditing: false,
      /* 从后端传过来一下数据就可以，*/
      avatar: '//game.gtimg.cn/images/lol/act/img/champion/Annie.png',
      account: 'user123',
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
    };
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
  }
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
