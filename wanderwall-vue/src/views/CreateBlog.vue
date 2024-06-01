<template>
  <Header/>
  <input type="file" ref="fileInput" style="display: none" />
  <div class="main-container">
    <div class="blog-detail-container" v-if="!isEdit">
      <div class="cover">
        <img :src="getCover(blog.cover)" :alt="请上传封面" class="blog-cover">
      </div>
      <div class="shadow"></div>
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
        <span class="author">by {{ blog.author }}  |</span>
        <span class="time">{{blog.date || this.getcurrentdate()}}</span>
      </div>
    </div>

    <div class="blog-detail-container" v-if="isEdit">
      <div class="cover">
        <img :src="getCover(temporaryblog.cover)" :alt="请上传封面" class="blog-cover">
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
        <el-input class="input-title" v-model="temporaryblog.title" autosize type="textarea"
          placeholder="请输入标题"></el-input>
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
      <template v-if="item.type === 'text' && item.content!==''">
        <span class="blog-text">{{ item.content }}</span><br />
      </template>
      <template v-else-if="item.type === 'title' && item.content !== ''">
        <span class="text-title">{{ item.content }}</span><br />
      </template>
      <template v-else-if="item.type === 'image'">
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture">
        <span class="picture-desc">{{ item.dsc }}</span><br />
      </template>
    </div>


    <div v-for="(item, index) in temporaryblog.paragh" :key="index" class="aside-description" v-if="isEdit">
      <template v-if="item.type === 'text'&& item.editing">
        <el-button-group class="button">
          <el-button class="black-button" style="" @click="addtextbox(index,1)">在上方插入文本框</el-button>
          <el-button class="black-button" @click="addimage(index,1)">在上方插入图片</el-button>
          <el-button class="black-button" @click="deleteitem(index)">删除此元素
          </el-button>
        </el-button-group>
        <el-input v-model="item.content" class="blog-text" autosize type="textarea"></el-input>
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index,0)">在下方插入文本框</el-button>
          <el-button class="black-button" @click="addimage(index,0)">在下方插入图片
          </el-button>
        </el-button-group>
        <br />
      </template>
      <template v-else-if="item.type === 'text'&& !item.editing">
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
        <el-input v-model="item.content" class="blog-text" autosize type="textarea"></el-input>
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

      <template v-else-if="item.type === 'image'&& !item.editing">
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture" @click="textedit(index)">
        <span class="picture-desc" @click="textedit(index)">{{ item.dsc }}</span><br />
      </template>
      <template v-else-if="item.type === 'image'&& item.editing">
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index,1)">在上方插入文本框</el-button>
          <el-button class="black-button" @click="addimage(index,1)">在上方插入图片</el-button>
          <el-button class="black-button" @click="deleteitem(index)">删除此元素
          </el-button>
        </el-button-group>
        <img :src="getCover(item.src)" :alt="item.dsc" class="picture">
        <el-input v-model="item.dsc" class="blog-text" autosize type="textarea"></el-input>
        <el-button-group class="button">
          <el-button class="black-button" @click="addtextbox(index,0)">在下方插入文本框</el-button>
          <el-button class="black-button" @click="addimage(index,0)">在下方插入图片
          </el-button>
        </el-button-group>
        <br />
      </template>
    </div>


  </div>
</template>

<script>

import { regionData, codeToText } from 'element-china-area-data'
import Header from "@/components/Header.vue"

export default {
  name: "createblog",
  style:require("@/assets/css/blog-detail.css" ),
  components: {Header},
  data() {
   return {
    options: regionData,
    isinsert:false,
    curabove:'',
    curIndex:'',
    isEdit: true,
    titleedit: false,
    selectedoptions: ['110000', '110100', '110101'],
    blog: {
        id: 1,
        title: '你的题目',
        author: 'Yunhai',
        cover: 'cover.png',
        date: '',
        province:'',
        city:'',
        area:'',
        paragh:[],
    },
    temporaryblog: {
        id: 1,
        title: '你的题目',
        author: 'Yunhai',
        cover: 'cover.png',
        date: '',
        province:'',
        city:'',
        area:'',
        paragh:[],
    }
    };
  },
  created() {
    this.addtextbox(0,1);
  },

  methods:{
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
      if (this.isEdit)
        this.addtextbox(0, 1);
    },

    Save() {
      this.isEdit = !this.isEdit;
      this.temporaryblog.paragh.forEach((item) => {
          item.editing = false;
      });

      this.titleedit = false;
      this.blog = JSON.parse(JSON.stringify(this.temporaryblog));
      this.blog.date = this.getcurrentdate();
    },

    tiedit() {
        this.titleedit = !this.titleedit;
        this.temporaryblog.paragh.forEach((item, i) => {
          if(item.content!=='')
             item.editing = false;
      });
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
          if(item.content==='')
          {
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
    addtextbox(index,above) {
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
          if(item.content==='')
          {
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
      if(!above) index++;
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
          if(item.content==='')
          {
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
}
  
</script>

<style >
</style>
