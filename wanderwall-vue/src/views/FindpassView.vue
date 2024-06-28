<template>
  <div class="login-background">
    <div class="flexcenter">
      <div style="margin-right: 200px; z-index: 6;width: 30%"><img src="@/assets/wanderwall-logo.png" style="opacity: 0.7"></div>
      <div class="login-container">
        <div class="form-container">
          <el-form :model="user" :rules="rules" ref="loginref" class="login-form">
            <h2 style="margin: 20px">Find Password</h2>
            <el-form :model="user" :rules="specialrule" ref="verifyref">
              <el-form-item prop="email">
                <el-input class="custom-input" prefix-icon="message" size="large" placeholder="Email" v-model="user.email" clearable></el-input>
              </el-form-item>
            </el-form>
            <el-form-item prop="password">
              <el-input class="custom-input" prefix-icon="lock" size="large" show-password placeholder="New password" v-model="user.password" clearable></el-input>
            </el-form-item>
            <el-form-item prop="confirmpass">
              <el-input class="custom-input" prefix-icon="lock" size="large" show-password placeholder="Confirm password" v-model="user.confirmpass" clearable></el-input>
            </el-form-item>
            <div style="flex: 1; display: flex;">
              <div style="flex: 1;">
                <el-form-item prop="code">
                  <el-input class="custom-input" prefix-icon="chat-dot-round" size="large" placeholder="Verification" v-model="user.code" clearable></el-input>
                </el-form-item>
              </div>
              <div style="flex: 1;text-align: right;">
                <el-form-item>
                  <el-button type="primary" style="width: 90%;height: 56px;background-color:#5f9592" @click="verify">Get Code</el-button>
                </el-form-item>
              </div>
            </div>
            <el-form-item>
              <el-button type="primary" style="width: 100%;background-color:#5f9592" @click="findpass">Modify</el-button>
            </el-form-item>
            <div style="display:flex">
              <div style="flex: 1;margin-bottom: 30px">Give up?Go <span style="color:lightseagreen; cursor: pointer; text-decoration: underline;" @click="$router.push('/login')">Log in</span></div>
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
  name: "RegisterView",
  components: {
    ElForm, ElFormItem, ElInput, ElButton
  },
  setup() {
    // 密码一致校验
    const confirm = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'));
      } else if (value !== this.user.password) {
        callback(new Error('两次密码不一致'));
      } else {
        callback
        callback();
      }
    };

    const user = ref({
      username: '',
      password: '',
      confirmpass: '',
      email: '',
      code: ''
    });
    const rules = ref({
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
      confirmpass: [
        { validator: confirm, trigger: 'blur' },
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
      ],
      code: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
      ]
    });
    const specialrule = ref({
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
      ]
    });

    const findpass = () => {
      // 找回密码逻辑
      console.log('找回密码');
    };

    const verify = () => {
      // 获取验证码逻辑
      console.log('获取验证码');
    };

    return {
      user,
      rules,
      findpass,
      specialrule,
      verify
    };
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
  background-image: url('~@/assets/login/find.jpg'); /* 使用~@表示根目录，然后再添加文件路径 */
  background-size: cover;
}

.login-background::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 调整透明度以获得所需的遮罩效果 */
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
  background-color: rgba(245, 245, 245, 0.9); /* 设置背景色为透明 */
  z-index: 5;
  width: 40%;
  border-radius: 5px;
  overflow: hidden;
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
