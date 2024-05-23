<template>
    <div>
      <div class="alluser">
        <div class="head">
          <div class="headsearch">
            <el-input placeholder="请输入想要认识的人的id或名字" v-model="searchUser">
              <i
                slot="suffix"
                style="cursor: pointer"
                class="el-input__icon el-icon-search"
                @click="searchUserByIdOrName"
              ></i>
            </el-input>
          </div>
        </div>
        <div class="fanorfollow_box">
          <div
            class="fanorfollow"
            v-for="item in displayedFansAndFollows"
            :key="item.id"
          >
            <div class="fanorfollow_left">
              <img class="fanorfollow_img" src="~@/assets/avatar/male.png"  @click="$router.push('/RegisterView')" />
            </div>
            
            <div class="fanorfollow_info">
              <div class="fanorfollow_info_top">
                <span @click="personal(item.id)">{{ item.nickname }}</span>
              </div>
              <div class="fanorfollow_info_bottom">
                <span @click="personal(item.id)">{{ item.design }}</span>
              </div>
            </div>
            <div class="fanorfollow_botton">
              <el-button
                @click="toggleFollow(item.id)"
                type="primary"
                size="small"
                round
                icon="el-icon-check"
              >
                {{ isFollowing(item.id) ? '已关注' : '关注' }}
              </el-button>
            </div>
          </div>
          <div>
            <el-empty
              v-if="displayedFansAndFollows.length === 0"
              :image-size="250"
              description="没有找到相关用户"
            ></el-empty>
          </div>
        </div>
      </div>
        <!-- 分页组件 -->
    <div class="block">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="fansAndFollows.length">
      </el-pagination>
    </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "MyFanAndFollow",
    data() {
      return {
        fansAndFollows: [
          // 模拟数据集
          { id: 1, nickname: '用户1', avatar: '~@/assets/ava.png', isFollowing: false },
          { id: 2, nickname: '用户2', avatar: '~@/assets/ava.png', isFollowing: false},
          { id: 3, nickname: '用户3', avatar: '~@/assets/ava.png', isFollowing: false },
          { id: 4, nickname: '用户4', avatar: '~@/assets/ava.png', isFollowing: false },
          { id: 5, nickname: '用户5', avatar: '~@/assets/ava.png', isFollowing: false},
          { id: 6, nickname: '用户1', avatar: '~@/assets/ava.png', isFollowing: false },
          // ...更多用户
        ],
        searchUser: '',
        displayedFansAndFollows: [], // 用于显示搜索结果或全部用户
        currentPage: 1, // 当前页码
      pageSize: 5, // 每页显示的好友数量
      };
    },
    methods: {
      personal(id) {
        this.$router.push({ path: `/newsuser/personal/${id}` });
      },
      toggleFollow(id) {
      const index = this.fansAndFollows.findIndex(item => item.id === id);
      if (index !== -1) {
        // 如果当前用户已经关注，则取消关注
        if (this.fansAndFollows[index].isFollowing) {
          this.fansAndFollows[index].isFollowing = false;
        } else {
          // 如果当前用户未关注，则关注
          this.fansAndFollows[index].isFollowing = true;
        }
        // 更新显示的用户列表，以便反映关注状态的改变
        this.updateDisplayedUsers();
      }
    },
      isFollowing(id) {
        return this.fansAndFollows.some(item => item.id === id && item.isFollowing);
      },
      searchUserByIdOrName() {
        if (this.searchUser) {
          this.displayedFansAndFollows = this.fansAndFollows.filter(item =>
            item.nickname.includes(this.searchUser) || item.id.toString().includes(this.searchUser)
          );
        } else {
          // 如果搜索框为空，显示全部用户
          this.updateDisplayedUsers();
        }
      },
      handleCurrentChange(val) {
      this.currentPage = val;
      this.updateDisplayedUsers();
    },
    updateDisplayedUsers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = this.currentPage * this.pageSize;
      this.displayedFansAndFollows = this.fansAndFollows.slice(start, end);
    },
    },
    mounted() {
      // 初始化时显示全部用户
      this.updateDisplayedUsers();
    },
    
    /*勾子函数 */
    // goToProfile() {
    // //   this.$router.push({ path: `/newsuser/personal/${id}` });
    // this.$router.push({ path: `/RegisterView` });
    // },
  
  
  };
  </script>
  
  <style>
  /* 这里添加你的样式 */
  fanorfollow_box:hover {
  border-width: 1px;
  border-color: deepskyblue;
  }
  
  .fanorfollow {
  padding: 15px 40px 15px 30px;
  height: 50px;
  display: flex;
  align-items: center;
  border: 1px solid #ebebeb;
  }
  
  .fanorfollow:hover {
  border-width: 1px;
  border-color: deepskyblue;
  }
  
  .fanorfollow_left {
  width: 60px;
  height: 60px;
  }
  
  .fanorfollow_img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 1px solid #ebebeb;
  vertical-align: top;
  }
  
  .fanorfollow_info {
  display: inline-block;
  margin-left: 20px;
  -webkit-box-flex: 1;
  -ms-flex-positive: 1;
  flex-grow: 1;
  overflow: hidden;
  }
  
  .fanorfollow_info_top {
  display: inline-block;
  font-size: 10px; /* 修正字体大小单位为 px */
  line-height: 14px;
  vertical-align: top;
  cursor: pointer;
  }
  
  .fanorfollow_info_top:hover {
  color: deepskyblue;
  }
  
  .fanorfollow_info_bottom {
  line-height: 14px;
  color: #999;
  margin-top: 5px;
  cursor: pointer;
  }
  .fanorfollow_img:hover {
  cursor: pointer;
  }
  
  </style>
  