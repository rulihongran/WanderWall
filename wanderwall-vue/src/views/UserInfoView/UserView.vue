<template>
  <Header/>
  <div class="header-container" >
    <div class="header-background" @click="triggerFileInput" :style="backgroundStyle">
      <input type="file" ref="fileInput" @change="onFileChange" />
      <div v-if="!backgroundImage" class="ProfileBanner-promptContainer-fgV ProfileBanner-transparentNavPromptContainer-xAj">
        <div class="ProfileBanner-prompt-yBZ">
          <div class="ProfileBanner-dropTarget-_sN">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42.672 42.672" class="ProfileBanner-dropIcon-jnp">
              <path d="M31.654,10.075C19.884,10.075,10,19.322,10,31.092A21.9,21.9,0,0,0,31.654,52.747c11.77,0,21.017-9.884,21.017-21.654A20.807,20.807,0,0,0,31.654,10.075Zm10.19,24.2L31.654,43.83c-.156.156.22.637,0,.637s-.481-.481-.637-.637l-10.19-9.553c-.24-.239-.132-.325,0-.637s.3-.643.637-.637h6.369V18.992c0-.458.816-.637,1.274-.637h4.458c.458,0,1.274.179,1.274.637V33h6.369c.333,0,.512.328.637.637S42.081,34.041,41.845,34.277Z" transform="translate(-10 -10.075)"></path>
            </svg>
          </div>
          <button class="ProfileBanner-promptHeading-tkX" @click="triggerFileInput">添加横幅图像</button>
          <p class="ProfileBanner-assetSuggestion-ADZ">最佳尺寸 3200 x 410px</p>
        </div>
      </div>
    </div>

    <!-- 资料框 -->
   <div class="center1">
    <div  class="center2">
      <div class="left" style="margin: 20px;">
          <div class="avatar-container">
            <div class="avatar">
            <img src="~@/assets/avatar/male.png" alt="马小跳的个人资料">
             </div>
          </div>
          <div style="  display: flex;justify-content: center;top:0; /* 水平居中 */">
            <h1 class="username">{{ username }}</h1>
          </div>
          <div style="margin-top:  40px;margin: 20px;"><el-button type="primary" round style="width: 100%; background-color:#5f9592;font-size: 20px" @click="$router.push({ name: 'EditView' })"><i class="el-icon-edit">修改个人资料</i></el-button>
          </div>
          <div>
            <div>
             <h2>{{ signature }}</h2>
             <p>{{ currentTime }}</p>
           </div>
          </div>

    </div>

    <div class="right">
      <el-menu
    :default-active="activeTabIndex"
    class="el-menu-demo"
    mode="horizontal"
    @select="handleSelect"
  >
    <el-menu-item
      v-for="(tab, index) in tabs"
      :key="index"
      :index="String(index)"
      @click="goToTab(tab.link)"
      class="custom-menu-item"
    >
      {{ tab.name }}
    </el-menu-item>
  </el-menu>

<!-- 工作区域 -->
    <div class="body-right">
        <router-view></router-view>
      </div>


    </div>
    </div>
  </div>
  </div>

  </template>
  
  <script>
  import Header from "@/components/Header.vue"
  import store from "../../store/index";
