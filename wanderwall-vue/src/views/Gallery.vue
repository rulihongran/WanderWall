<template>
  <Header/>
  <input type="file" ref="fileInput" style="display: none" />
  <div class="main-container">
    <div class="group">
      <div class="paragraph">
        <span class="gallery-heading">画廊</span
        ><span class="gallery-subheading">旅行的点点滴滴</span>
      </div>
      <div class="paper-edge"></div>
      <div class="image-background"></div>
      <div class="gallery-overlay"></div>
    </div>

    <template v-if="!isEdit">
    <el-button-group class="button">
      <el-button class="black-button" @click="Edit"><el-icon>
          <Edit />
        </el-icon>编辑</el-button>
    </el-button-group>
  </template>

<template v-else>
    <el-button-group class="button">
      <el-button class="black-button" @click="Post"><el-icon><Picture /></el-icon>上传
      </el-button>
      <el-button class="black-button" @click="Save"><el-icon><DocumentChecked /></el-icon>保存
    </el-button>
      <el-button class="black-button" @click="Reset"><el-icon><RefreshLeft /></el-icon>恢复</el-button>
    </el-button-group>
  </template>

<div class="black-divider"></div>
<div class="gallery-grid">

  <template v-if="!isEdit">
      <div v-for="(image, index) in images" :key="index" class="gallery-item">
        <div>
          <el-image :src="getCover(image.src)" :alt="显示失败" :preview-src-list="imgList" :initial-index=index class="gallery-show"></el-image>
          <div class="hover-image"></div>
          <span class="gallery-imgdsc">{{ image.dsc }}</span>
        </div>
      </div>
    </template>

  <template v-if="isEdit">
    <div v-for="(image, index) in temporaryimages" :key="index" class="gallery-item">

         <template v-if="!images[index].isediting">
            <div>
             <el-image :src="getCover(image.src)" :alt="显示失败" class="gallery-show" @click="imgedit(index)"></el-image>
             <div class="hover-image"></div>
             <span class="gallery-imgdsc">{{ image.dsc }}</span>
            </div>
            
        </template>
        <template v-if="images[index].isediting">
          <div>
            <el-image :src="getCover(image.src)" :alt="显示失败" class="gallery-show" @click="imgchange(index)"></el-image>
            <el-input class="input-imgdsc" placeholder="请输入图片描述" v-model="image.dsc"></el-input>
            <el-button class="black-button" @click="Delete(index)"><el-icon>
                <Delete />
              </el-icon>Delete</el-button>
          </div>
        </template>
    </div>
  </template>

