<template>
  <Header/>
  <div class="blog-main-container">
    <div class="group">
      <div class="paragraph">
        <span class="blog-heading">Blog</span
        ><span class="blog-subheading">Travel Records</span>
      </div>
      <div class="blog-image-background"></div>
      <div class="overlay"></div>
      <div class="paper-edge"></div>
    </div>

    <span class="blog-create">Add Blog</span>
    <div class="black-background"></div>
    <div class="create-frame" @click="$router.push('/createblog')" >
      <div class="create">Create a new blog...</div>
    </div>
    <span class="blog-create">Current blogs</span>
    <div class="black-background"></div>

    <div class="blog-grid">
      <div v-for="(blog, index) in paginatedBlogs" :key="index" class="blog-item">
      <router-link :to="{ name: 'blog-detail', params: { id: blog.blog_id } }" class="router-link-custom">
        <img :src="blog.cover" :alt="blog.title" class="blog-cover">
        <h2 class="blog-title">{{ blog.title }}</h2>
        <div class="divider"></div>
        <p class="blog-description">{{ blog.content }}</p>
      </router-link>
      </div>
    </div>
    <el-button-group>
      <el-button class="black-button" @click="prevpage"><el-icon class="el-icon--left"><ArrowLeft /></el-icon>Prev</el-button>
      <el-button class="black-button"  @click="nextpage">
        Next<el-icon class="el-icon--right"><ArrowRight /></el-icon>
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
        // { blog_id: 1, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover1.png' ,
        // content:'Aside from scuba diving, the prime attraction in Fulidhoo is the shark and stingray beach, where you can see nurse sharks, sea turtles, and stingrays in the shallow water and even get your picture taken with them.'},
        // { blog_id: 2, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png' ,
        // content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        // { blog_id: 3, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover3.png' ,
        // content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        //
        // { blog_id: 4, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover3.png' ,
        // content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        // { blog_id: 5, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png' ,
        // content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        // { blog_id: 6, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover1.png' ,
        // content:'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'},
        // 更多博客...
      ],
      currentPage: 1,
      pageSize: 3
    };
  },
  created() {
    this.blogList();
  },
  methods:{
    // getCover(cover) {
    //   return require(`@/assets/images/${cover}`);
    // },

    blogList(){
      this.$http.get("/user/get_blog_list").then(res => {
        console.log(res);
        this.blogs = (res != undefined)?res:this.blogs;
        console.log(this.blog);
      });
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
