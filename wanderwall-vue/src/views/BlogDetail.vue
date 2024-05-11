<template>
  <Header/>
  <input type="file" ref="fileInput" style="display: none" />
  <div class="main-container">
    <div class="blog-detail-container" v-if="!isEdit">
      <div class="cover">
        <img :src="getCover(blog.cover)" :alt="请上传封面" class="blog-cover">
      </div>
<!--      <div class="shadow"></div>-->

      <div class="tag">
        <template v-if="blog.province !== ''">
          <span class="tag-text">{{ blog.province }}</span>
          <div class="border"></div>
        </template>
        <template v-if="blog.city !== ''&&blog.city !== '市辖区'">
          <span class="tag-text">{{ blog.city }}</span>
          <div class="border"></div>
        </template>
        <template v-if="blog.area !== ''">
          <span class="tag-text">{{ blog.area }}</span>
        </template>
      </div>

      <span class="title">{{ blog.title }}</span>
      <div class="author-time">
        <span class="author">by {{ blog.author }} |</span>
        <span class="time">{{blog.date || this.getcurrentdate()}}</span>
      </div>
    </div>

    <div class="container" v-if="isEdit">
      <div class="cover">
        <img :src="getCover(temporaryblog.cover)" :alt="请上传封面" class="blog-cover" >
      </div>
      <div class="shadow" style="cursor: pointer;" @click="changecover"></div>

      <div class="tag">
        <template v-if="temporaryblog.province !== ''">
          <span class="tag-text">{{ temporaryblog.province }}</span>
          <div class="border"></div>
        </template>
        <template v-if="temporaryblog.city !== ''&&temporaryblog.city !== '市辖区'">
          <span class="tag-text">{{ temporaryblog.city }}</span>
          <div class="border"></div>
        </template>
        <template v-if="temporaryblog.area !== ''">
          <span class="tag-text">{{ temporaryblog.area }}</span>
        </template>
      </div>

      <template v-if="!titleedit">
        <span class="title" @click="tiedit">{{ temporaryblog.title }}</span>
      </template>

      <template v-if="titleedit">
        <el-input class="input-title" v-model="temporaryblog.title" autosize type="textarea"></el-input>
      </template>

      <div class="author-time">
        <span class="author">by {{ temporaryblog.author }} |</span>
        <span class="time">{{temporaryblog.date || this.getcurrentdate()}}</span>
      </div>
      
    </div>
    <el-button-group class="button" v-if="!isEdit">

      <el-button class="black-button" @click="Edit"><el-icon>
          <Edit />
        </el-icon>编辑</el-button>
      <el-button class="black-button" @click="Delete"><el-icon>
          <Delete />
        </el-icon>删除
      </el-button>
    </el-button-group>
    <template v-else>
    <div style="display:flex; width: 76%; margin-left:12%">
      <div style="flex:1; text-align:right">
       
        <el-button-group class="button" >
          <el-button class="black-button" @click="Save"><el-icon>
              <DocumentChecked />
            </el-icon>保存</el-button>
          <el-button class="black-button" @click="Delete"><el-icon>
              <Delete />
            </el-icon>删除
          </el-button>
          <el-button class="black-button" @click="Reset"><el-icon>
            <RefreshLeft />
          </el-icon>重置
          </el-button>
          <el-button class="black-button" @click="addtextbox(this.temporaryblog.paragh.length, 1)"><el-icon>
              <ChatLineSquare />
            </el-icon>添加文本框</el-button>
          <el-button class="black-button" @click="addimage(this.temporaryblog.paragh.length, 1)"><el-icon>
              <Picture />
            </el-icon>添加图片</el-button>
        </el-button-group>
      </div>
      <div style="flex:1; text-align:left">
        <el-cascader class="cascader" size='default' :options='options' v-model='selectedoptions' @change='addressChange'
          placeholder="请选择地区"></el-cascader>
      </div>
    </div>
  </template>

    <div v-for="(item, index) in blog.paragh" :key="index" class="aside-description" v-if="!isEdit">
      <template v-if="item.type === 'text' && item.content != ''">
        <span class="blog-text">{{ item.content }}</span><br />
      </template>
      <template v-else-if="item.type === 'title' && item.content != ''">
        <span class="text-title">{{ item.content }}</span><br />
      </template>
      <template v-else-if="item.type === 'image'">
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture">
        <span class="picture-desc">{{ item.dsc }}</span><br />
      </template>
    </div>


    <div v-for="(item, index) in temporaryblog.paragh" :key="index" class="aside-description" v-if="isEdit">
      <template v-if="item.type === 'text' && item.editing">
        <el-button-group class="button">
          <el-button class="black-button" style="" @click="addtextbox(index, 1)">在上方插入文本框</el-button>
          <el-button class="black-button" style="" @click="addtitle(index, 1)">在上方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 1)">在上方插入图片</el-button>
          <el-button class="black-button" @click="deleteitem(index)">删除此元素
          </el-button>
        </el-button-group>
        <el-input v-model="item.content" class="blog-text" autosize type="textarea"></el-input>
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index, 0)">在下方插入文本框</el-button>
          <el-button class="black-button" @click="addtitle(index, 0)">在下方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 0)">在下方插入图片
          </el-button>
        </el-button-group>
        <br />
      </template>
      <template v-else-if="item.type === 'text' && !item.editing">
        <span class="blog-text" @click="textedit(index)">{{ item.content }}</span><br />
      </template>

      <template v-if="item.type === 'title' && item.editing">
        <el-button-group class="button">
          <el-button class="black-button" style="" @click="addtextbox(index, 1)">在上方插入文本框</el-button>
          <el-button class="black-button" style="" @click="addtitle(index, 1)">在上方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 1)">在上方插入图片</el-button>
          <el-button class="black-button" @click="deleteitem(index)">删除此元素
          </el-button>
        </el-button-group>
        <el-input v-model="item.content" class="text-title" autosize type="textarea"></el-input>
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index, 0)">在下方插入文本框</el-button>
          <el-button class="black-button" @click="addtitle(index, 0)">在下方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 0)">在下方插入图片
          </el-button>
        </el-button-group>
        <br />
      </template>
      <template v-else-if="item.type === 'title' && !item.editing">
        <span class="text-title" @click="textedit(index)">{{ item.content }}</span><br />
      </template>

      <template v-else-if="item.type === 'image' && !item.editing">
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture" @click="textedit(index)">
        <span class="picture-desc" @click="textedit(index)">{{ item.dsc }}</span><br />
      </template>
      <template v-else-if="item.type === 'image' && item.editing">
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index, 1)">在上方插入文本框</el-button>
          <el-button class="black-button" @click="addtitle(index, 1)">在上方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 1)">在上方插入图片</el-button>
          <el-button class="black-button" @click="deleteitem(index)">删除此元素
          </el-button>
        </el-button-group>
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture">
        <el-input v-model="item.dsc" class="picture-desc" autosize type="textarea"></el-input>
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index, 0)">在下方插入文本框</el-button>
          <el-button class="black-button" @click="addtitle(index, 0)">在下方插入文本标题</el-button>
          <el-button class="black-button" @click="addimage(index, 0)">在下方插入图片
          </el-button>
        </el-button-group>
        <br /><br />
      </template>
    </div>

  </div>
