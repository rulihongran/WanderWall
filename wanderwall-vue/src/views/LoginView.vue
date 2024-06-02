<template>
  <div class="login-background">
    <div class="flexcenter">
      <div style="margin-right: 200px; z-index: 6;width: 30%"><img src="@/assets/wanderwall-logo.png" style="opacity: 0.7"></div>
      <div class="login-container">
        <div class="form-container">
          <el-form :model="user" :rules="rules" ref="loginref" class="login-form">
            <!-- 表单内容 -->
            <h2 style="margin: 20px">登录</h2>
            <el-form-item prop="username">
              <el-input class="custom-input" prefix-icon="User" size="large" placeholder="请输入账号或邮箱" v-model="user.username" clearable></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input class="custom-input" prefix-icon="Lock" size="large" show-password placeholder="请输入密码" v-model="user.password" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%; background-color:#5f9592" @click="$router.push('/')">登录</el-button>
            </el-form-item>
            <div style="display:flex">
              <div style="flex: 1;margin-bottom: 50px">还没有账号?去<span style="color:green; cursor: pointer; text-decoration: underline;" @click="$router.push('/register')">注册</span></div>
              <div style="flex: 1;text-align: right;"><span style="color:green; cursor: pointer; text-decoration: underline;" @click="$router.push('/findpass')">忘记密码?</span></div>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';

export default {
  name: "LoginView",
  components: {
    ElForm, ElFormItem, ElInput, ElButton
  },
  setup() {
    const user = ref({
      username: '',
      password: ''
    });

    const rules = {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
    };

    return {
      user,
      rules
    };
  }
}
</script>
  
  <style scoped>
  .login-background {
    height: 100vh;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-image: url('~@/assets/login/login.jpg'); /* 使用~@表示根目录，然后再添加文件路径 */
    background-size: cover;
  }
  .login-background::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4); /* 调整透明度以获得所需的遮罩效果 */
    z-index: 1;
  }
  .flexcenter {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 80%; /* 设置合适的宽度 */
    background-color: rgba(0, 0, 0, 0); /* 设置背景色为透明 */
  }
  
  .login-container {
    display: flex;
    background-color:  rgba(245, 245, 245, 0.8); /* 设置半透明背景色 */;
    width: 40%;
    border-radius: 5px;
    overflow: hidden;
    z-index: 3;
  }
  
  .image-container {
    flex: 1;
    display: flex;
  }
  
  .image-container img {
    height: 100%;
    width: 100%;
  }
  
  .form-container {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .login-form {
    width: 80%;
  }

  ::v-deep .custom-input .el-input__inner {
  border: none;
}
  
  </style>
  