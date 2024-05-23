<template>
  <div class="login-background">
    <div class="flexcenter">
      <div style="margin-right: 200px; z-index: 6;width: 30%"><img src="@/assets/wanderwall-logo.png" style="opacity: 0.7"></div>
      <div class="login-container">
        <div class="form-container">
          <el-form :model="user" :rules="rules" ref="loginref" class="login-form">
            <!-- 表单内容 -->
            <h2 style="margin: 20px">找回密码</h2>
            <el-form :model="user" :rules="specialrule" ref="verifyref">
              <el-form-item prop="email">
              <el-input  prefix-icon="el-icon-message" size="large" placeholder="请输入邮箱" v-model="user.email"  clearable></el-input>
              </el-form-item>
            </el-form>
            <el-form-item prop="password">
              <el-input  prefix-icon="el-icon-lock" size="large" show-password placeholder="请输入新密码" v-model="user.password" clearable></el-input>
            </el-form-item>
            <el-form-item prop="confirmpass">
              <el-input  prefix-icon="el-icon-lock" size="large" show-password placeholder="请确认密码" v-model="user.confirmpass" clearable></el-input>
            </el-form-item>
            
            <div style="flex: 1; display: flex;"> 
              <div style="flex: 1;"> 
                <el-form-item prop="code">
                  <el-input  prefix-icon="el-icon-chat-dot-round" size="large" placeholder="请输入验证码" v-model="user.code" clearable></el-input>
                </el-form-item>
              </div>
              <div style="flex: 1;text-align: right;"> 
                <el-form-item>
                  <el-button type="primary" style="width: 90%;background-color:#5f9592" @click="verify">获取验证码</el-button>
                </el-form-item>
              </div>
              
            </div>
            <el-form-item>
              <el-button type="primary" style="width: 100%;background-color:#5f9592" @click="findpass">修改</el-button>
            </el-form-item>
            <div style="display:flex">
              <div style="flex: 1;color: white;margin-bottom: 30px">放弃修改?去<span style="color:lightseagreen; cursor: pointer; text-decoration: underline;" @click="$router.push('/login')">登录</span></div>
            </div>

          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginView",
  data() {
    const confirm = (rule, value, callback) =>{
      if(value === '')
      {
        callback(new Error('请确认密码'))
      }
      else if(value !== this.user.password)
      {
        callback(new Error('两次密码不一致'))
      }
      else
      {
        callback()
      }
  }
   return {
        user:{
          email: '',
          password: '' ,
          confirmpass: '',
          code: ''
        },
        rules: {
          email: [
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
 { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
          ],
          confirmpass: [
            { validator: confirm, trigger: 'blur' },
          ],
          code: [
            { required: true, message: '请输入验证码', trigger: 'blur' },
          ]
        },
        specialrule: {
          email: [
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
 { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
          ]
        }
    }
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

</style>
