<template>
  <Header/>
  <swiper
      :direction="'vertical'"
      :modules="modules"
      :pagination="{ clickable: true }"
  >
    <!--封面-->
    <swiper-slide>
      <div id="report-container">
        <div class="year-title">
          {{year}}
        </div>
        <video class="custom-video" autoplay muted loop>
          <source src="../assets/report/cover.mp4" type="video/mp4">
        </video>
        <div class="tips-title">下滑查看更多</div>
      </div>
    </swiper-slide>
    <!--Page 1-->
    <swiper-slide>
      <div id="report-container">
        <div class="right-text-overlay">
          <p class="right-text-p">这一年<br>
            你一共去了<span class="highlight">{{city_num}}</span>个城市<br>
            超过了<span class="highlight">{{exceed_friend_rate}}</span>的好友
          </p>

          <p class="right-text-p">你旅行目的地偏好<span class="highlight">{{fav_area}}</span><br>
            这里的哪些人、事和景吸引着你呢
          </p>
        </div>
      <video class="custom-video" autoplay muted loop>
        <source src="../assets/report/page1.mp4" type="video/mp4">
      </video>
      </div>
    </swiper-slide>
    <!--Page 2-->
    <swiper-slide>
      <div id="report-container">
        <div class="left-text-overlay">
          <p class="left-text-p">你最长时间的一次旅行持续<span class="highlight">{{longest_travel.day_num}}</span>天<br>
            在去年<span class="highlight">{{longest_travel.month}}</span>月份<br>
            你从<span class="highlight">{{longest_travel.route}}</span><br>
            足足去了<span class="highlight">{{longest_travel.city_num}}</span>个城市<br>
            这一定是一段很难忘的记忆吧
          </p>
        </div>
        <video class="custom-video" autoplay muted loop>
          <source src="../assets/report/page2.mp4" type="video/mp4">
        </video>
      </div>
    </swiper-slide>
    <!--Page 3-->
    <swiper-slide>
      <div id="report-container">
        <div class="right-text-overlay">
          <p class="right-text-p">{{year}}年<br>
            和你去过最多相同地方的好友是<span class="highlight">{{common_city.friend}}</span><br>
            去过的相同城市数量高达<span class="highlight">{{common_city.city_num}}</span>个<br>
            包括<span class="highlight">{{common_city.city_name}}</span><br>
            旅行偏好这么相似<br>
            下次出发，记得叫上ta哦<br>
          </p>
        </div>
        <video class="custom-video" autoplay muted loop>
          <source src="../assets/report/page3.mp4" type="video/mp4">
        </video>
      </div>
    </swiper-slide>
    <!--Page 4-->
    <swiper-slide>
      <div id="report-container">
        <div class="center-text-overlay">
          <p class="center-text-p">
            {{year}}<br>
            你的旅行Blog里出现得最多的词是<br>
            <span class="highlight" style="font-size: 36px">{{blog_preference.word}}</span><br>
            频率高达<span class="highlight" style="font-size: 24px">{{blog_preference.freq}}</span>次
          </p>
        </div>
        <div class="left-text-overlay">
          <p class="left-text-p" v-for="(item, index) in blog_preference.contentWithDate" :key="index">
            <span v-html="highlightWord(item.content, blog_preference.word)"></span> <br>
            -- {{ item.date }}
          </p>
        </div>
        <video class="custom-video" autoplay muted loop>
          <source src="../assets/report/page4.mp4" type="video/mp4">
        </video>
      </div>
    </swiper-slide>
    <!--Page 45-->
    <swiper-slide>
      <div id="report-container">
        <div class="left-text-overlay">
          <p class="left-text-p">
            {{special_travel.date}}<span class="highlight">{{special_travel.city}}</span><br>
            大概是很特别的一次旅行吧<br>
            你足足发了<span class="highlight">{{special_travel.blog_num}}</span>条Blog，<span class="highlight">{{special_travel.pic_num}}</span>张照片<br>
            遇见了什么让你这么开心呢
          </p>
        </div>
        <video class="custom-video" autoplay muted loop>
          <source src="../assets/report/page5.mp4" type="video/mp4">
        </video>
      </div>
    </swiper-slide>
