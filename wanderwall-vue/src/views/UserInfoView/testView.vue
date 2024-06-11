<template>
    <div>
      <el-card>
        <el-descriptions  class="margin-top" title="简介" :column="2" border>
          <template slot="extra">
            <el-button type="primary" size="small" @click="isEditing = !isEditing">{{ isEditing ? '保存' : '修改个人信息' }}</el-button>
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-picture-outline"></i>
              头像
            </template>
            <img v-if="!isEditing" class="img" :src="avatar" alt="" />
            <el-upload v-else class="avatar-uploader" action="https://jsonplaceholder.typicode.com/posts/" :show-file-list="false" :on-success="handleAvatarSuccess">
              <img v-if="imageUrl" :src="imageUrl" class="img" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              账户名
            </template>
            <span v-if="!isEditing">{{ account }}</span>
            <el-input v-else v-model="account"></el-input>
          </el-descriptions-item>
          <!-- 其他描述项也类似处理，省略... -->
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-s-custom"></i>
              昵称
            </template>
            <span v-if="!isEditing">{{ nickname }}</span>
            <el-input v-else v-model="nickname"></el-input>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-odometer"></i>
              生日
            </template>
            <span v-if="!isEditing">{{ birthday }}</span>
            <el-date-picker v-else v-model="birthday" type="date"></el-date-picker>
          </el-descriptions-item>
          <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱Email
          </template>
          <span v-if="!isEditing">{{ email }}</span>
          <el-input v-else v-model="email"></el-input>
        </el-descriptions-item>
        <!-- Mobile Phone Number Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号码
          </template>
          <span v-if="!isEditing">{{ mobilePhoneNumber }}</span>
          <el-input v-else v-model="mobilePhoneNumber"></el-input>
        </el-descriptions-item>
        <!-- Area Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-map-location"></i>
            地区
          </template>
          <span v-if="!isEditing">{{ area }}</span>
          <el-input v-else v-model="area"></el-input>
        </el-descriptions-item>
        <!-- Creation Date Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-date"></i>
            注册日期
          </template>
          <span>{{ createDate | formatDate }}</span>
        </el-descriptions-item>
        <!-- Sex Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-male"></i>
            <i class="el-icon-female"></i>
            性别
          </template>
          <span v-if="!isEditing">{{ sex }}</span>
          <el-select v-else v-model="sex" placeholder="请选择">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-descriptions-item>
        <!-- Hobby Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-basketball"></i>
            兴趣爱好
          </template>
          <span v-if="!isEditing">{{ hobby }}</span>
          <el-input v-else v-model="hobby"></el-input>
        </el-descriptions-item>
        <!-- Personal Signature Field -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-magic-stick"></i>
            个性签名
          </template>
          <span v-if="!isEditing">{{ design }}</span>
          <el-input v-else v-model="design"></el-input>
        </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </template>
  
  <script>
  export default {
    name: "infoView",
    data() {
      return {
        isEditing: false,
        avatar: '//game.gtimg.cn/images/lol/act/img/champion/Annie.png',
        account: 'user123',
        birthday: '1995-05-15',
        email: 'user@example.com',
        mobilePhoneNumber: '1234567890',
        area: '北京',
        createDate: Date.now(),
        nickname: '马小跳',
        sex: '男',
        work: '工程师',
        hobby: '阅读, 旅游',
        design: '生活就像海洋，只有意志坚强的人才能到达彼岸。',
      };
    },
    methods: {
      handleAvatarSuccess(res, file) {
        this.avatar = URL.createObjectURL(file.raw);
      },
      // ...其他方法
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
  </style>
  