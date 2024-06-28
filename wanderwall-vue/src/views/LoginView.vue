<template>
  <div class="login-background">
    <div class="flexcenter">
      <div style="margin-right: 200px; z-index: 6;width: 30%"><img src="@/assets/wanderwall-logo.png" style="opacity: 0.7"></div>
      <div class="login-container">
        <div class="form-container">
          <el-form :model="user" :rules="rules" ref="loginref" class="login-form">
            <!-- 表单内容 -->
            <h2 style="margin: 20px">Log in</h2>
            <el-form-item prop="username">
              <el-input class="custom-input" prefix-icon="User" size="large" placeholder="Account or Email" v-model="user.username" clearable></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input class="custom-input" prefix-icon="Lock" size="large" show-password placeholder="Password" v-model="user.password" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%; background-color:#5f9592" @click="login">Log in</el-button>
            </el-form-item>
            <div style="display:flex">
              <div style="flex: 1;margin-bottom: 50px">No account?Go <span style="color:green; cursor: pointer; text-decoration: underline;" @click="$router.push('/register')">Sign up</span></div>
              <div style="flex: 1;text-align: right;"><span style="color:green; cursor: pointer; text-decoration: underline;" @click="$router.push('/findpass')">Forget password?</span></div>
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
import store from "../store/index";
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
  },
  methods: {
      login() {
      this.$refs['loginref'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.$http.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              // localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
              // 动态设置当前用户的路由
              // setRoutes()
              this.$message.success("Log in successfully.");
              store.commit("setUserName", res.data.username)  // 存储用户信息到vuex
              this.$store.commit("setUserName", res.data.username) 
              localStorage.setItem("username", JSON.stringify(res.data.username))  // 存储用户信息到浏览器
              console.log(this.$store.state.user_name)
              this.$router.push("/")
              // if (res.data.role === 'ROLE_STUDENT') {
              //   this.$router.push("/")
              //   this.$router.push("/front/home")
              // } else {
              //   this.$router.push("/")
              // }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
    }
}
</script>
  
  <style scoped>
  :deep(.el-input__inner){
    padding: 0px;
    border: 0px !important;
    box-shadow: none !important;
    background: transparent;
  }
  :deep(.el-input__inner:hover){
    background: transparent;
  }
  :deep(.el-input__inner:focus){
    background: transparent;
  }

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
  