<!--    结束-->
    <swiper-slide>
    <img src="../assets/report/end.png" alt="Image 2"></swiper-slide>
  </swiper>
</template>
<script>
import Header from "@/components/Header";
import { Pagination } from "swiper";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/swiper.min.css";
// import "swiper/modules/pagination/pagination.min.css";

export default {
  name:"AnnualReport",
  components: {
    Swiper,
    SwiperSlide,
    Header
  },
  data(){
    return{
      year:2024,
      city_num:5,
      exceed_friend_rate:"50%",
      fav_area:"长三角地区",
      longest_travel:{day_num:7,month:5,route:"扬州-杭州-南京",city_num:3},
      common_city:{friend:"鱼籽酱虾球",city_name:"南京,扬州,杭州...",city_num:3,},
      blog_preference:{
        word:"轻松拿下",
        freq:6,
        contentWithDate: [
          { content: "小小泰山，轻松拿下", date: "2024.2.29" },
          { content: "日行三万步，轻松拿下pyq第一名", date: "2024.4.15" },
          { content: "本人已轻松拿下环青海湖骑行，望周知", date: "2024.5.1" }
        ],
      },
      special_travel:{
        date:"5月12日",
        city:"长沙",
        blog_num:5,
        pic_num:20
      }
    }
  },
  methods: {
    highlightWord(content, word) {
      const regex = new RegExp(`(${word})`, 'gi');
      return content.replace(regex, '<span class="highlight">$1</span>');
    }
  },
  setup() {
    return {
      modules: [Pagination]
    };
  },
};
</script>
<style scoped>
.swiper {
  height: 100vh;
}
.swiper-slide {
  height: 80%;
  line-height: 100vh;
  font-size: 30px;
  text-align: center;
  background: linear-gradient(90deg, #000000, #6e5995);
}
.custom-video {
  width: 100%;
  max-width: 1980px;
}
#report-container {
  margin-top: 80px;
  width: 100%;
  height: 800px;
  position: relative;
  overflow: hidden;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
.year-title {
  position: absolute;
  top: 16%;
  left: 13%;
  transform: translateY(-50%);
  text-align: left;
  width: 100%;
  z-index: 2; /* 标题在视频上方 */
  font-size: 100px;
  color: #f0f0f0;
  font-weight: bold;
}
.tips-title {
  position: absolute;
  bottom: -30%;
  right: 5%;
  text-align: right;
  width: 100%;
  z-index: 2;/* 标题在视频上方 */
  font-size: 20px;
  color: #b0aeae;
  font-weight: bold;
}
.right-text-overlay {
  position: absolute;
  top: 10%; /* 或你需要的任何位置 */
  right: 5%; /* 或你需要的任何位置 */
  transform: translate(-20%, -10%); /* 居中文字 */
  color: white; /* 根据需要设置文字颜色 */
  font-size: 2rem; /* 根据需要设置文字大小 */
  text-align: right;
  z-index: 1; /* 确保文字在视频上方 */
}
.right-text-p{
  font-family: 'SimHei', sans-serif;
  margin-top: 30px;
  font-size: 24px;
  line-height: 1.5;
}
:deep(.highlight) {
  color: #ffdd00;
}
.left-text-overlay {
  position: absolute;
  bottom: 25%;
  left: 12%;
  transform: translate(-20%, -10%); /* 居中文字 */
  color: white;
  font-size: 2rem;
  text-align: left;
  z-index: 1; /* 确保文字在视频上方 */
}
.left-text-p{
  font-family: 'SimHei', sans-serif;
  margin-top: 30px;
  font-size: 24px;
  line-height: 1.5;
}
.center-text-overlay {
  position: absolute;
  top: 15%;
  left: 50%;
  transform: translate(-50%, -50%); /* 居中文字 */
  color: white;
  font-size: 2rem;
  text-align: center;
  z-index: 1; /* 确保文字在视频上方 */
}
.center-text-p{
  font-family: 'SimHei', sans-serif;
  margin-top: 30px;
  font-size: 24px;
  line-height: 1.5;
}
</style>
