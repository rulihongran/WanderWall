<template>
  <div style="height: 1000px; width: 100%;display: flex;background-color: #f9f9f9;">
    <div class="nav">
        <div class="button-style">
            <el-button type="primary" round @click="$router.push({ name: 'UserView' })">
                <i class="el-icon-edit">返回个人资料</i></el-button></div>
    </div>
    <div class="back1">
        <div class="back2">

            <div class="person_body_left">
        <el-card class="box-card" :body-style="{ padding: '0px' }">

          <div slot="header" class="clearfix">
            <span class="person_body_list" style="border-bottom: none"
              >个人中心</span
            >
          </div>
          <el-menu
            router
            active-text-color="#00c3ff"
            class="el-menu-vertical-demo"
          >
            <el-menu-item
              index="info"
              :route="{ name: 'infoView', params: $route.params.id }"
            >
              <i class="el-icon-user"></i>
              <span slot="title">个人简介</span>
            </el-menu-item>
            <el-menu-item
              index="myarticle"
              :route="{ name: 'myarticle', params: $route.params.id }"
            >
              <i class="el-icon-edit-outline"></i>
              <span slot="title">发帖</span>
            </el-menu-item>
            <el-menu-item
              index="mycollect"
              :route="{ name: 'mycollect', params: $route.params.id }"
            >
              <i class="el-icon-document"></i>
              <span slot="title">收藏</span>
            </el-menu-item>
            <el-menu-item
              index="myfan"
              :route="{ name: 'myfan', params: $route.params.id }"
            >
              <i class="el-icon-tableware"></i>
              <span slot="title">粉丝</span>
            </el-menu-item>
            <el-menu-item
              index="myfollow"
              :route="{ name: 'myfollow', params: $route.params.id }"
            >
              <i class="el-icon-circle-plus-outline"></i>
              <span slot="title">关注</span>
            </el-menu-item>
          </el-menu>
        </el-card>
     
    </div>

    <div class="person_body_right">
        <router-view></router-view>
      </div>

        </div>
    </div>

  </div>
</template>

<script>
export default {
    name: 'EditView',
}
</script>

<style>
     .nav{
        height: 60px;
         width: 100%;
         background:#5f9592 ;
         position: fixed;
         top: 0;
         bottom: 0;
         left:0;
         display: flex; 
        justify-content: center; 
        align-items: center; 
     }

     .button-style {
  width: 200px;
  font-size: 20px;
  margin-top: 40px;
  margin: 20px;
  text-align: center;
}
     .back1{
        position: absolute;
      top: 60px; /* 紧贴在 .nav 元素的下方 */
      left: 0;
      height: 900px;
      width: 100%;
      display: flex;
     }

     .back2{
    top:60px;
    bottom: 20px;
    display: flex;
    width: calc(100% - 200px); /* 宽度为100%减去左右边距 */
    height: 100%;
    margin-left: 100px; /* 左边距 */
    margin-right: 100px; /* 右边距 */
   background-color: transparent; /* 设置背景色为透明 */
   position: absolute; /* 将叠加层设为绝对定位 */ 
     }

     .person_body_left {
  width: 20%;
  height: 600px;
  border-radius: 5px;
  margin-right: 3%;
  text-align: center;
  position: fixed;
}

.person_body_list {
  width: 100%;
  height: 50px;
  margin-top: 25px;
  font-size: 22px;
  border-bottom: 1px solid #f0f0f0;
}
.person_body_right {
  width: 75%;
  /* height: 500px; */
  height: 600px;
  border-radius: 5px;
  background-color: white;
  position: absolute;
  right: 0; /* 往右边靠 */
  top: 0; /* 从顶部开始 */
}

</style>