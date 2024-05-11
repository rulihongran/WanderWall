<template>
  <Header/>
  <div class="blog-main-container">
    <div class="group">
      <div class="paragraph">
        <span class="blog-heading">博客</span
        ><span class="blog-subheading">旅行记录</span>
      </div>
      <div class="paper-edge"></div>
      <div class="blog-image-background"></div>
      <div class="overlay"></div>
    </div>

    <span class="blog-create">新建博客</span>
    <div class="black-background"></div>
    <div class="create-frame" @click="$router.push('/createblog')" >
      <div class="create">创建一个新博客...</div>
    </div>
    <span class="blog-create">现有博客</span>
    <div class="black-background"></div>

    <div class="blog-grid">
      <div v-for="(blog, index) in paginatedBlogs" :key="index" class="blog-item">
      <router-link :to="{ name: 'blog-detail', params: { id: blog.id } }" class="router-link-custom">
        <img :src="getCover(blog.cover)" :alt="blog.title" class="blog-cover">
        <h2 class="blog-title">{{ blog.title }}</h2>
        <div class="divider"></div>
        <p class="blog-description">{{ blog.content }}</p>
      </router-link>
      </div>
    </div>
    <el-button-group>
      <el-button class="black-button" @click="prevpage"><el-icon class="el-icon--left"><ArrowLeft /></el-icon>上一页</el-button>
      <el-button class="black-button"  @click="nextpage">
        下一页<el-icon class="el-icon--right"><ArrowRight /></el-icon>
      </el-button>
    </el-button-group>
  </div>
</template>

<script>
import {
  ArrowLeft,
  ArrowRight,
} from '@element-plus/icons-vue';
import Header from "@/components/Header.vue"

export default {
  name: "Blog",
  style: require('../assets/css/blog.css'),
  components: {Header},
  data() {
    return {
      isinsert:false,
      curabove:'',
      curIndex:'',
      blogs: [
        // 假设的博客数据
        { id: 1, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover1.png' ,
        content:'Aside from scuba diving, the prime attraction in Fulidhoo is the shark and stingray beach, where you can see nurse sharks, sea turtles, and stingrays in the shallow water and even get your picture taken with them.'},
        { id: 2, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png' ,
        content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        { id: 3, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover3.png' ,
        content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},

        { id: 4, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover3.png' ,
        content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        { id: 5, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png' ,
        content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        { id: 6, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover1.png' ,
        content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        // 更多博客...
      ],
      currentPage: 1,
      pageSize: 3
    };
  },
  created() {

  },
  methods:{
    getCover(cover) {
      return require(`@/assets/images/${cover}`);
    },
    prevpage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextpage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    }
},
computed: {
    totalItems() {
      return this.blogs.length;
    },
    totalPages() {
      return Math.ceil(this.totalItems / this.pageSize);
    },
    paginatedBlogs() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.blogs.slice(start, end);
    }
  }
}
  
</script>