</template>

<script>

import {
  Delete,
  Edit,
} from '@element-plus/icons-vue'
import { regionData, codeToText } from 'element-china-area-data'
import Header from "@/components/Header.vue";

export default {
  name: "blog-detail",
  components:{Header},
  props: {
    id: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      options: regionData,
      isinsert: false,
      isEdit: false,
      titleedit: false,
      curabove: '',
      curIndex: '',
      temporaryblog: {},
      selectedoptions: ['110000', '110100', '110101'],
      blog: {
        id: 1,
        title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives',
        cover: 'cover.png',
        author: 'Yunhai',
        date: '6月1日 , 2023',
        province:'海南省',
        city:'三亚市',
        area:'天涯区',
        paragh: [
          {
            type: 'text',
            editing: false,
            content: "Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar."
          },
          {
            type: 'text',
            editing: false,
            content: " Aside from scuba diving, the prime attraction in Fulidhoo is the shark and stingray beach, where you can see nurse sharks, sea turtles, and stingrays in the shallow water and even get your picture taken with them. Of course, Fulidhoo also has the white sand and clear blue water that's typical of the Maldives, and there are family-owned hotels for as low as $60 USD per night, so it's a good option if you're wanting to get your fix of the tropics without spending a lot of money."
          },
          {
            type: 'text',
            editing: false,
            content: "This travel guide will explain how to get to Fulidhoo island, what to do there, where to stay, and everything else you need to know before you go!"
          },
          {
            type: 'title',
            editing: false,
            content: "Where To Stay In Fulidhoo"
          },
          {
            type: 'image',
            editing: false,
            src: 'pict4.png',
            dsc: "Kinan Retreat"
          },
          {
            type: 'title',
            editing: false,
            content: "How To Get To Fulidhoo Island"
          },
          {
            type: 'text',
            editing: false,
            content: "There’s no airport in Fulidhoo, so you first have to fly into the capital of the Maldives at Male International Airport (MLE), which is about 55 kilometers (35 miles) from Fulidhoo.\nFrom Male, you can take a speedboat or ferry directly to Fulidhoo."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict1.png',
            dsc: "Swimming with a stingray in Fulidhoo!"
          },
          {
            type: 'text',
            editing: false,
            content: "The main way to get to Fulidhoo is by public speedboat, and that’s how we went. There are daily departures in the morning and afternoon from Male airport and the city.\nThe speedboat journey to Fulidhoo takes a little less than 1.5 hours and costs about $40 USD per person one way. It was very bumpy in our experience, but I’m sure it depends on the weather.\nOn the bright side, the boats are new and the seats are comfortable. They also have a toilet and plenty of room for storing luggage, and they give you free bottled water during the ride."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict6.png',
            dsc: "Tour boat looking at the stingrays in Fulidhoo"
          },
          {
            type: 'text',
            editing: false,
            content: "The boats from Male to Fulidhoo depart at 10 AM and 4 PM (Fridays 9:30 AM only). And the boats return from Fulidhoo to Male at 7:30 AM and 1:30 PM (Fridays 7 AM only).\nHowever, keep in mind there are multiple speedboat companies operating this route and the timetable seems to change occasionally, so I’d double check the timetable with your hotel before you go."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict2.png',
            dsc: "Huvan Inn"
          },
          {
            type: 'text',
            editing: false,
            content: "You can ask your hotel in Fulidhoo to book the speedboat for you, and then pay for it with your room bill when you arrive. That’s what we did, and paying by credit card spared us from having to carry lots of cash everywhere."
          }
        ]

      },
      blogcopy: {
        id: 1,
        title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives',
        cover: 'cover.png',
        author: 'David & Intan',
        date: '6月1日 , 2023',
        province:'北京市',
        city:'市辖区',
        area:'海淀区',
        paragh: [
          {
            type: 'text',
            editing: false,
            content: "Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar."
          },
          {
            type: 'text',
            editing: false,
            content: " Aside from scuba diving, the prime attraction in Fulidhoo is the shark and stingray beach, where you can see nurse sharks, sea turtles, and stingrays in the shallow water and even get your picture taken with them. Of course, Fulidhoo also has the white sand and clear blue water that's typical of the Maldives, and there are family-owned hotels for as low as $60 USD per night, so it's a good option if you're wanting to get your fix of the tropics without spending a lot of money."
          },
          {
            type: 'text',
            editing: false,
            content: "This travel guide will explain how to get to Fulidhoo island, what to do there, where to stay, and everything else you need to know before you go!"
          },
          {
            type: 'title',
            editing: false,
            content: "Where To Stay In Fulidhoo"
          },
          {
            type: 'image',
            editing: false,
            src: 'pict4.png',
            dsc: "Kinan Retreat"
          },
          {
            type: 'title',
            editing: false,
            content: "How To Get To Fulidhoo Island"
          },
          {
            type: 'text',
            editing: false,
            content: "There’s no airport in Fulidhoo, so you first have to fly into the capital of the Maldives at Male International Airport (MLE), which is about 55 kilometers (35 miles) from Fulidhoo.\nFrom Male, you can take a speedboat or ferry directly to Fulidhoo."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict1.png',
            dsc: "Swimming with a stingray in Fulidhoo!"
          },
          {
            type: 'text',
            editing: false,
            content: "The main way to get to Fulidhoo is by public speedboat, and that’s how we went. There are daily departures in the morning and afternoon from Male airport and the city.\nThe speedboat journey to Fulidhoo takes a little less than 1.5 hours and costs about $40 USD per person one way. It was very bumpy in our experience, but I’m sure it depends on the weather.\nOn the bright side, the boats are new and the seats are comfortable. They also have a toilet and plenty of room for storing luggage, and they give you free bottled water during the ride."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict6.png',
            dsc: "Tour boat looking at the stingrays in Fulidhoo"
          },
          {
            type: 'text',
            editing: false,
            content: "The boats from Male to Fulidhoo depart at 10 AM and 4 PM (Fridays 9:30 AM only). And the boats return from Fulidhoo to Male at 7:30 AM and 1:30 PM (Fridays 7 AM only).\nHowever, keep in mind there are multiple speedboat companies operating this route and the timetable seems to change occasionally, so I’d double check the timetable with your hotel before you go."
          },
          {
            type: 'image',
            editing: false,
            src: 'pict2.png',
            dsc: "Huvan Inn"
          },
          {
            type: 'text',
            editing: false,
            content: "You can ask your hotel in Fulidhoo to book the speedboat for you, and then pay for it with your room bill when you arrive. That’s what we did, and paying by credit card spared us from having to carry lots of cash everywhere."
          }
        ]

      },
      blogs: [
        // 假设的博客数据
        {
          id: 1, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover1.png',
          content: 'Aside from scuba diving, the prime attraction in Fulidhoo is the shark and stingray beach, where you can see nurse sharks, sea turtles, and stingrays in the shallow water and even get your picture taken with them.'
        },
        {
          id: 2, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png',
          content: 'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'
        },
        {
          id: 3, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover3.png',
          content: 'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'
        },

        {
          id: 4, title: 'Fulidhoo Island Guide: Shark & Stingray Beach In Maldives', cover: 'cover3.png',
          content: 'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'
        },
        {
          id: 5, title: 'How To Visit Dhigurah Island: Budget Paradise In Maldives', cover: 'cover2.png',
          content: 'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'
        },
        {
          id: 6, title: '10 Best Hikes In Aruba: Family Friendly Trails', cover: 'cover1.png',
          content: 'Fulidhoo is a small, budget-friendly local island we visited recently in the Maldives, along with Dhigurah island, which we loved for its beaches and sandbar.'
        },
        // 更多博客...
      ],
     
    };
  },

  created() {

  },

  methods: {
    getcurrentdate() {
      const today = new Date();
      const dd = String(today.getDate());
      const mm = String(today.getMonth() + 1); // 月份是从0开始的
      const yyyy = today.getFullYear();

      return  mm+'月' + dd + '日 '+ ', '+yyyy
    },
    getCover(cover) {
      return require(`@/assets/blogimage/${cover}`);
    },
    Edit() {
      this.isEdit = !this.isEdit;
      this.temporaryblog = JSON.parse(JSON.stringify(this.blog));
    },
    Save() {
      this.isEdit = !this.isEdit;
      this.temporaryblog.paragh.forEach((item) => {
          item.editing = false;
      });

      this.titleedit = false;
      this.blog = JSON.parse(JSON.stringify(this.temporaryblog));
      /*this.blogcopy = this.temporaryblog;*/
      this.blog.date = this.getcurrentdate();
    },

    Reset() {
      this.temporaryblog.paragh.forEach((item) => {
          item.editing = false;
      });
      this.selectedOptions = [];
      this.titleedit = false;
      this.temporaryblog = JSON.parse(JSON.stringify(this.blogcopy));
      console.log(this.blogcopy);
    },

    tiedit() {
      this.titleedit = !this.titleedit;
    },

    addressChange(arr) {
      this.temporaryblog.province=codeToText[arr[0]];
      this.temporaryblog.city=codeToText[arr[1]];
      this.temporaryblog.area=codeToText[arr[2]];
    },

    textedit(index) {
      // 将其他段落的 editing 属性设为 false
      this.temporaryblog.paragh.forEach((item, i) => {
        if (i !== index) {
          if (this.temporaryblog.paragh[i].content === '') {
            this.temporaryblog.paragh.splice(i, 1);
          }
          else
            item.editing = false;
        }
      });
      this.titleedit = false;

      // 将当前段落的 editing 属性设为 true
      this.temporaryblog.paragh[index].editing = true;
    },
    deleteitem(index) {
      this.temporaryblog.paragh.splice(index, 1);
    },
    addtextbox(index, above) {
      // 向 temporaryblog.paragh 数组添加一个新的文本段落
      this.temporaryblog.paragh.forEach((item) => {
        item.editing = false;
      });
      if (!above) {
        index++; // 如果是在下方插入，需要加 1
      }

      this.temporaryblog.paragh.splice(index, 0, {
        type: 'text',
        editing: true,
        content: ""
      });
      this.temporaryblog.paragh.forEach((item, i) => {
        if (i !== index) {
          if (item.content === '') {
            this.temporaryblog.paragh.splice(i, 1);
          }
          else
            item.editing = false;
        }
      });
      this.titleedit = false;
    },

    addtitle(index, above) {
      // 向 temporaryblog.paragh 数组添加一个新的文本段落
      this.temporaryblog.paragh.forEach((item) => {
        item.editing = false;
      });
      if (!above) {
        index++; // 如果是在下方插入，需要加 1
      }

      this.temporaryblog.paragh.splice(index, 0, {
        type: 'title',
        editing: true,
        content: ""
      });
      this.temporaryblog.paragh.forEach((item, i) => {
        if (i !== index) {
          if (item.content === '') {
            this.temporaryblog.paragh.splice(i, 1);
          }
          else
            item.editing = false;
        }
      });
      this.titleedit = false;
    },
    addimage(index, above) {
      this.curIndex = index//记录要操作的下标  因为handleChangeCallback回调的时候参数就不对了
      this.curabove = above//记录是上面插入还是下面插入  因为handleChangeCallback回调的时候参数就不对了
      this.isinsert = false//记录当前是否是插入完成  false未完成
      // 向 temporaryblog.paragh 数组添加一个新的图片段落
      this.temporaryblog.paragh.forEach((item) => {
        item.editing = false;
      });
      if (!above) index++;
      this.$refs.fileInput.value = ''
      this.$refs.fileInput.click();

      const handleChangeCallback = () => {
        return (event) => {
          let iidex = !this.curabove ? (this.curIndex + 1) : this.curIndex//这个回调里面进行修改要插入的下标
          if (this.isinsert) return//如果已经插入完成就不要进行后面插入  就不会反复插入了
          this.isinsert = true//如果是刚进来  未插入  就修改成true  这样再次进入这个方法的时候  就不会继续插入
          // 在这里可以使用index参数
          const file = event.target.files[0];
          const filename = file.name;
          this.temporaryblog.paragh.splice(iidex, 0, {//iidex插入下标改了
            type: 'image',
            editing: true,
            src: filename,
            dsc: ""
          });
        };
      };

      // 添加带有捕获index参数的回调函数
      this.$refs.fileInput.addEventListener('change', handleChangeCallback());

      // 使用相同的回调函数来移除监听器
      this.$refs.fileInput.removeEventListener('change', handleChangeCallback());
      this.temporaryblog.paragh.forEach((item, i) => {
        if (i !== index) {
          if (item.content === '') {
            this.temporaryblog.paragh.splice(i, 1);
          }
          else
            item.editing = false;
        }
      });
      this.titleedit = false;

    },

    changecover() {
      this.isinsert = false

      this.temporaryblog.paragh.forEach((item) => {
        item.editing = false;
      });
      this.$refs.fileInput.value = ''
      this.$refs.fileInput.click();

      const handleChangeCoverCallback = () => {
        return (event) => {
          let iidex = !this.curabove ? (this.curIndex + 1) : this.curIndex//这个回调里面进行修改要插入的下标
          if (this.isinsert) return//如果已经插入完成就不要进行后面插入  就不会反复插入了
          this.isinsert = true//如果是刚进来  未插入  就修改成true  这样再次进入这个方法的时候  就不会继续插入
          // 在这里可以使用index参数
          const file = event.target.files[0];
          const filename = file.name;
          this.temporaryblog.cover = filename;
        };
      };

      // 添加带有捕获index参数的回调函数
      this.$refs.fileInput.addEventListener('change', handleChangeCoverCallback());

      // 使用相同的回调函数来移除监听器
      this.$refs.fileInput.removeEventListener('change', handleChangeCoverCallback());


    },
  }
};


</script>

<style src="@/assets/css/blog-detail.css"></style>