<template>
  <div class="penci-header-wrap" style="margin-bottom: 0px " >
    <header id="header" class="header-header-6 has-bottom-line" itemscope="itemscope" style="background: black; margin: 0px;position: fixed;width: 100%">
      <div id="navigation-sticky-wrapper" class="sticky-wrapper" style="height: 82px;">
        <nav id="navigation" class="navigator" role="navigation" style="background: black; padding-top: 15px;height: 82px;border-color: black">
          <div class="header-container" >
            <div id="logo" style="padding-left: 7%">
              <a href="/">
                <img class="penci-mainlogo penci-limg penci-logo" src="../assets/wanderwall-logo.png" alt="The World Travel Guy" width="230" height="100"> </a>
            </div>
            <!--菜单-->
            <ul id="menu-menu-1" class="menu" style="padding-left: 10%;padding-right: 8%">
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/') }"><a href="/" style="font-size: 14px">Home</a></li>
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/tracemap/') }" ><a href="/tracemap/" style="font-size: 14px" >Trace Map</a></li>
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/relation-graph/') }" ><a href="/relation-graph/" style="font-size: 14px" >Travel Relation</a></li>
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/blog/') }"><a href="/blog/" style="font-size: 14px">Blog</a></li>
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/gallery/') }"><a href="/gallery/"style="font-size: 14px">Gallery</a></li>
              <li id="menu-item-1432" :class="{ 'megamenu menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item ajax-mega-menu menu-item-1432': isActive('/report') }"><a href="/report"style="font-size: 14px">Report</a></li>
            </ul>
            <!--未登录-->
            <div v-if = "!isLoggedIn" class="nav-v2-dynamic__logged-out">
              <a class="login-button" href="/login">Log in</a>
              <a class="signup-button" href="/register">Sign up</a>
            </div>
            <!--已登录-->
            <div v-else class="nav-v2-avatar">
              <a data-nav-v2-avatar="true" title="Open profile" href="/userinfo">
                <img class="nav-v2-avatar__image" alt="avatar" :src="avatarPath" />
              </a>
              <div class="nav-v2-profile" data-nav-v2-profile data-nav-event-category="Profile">
                <div class="nav-v2-profile__wrapper" data-nav-v2-profile-wrapper>
                  <a class="nav-v2-user" title="Open profile" data-nav-event-clicked="Profile" :href="avatarPath">
                    <img class="nav-v2-user__image" alt="avatar" width="80" height="80" :src="avatarPath" />
                    <div class="nav-v2-user__name">{{ user.username }}</div>
                  </a>
                  <div class="nav-v2-sessions">
                  </div>
                  <ul class="nav-v2-profile__menu">
                    <li class="nav-v2-profile__item">
                      <a data-nav-event-clicked="Work preferences" data-open-availability-modal="work-types" href="/userinfo">Pictures</a>
                    </li>
                    <li class="nav-v2-profile__item">
                      <a data-nav-event-clicked="Settings" href="/friend">Friends</a>
                    </li>
                    <li class="nav-v2-profile__item">
                      <a data-nav-event-clicked="Settings" href="/edit">Settings</a>
                    </li>
                    <li class="nav-v2-profile__item nav-v2-profile__item--divided">
                      <button @click="logout">Sign out</button>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </nav>
      </div>
    </header>
  </div>
</template>

<script>
export default {
  name: 'Header',
  style: require('../assets/css/homepage.css'),
  props: {
    msg: String
  },

  data(){
    return{
      isLoggedIn:true,
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):{
        username: "Suicidal Capybara",
      },
    }
  },
  computed:{
    avatarPath(){
      return "https://img0.baidu.com/it/u=1702833491,2146188759&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800";
    }
  },
  created() {
    this.checkLoginStatus();
  },
  methods:{
  checkLoginStatus() {
    this.isLoggedIn = !!this.user.username; // 已登录
  },
  isActive(path) {
    return this.$route.path === path; //根据不同网址选择高亮选项
  },
  logout(){
    // localStorage.removeItem("user");
    this.user={};
    this.checkLoginStatus();
  }}
}
</script>

<style scoped>
</style>