</div>
</div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "Gallery",
  components: {Header},
  data() {
    return {
      isEdit: false,
      isinsert: false,
      curIndex: '',
      images: [
        { src: '001.png', isediting: false, dsc: "Tikal ruins in Guatemala" },
        { src: '002.png', isediting: false, dsc: "Mount Bromo volcano in Java, Indonesia" },
        { src: '003.png', isediting: false, dsc: "Tiger's Nest Monastery in Bhutan" },
        { src: '004.png', isediting: false, dsc: "Wadi Rum desert in Jordan" },
        { src: '005.png', isediting: false, dsc: "005.png" },
        { src: '006.png', isediting: false, dsc: "006.png" },
        { src: '007.png', isediting: false, dsc: "007.png" },
        { src: '008.png', isediting: false, dsc: "008.png" },
        { src: '009.png', isediting: false, dsc: "009.png" },
        { src: '010.png', isediting: false, dsc: "010.png" },
        { src: '011.png', isediting: false, dsc: "011.png" },
        { src: '012.png', isediting: false, dsc: "012.png" },
        { src: '013.png', isediting: false, dsc: "013.png" },
        { src: '014.png', isediting: false, dsc: "014.png" },
        { src: '015.png', isediting: false, dsc: "015.png" },
        { src: '016.png', isediting: false, dsc: "016.png" },
        { src: '017.png', isediting: false, dsc: "017.png" },
        { src: '018.png', isediting: false, dsc: "018.png" },
        { src: '019.png', isediting: false, dsc: "019.png" },
        { src: '020.png', isediting: false, dsc: "020.png" },
      ],
      imagescopy: [
        { src: '001.png', isediting: false, dsc: "Tikal ruins in Guatemala" },
        { src: '002.png', isediting: false, dsc: "Mount Bromo volcano in Java, Indonesia" },
        { src: '003.png', isediting: false, dsc: "Tiger's Nest Monastery in Bhutan" },
        { src: '004.png', isediting: false, dsc: "Wadi Rum desert in Jordan" },
        { src: '005.png', isediting: false, dsc: "005.png" },
        { src: '006.png', isediting: false, dsc: "006.png" },
        { src: '007.png', isediting: false, dsc: "007.png" },
        { src: '008.png', isediting: false, dsc: "008.png" },
        { src: '009.png', isediting: false, dsc: "009.png" },
        { src: '010.png', isediting: false, dsc: "010.png" },
        { src: '011.png', isediting: false, dsc: "011.png" },
        { src: '012.png', isediting: false, dsc: "012.png" },
        { src: '013.png', isediting: false, dsc: "013.png" },
        { src: '014.png', isediting: false, dsc: "014.png" },
        { src: '015.png', isediting: false, dsc: "015.png" },
        { src: '016.png', isediting: false, dsc: "016.png" },
        { src: '017.png', isediting: false, dsc: "017.png" },
        { src: '018.png', isediting: false, dsc: "018.png" },
        { src: '019.png', isediting: false, dsc: "019.png" },
        { src: '020.png', isediting: false, dsc: "020.png" },
      ],
      temporaryimages: [],
      imgList: [

      ],
    };
  },
  created() {
    this.createimglist();

  },
  methods: {
    getCover(cover) {
      return require(`@/assets/gallery/${cover}`);
    },
    createimglist() {
      this.images.forEach((item) => {
        this.imgList.post
      });

      this.images.forEach((item) => {
        this.imgList.push(this.getCover(item.src))
      });
    },
    Edit() {
      this.isEdit = !this.isEdit;
      this.temporaryimages = JSON.parse(JSON.stringify(this.images));
    },
    Save() {
      this.isEdit = !this.isEdit;
      this.images = JSON.parse(JSON.stringify(this.temporaryimages));
    },
    Reset() {
      this.temporaryimages.forEach((item) => {
        item.isediting = false;
      });
      this.temporaryimages = JSON.parse(JSON.stringify(this.imagescopy));
      this.images = JSON.parse(JSON.stringify(this.imagescopy));
      this.createimglist()

    },
    Delete(index) {
      this.temporaryimages.splice(index, 1);
      this.imgList.splice(index, 1);
    },
    imgedit(index) {
      this.images.forEach((item) => {
        item.isediting = false;
      });
      this.images[index].isediting = true;
    },
    imgchange(index) {
      this.curIndex = index,
        this.isinsert = false//记录当前是否是插入完成  false未完成
      this.$refs.fileInput.value = ''
      this.$refs.fileInput.click();
      const handleChangeCallback = () => {
        return (event) => {
          if (this.isinsert) return//如果已经插入完成就不要进行后面插入  就不会反复插入了
          this.isinsert = true//如果是刚进来  未插入  就修改成true  这样再次进入这个方法的时候  就不会继续插入
          const file = event.target.files[0];
          const filename = file.name;
          this.temporaryimages[this.curIndex].src = filename;

          this.imgList[this.curIndex]=this.getCover(filename);
        };
      };
      // 添加带有捕获index参数的回调函数
      this.$refs.fileInput.addEventListener('change', handleChangeCallback());

      // 使用相同的回调函数来移除监听器
      this.$refs.fileInput.removeEventListener('change', handleChangeCallback());
    },
    Post() {
      this.isinsert = false//记录当前是否是插入完成  false未完成
      this.images.forEach((item) => {
        item.isediting = false;
      });
      this.$refs.fileInput.value = ''
      this.$refs.fileInput.click();

      const handleChangeCallback = () => {
        return (event) => {
          if (this.isinsert) return//如果已经插入完成就不要进行后面插入  就不会反复插入了
          this.isinsert = true//如果是刚进来  未插入  就修改成true  这样再次进入这个方法的时候  就不会继续插入
          const file = event.target.files[0];
          const filename = file.name;
          this.temporaryimages.splice(0, 0, {//插入头部
            src: filename,
            isediting: true,
            dsc: ""
          });

          this.imgList.splice(0, 0, this.getCover(filename));
        };
      };
      // 添加带有捕获index参数的回调函数
      this.$refs.fileInput.addEventListener('change', handleChangeCallback());

      // 使用相同的回调函数来移除监听器
      this.$refs.fileInput.removeEventListener('change', handleChangeCallback());

    },
  }
}
</script>

<style src="@/assets/css/gallery.css"></style>
