<template>
  <div>
    <div class="alluser">
      <div class="head">
        <h3>好友列表</h3>
        <div class="headsearch" style="border: none;">
          <ElInput
    class="custom-input"
    placeholder="请输入想要搜索的好友的id或名字"
    v-model="searchUser"
    border=none
  >
    <template #append>
      <el-icon class="search-icon" style="cursor: pointer;" @click="searchUserByIdOrName"><Search /></el-icon>
    </template>
  </ElInput>
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
        @click="deleteFriend(item.id)"
        type="danger"
        size="small"
        round
        icon="delete"
      >
        删除
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
      v.model:current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="fansAndFollows.length">
    </el-pagination>
  </div>
  </div>
</template>

<script>
/*需要从后端传入的数据有：fansAndFollows: [{ id: 1, nickname: '用户1', avatar: '~@/assets/avatar/male.png', isFollowing: true}, ]*/
/*相互传输isFollowing实现好友添加或者删除 */
import { ref } from 'vue';
import { ElInput, Search } from 'element-plus';
export default {
  name: "MyFanAndFollow",
  components: {
    ElInput,
    Search,
  },
  data() {

    return {
      fansAndFollows: [
        // 模拟数据集
        { id: 1, nickname: '用户1', avatar: '~@/assets/avatar/male.png', isFollowing: true},
        { id: 2, nickname: '用户2', avatar: '~@/assets/avatar/male.png', isFollowing: true },
        { id: 3, nickname: '用户3', avatar: '~@/assets/avatar/male.png', isFollowing: true },
        { id: 4, nickname: '用户4', avatar: '~@/assets/avatar/male.png', isFollowing: true },
        { id: 5, nickname: '用户5', avatar: '~@/assets/avatar/male.png', isFollowing: true},
        { id: 6, nickname: '用户1', avatar: '~@/assets/avatar/male.png', isFollowing: true },
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
    /*仅此作为演示 */
    deleteFriend(id) {
      const index = this.fansAndFollows.findIndex(item => item.id === id);
      if (index !== -1) {
        this.fansAndFollows.splice(index, 1); // 移除好友
        this.updateDisplayedUsers(); // 更新显示的用户列表
      }
    },

    /* 后端实现 */
    // deleteFriend(id) {
    //   const index = this.fansAndFollows.findIndex(item => item.id === id);
    //   if (index !== -1) {
    //     // 设置 isFollowing 为 false，表示不再关注这个好友
    //     this.fansAndFollows[index].isFollowing = false;
    //     this.updateDisplayedUsers(); // 更新显示的用户列表

    //     // 发送请求到后端 API 来永久删除好友(后端实现)
    //     axios.delete(`/api/delete-friend/${id}`)
    //       .then(response => {
    //         console.log('Friend deleted successfully:', response.data);
    //         this.updateDisplayedUsers(); // 更新显示的用户列表
    //       })
    //       .catch(error => {
    //         console.error('Error deleting friend:', error);
    //       });
      // }



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
width: 50;
height: 50px;
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
font-size: 15px; /* 修正字体大小单位为 px */
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
.headsearch .el-input__inner {
  border: none;
}
.search-icon {
  cursor: pointer;
  /* 设置图标大小 */
  font-size: 24px; /* 根据需要调整大小 */
  width: 24px; /* 根据需要调整大小 */
  height: 24px; /* 根据需要调整大小 */
}
</style>
