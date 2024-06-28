<template>
    <div class="myart1">
      <article-item v-for="a in allData" :key="a.id" v-bind="a"/>
          <el-empty
          v-if="allData.length == 0"
          :image-size="250"
          description="暂未发表任何故事额"
        ></el-empty>
    </div>
  </template>
  
  <script>
  import ArticleItem from '@/components/ArticleItem.vue';
  export default {
    components: { ArticleItem },
    name: "MyArticle",
    data() {
      return {
        allData:[]
      };
    },
    mounted() {
      this.load();
    },
    methods: {
    //   load() {
    //     myArticle(this.$route.params.id)
    //       .then((res) => {
    //         console.log(res);
    //         this.allData=res.data
    //       })
    //       .catch((err) => {
    //         console.log(err);
    //       });
    //   },
     },
  };
  </script>
  
  <style>
    .myart1{
      line-height: 30px;
    }
  </style>
  