import { param } from "jquery";
import { data } from "jquery";
  export default {
    name: 'UserView',
    components: {Header},
    data() {
      return {
        isHovering: false,
        backgroundImage: null,
        showPrompt: true,
        src:null,
        username:JSON.parse(localStorage.getItem("username"))?JSON.parse(localStorage.getItem("username")):"Suicidal Capybara",//JSON.parse(localStorage.getItem("user"))
        signature: '这是我的个性签名',
      currentTime: '',

      tabs: [
        { name: '故事', link: 'work' },
        { name: '好友', link: 'friend' },
        { name: '推荐', link: 'addfriend' },
      ],
      activeTabIndex: 0         // 当前活动选项卡的索引
      };
    },
    created() {
      console.log(this.$store.state.user_name)
      this.load();
    // 在组件创建时获取当前时间
    this.updateTime();
    // 每隔一秒更新一次时间
    setInterval(this.updateTime, 1000);
  },
    computed: {
      backgroundStyle() {
        return {
          backgroundColor: this.backgroundImage ? 'transparent' : '#5f9592',
          backgroundImage: this.src?this.src:`url(${this.backgroundImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center'
        };
      }
    },
    methods: {
      load() {
        console.log(store.state.user_name)
        this.$http.get("/user/"+this.username).then(res => {
          console.log(res)
        if(res.signature!=null)
        {
        this.signature = res.signature
        }
        // this.tableData = res.data.records
        // this.total = res.dsignatureata.total
      });
      this.$http({
        url: '/user/download/background',
        method: 'post',
        params: {username: this.username},
        
        //responseType:'blob'
      }).then(res =>  {
         console.log(res);
          const url = res;
          // let blob = new window.Blob([res], {type: 'image/png'});
          // console.log(blob);
          // let url = window.URL.createObjectURL(blob);
          // this.src = url;
          this.backgroundImage=url?url:this.backgroundImage;
      }
    );
      }, 
        triggerFileInput() {
      this.$refs.fileInput.click();
    },
    onFileChange(event) {
      const file = event.target.files[0];
      const file2=this.$refs.fileInput.files[0];
      const formData = new FormData();
      formData.append('username', this.username);
      formData.append('file', file2);
      console.log(this.$refs.fileInput.files[0]);
      // 使用 axios 或其他 HTTP 请求库发送文件到后台
      
      this.$http({
        url: '/user/upload/background',
        method: 'post',
        data: formData,
        headers: {'Content-Type': "multipart/form-data"},
      }).then(res =>  {
          console.log(res); 
      }
    );
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.backgroundImage = e.target.result;
          this.showPrompt = false; // 图片上传成功后，隐藏提示信息
        };
        reader.readAsDataURL(file);
      }
    },
    goToTab(link) {
      // 获取当前路由路径
      const currentRoutePath = this.$route.path;

      // 如果当前路由路径与目标链接不同，则进行导航
      if (currentRoutePath !== link) {
        this.$router.push(link);
      }
    },
 

    /*时间 */
    updateTime() {
      // 使用 Date 对象获取当前时间，并格式化为字符串
      const now = new Date();
      const hours = now.getHours();
      const minutes = now.getMinutes();
      const seconds = now.getSeconds();
      this.currentTime = `${this.formatTime(hours)}:${this.formatTime(minutes)}:${this.formatTime(seconds)}`;
    },
    formatTime(time) {
      // 将时间格式化为两位数
      return time < 10 ? '0' + time : time;
    }
  }
  };
  </script>
  
  <style scoped>
  .header-container {
    margin-top: 80px;
    position: relative;
    width: 100%;
 /* 根据需要调整高度 */
    height:800px;
    background-color: #f9f9f9;;
  }
  
  .header-background {
    width: 100%;
    height: 266px;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    position: relative;
  }
  
  .header-background input[type="file"] {
    display: none;
  }
  

  
  .ProfileBanner-prompt-yBZ {
    /* 根据需要添加样式 */
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }
 
  .ProfileBanner-dropIcon-jnp {
    /* 根据需要添加样式 */
    width: 66px;
    height: 66px;
    fill: currentColor;
  }
  
  .ProfileBanner-promptHeading-tkX {
    /* 根据需要添加样式 */
    font-size: 20px; /* 设置字体大小 */
    font-weight: bold; /* 设置字体加粗 */
    background-color: transparent; /* 设置背景透明 */
    cursor: pointer;
  }
  
  .ProfileBanner-assetSuggestion-ADZ {
    /* 根据需要添加样式 */
    font-size: 16px;
    font-weight: bold;
    margin-top: 4px;
  }

  /*第二个底层 :背景图插入有问题*/
  .center1{
    top:0;
    width: calc(100% - 140px); /* 宽度为100%减去左右边距 */
   margin-left: 70px; /* 左边距 */
   margin-right: 70px; /* 右边距 */
   background-color: transparent; /* 设置背景色为透明 */
   height: 100%;
  /* 如果需要的话，可以添加边框 */
  border: 1px solid transparent;
  position: absolute; /* 将叠加层设为绝对定位 */
  pointer-events: none; /* 禁用点击行为 */
  }

   /*第三个底层 */
   .center2{
    top:80px;
    bottom: 80px;
    display: flex;
    width: calc(100% - 200px); /* 宽度为100%减去左右边距 */
   margin-left: 100px; /* 左边距 */
   margin-right: 100px; /* 右边距 */
   background-color: transparent; /* 设置背景色为透明 */
  position: absolute; /* 将叠加层设为绝对定位 */  
  }
  /*用户资料显示 */

  .left {
    width: 360px; /* 设置宽度为 360 像素 */
  height: 660px; /* 设置高度为 660 像素 */
  background-color: white; /* 设置底色为白色 */
  pointer-events: auto; /* 使得right部分可点击 */
}
.avatar-container {
  width: 122px;
  height: 122px;
  margin: 20px auto 0;
}

.avatar img {
  width: 110px;
  height: 110px;
}

.username {
  color: black; /* 设置文本颜色为黑色 */
  width: 300px;
  height: 29px;
  font-size: 18px; /* 设置字体大小为 17px */
  text-align: center; /* 设置文本水平居中 */
}
/*右侧格式*/
.right {
  flex: 1; /* 自动扩展填充剩余空间 */
}
.el-menu-demo{
  top:190px;
  pointer-events: auto; /* 使得部分可点击 */
}
.custom-menu-item {
  color: #333; /* 设置菜单项文字颜色 */
  font-weight: bold; /* 设置菜单项文字加粗 */
  font-size: large;
  padding: 10px 20px; /* 设置菜单项内边距 */
  pointer-events: auto; /* 使得部分可点击 */
}

.body-right{
  margin-top: 200px;
  height: 420px;
  width: 100%;
  /* height: 500px; */
  border-radius: 5px;
  background-color: white;
  pointer-events: auto; /* 使得right部分可点击 */
}

  </style>
  