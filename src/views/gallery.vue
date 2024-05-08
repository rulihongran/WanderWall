<template>
  <input type="file" ref="fileInput" style="display: none" />
  <div class="main-container">
    
    <div class="group">
      <div class="paragraph">
        <span class="gallery-heading">画廊</span
        ><span class="gallery-subheading">我们旅行的点点滴滴</span>
      </div>
      <div class="container"></div>
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
            <div class="hover-image"></div>
             <span class="gallery-imgdsc"></span>
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
export default {
  name: "gallery",
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
        { src: '021.png', isediting: false, dsc: "021.png" },
        { src: '022.png', isediting: false, dsc: "022.png" },
        { src: '023.png', isediting: false, dsc: "023.png" },
        { src: '024.png', isediting: false, dsc: "024.png" },
        { src: '025.png', isediting: false, dsc: "025.png" },
        { src: '026.png', isediting: false, dsc: "026.png" },
        { src: '027.png', isediting: false, dsc: "027.png" },
        { src: '028.png', isediting: false, dsc: "028.png" },
        { src: '029.png', isediting: false, dsc: "029.png" },
        { src: '030.png', isediting: false, dsc: "030.png" },
        { src: '031.png', isediting: false, dsc: "031.png" },
        { src: '032.png', isediting: false, dsc: "032.png" },
        { src: '033.png', isediting: false, dsc: "033.png" },
        { src: '034.png', isediting: false, dsc: "034.png" },
        { src: '035.png', isediting: false, dsc: "035.png" },
        { src: '036.png', isediting: false, dsc: "036.png" },
        { src: '037.png', isediting: false, dsc: "037.png" },
        { src: '038.png', isediting: false, dsc: "038.png" },
        { src: '039.png', isediting: false, dsc: "039.png" },
        { src: '040.png', isediting: false, dsc: "040.png" },
        { src: '041.png', isediting: false, dsc: "041.png" },
        { src: '042.png', isediting: false, dsc: "042.png" },
        { src: '043.png', isediting: false, dsc: "043.png" },
        { src: '044.png', isediting: false, dsc: "044.png" },
        { src: '045.png', isediting: false, dsc: "045.png" },
        { src: '046.png', isediting: false, dsc: "046.png" },
        { src: '047.png', isediting: false, dsc: "047.png" },
        { src: '048.png', isediting: false, dsc: "048.png" },
        { src: '049.png', isediting: false, dsc: "049.png" },
        { src: '050.png', isediting: false, dsc: "050.png" },
        { src: '051.png', isediting: false, dsc: "051.png" },
        { src: '052.png', isediting: false, dsc: "052.png" },
        { src: '053.png', isediting: false, dsc: "053.png" },
        { src: '054.png', isediting: false, dsc: "054.png" },
        { src: '055.png', isediting: false, dsc: "055.png" },
        { src: '056.png', isediting: false, dsc: "056.png" },
        { src: '057.png', isediting: false, dsc: "057.png" },
        { src: '058.png', isediting: false, dsc: "058.png" },
        { src: '059.png', isediting: false, dsc: "059.png" },
        { src: '060.png', isediting: false, dsc: "060.png" },
        { src: '061.png', isediting: false, dsc: "061.png" },
        { src: '062.png', isediting: false, dsc: "062.png" },
        { src: '063.png', isediting: false, dsc: "063.png" },
        { src: '064.png', isediting: false, dsc: "064.png" },
        { src: '065.png', isediting: false, dsc: "065.png" },
        { src: '066.png', isediting: false, dsc: "066.png" },
        { src: '067.png', isediting: false, dsc: "067.png" },
        { src: '068.png', isediting: false, dsc: "068.png" },
        { src: '069.png', isediting: false, dsc: "069.png" },
        { src: '070.png', isediting: false, dsc: "070.png" },
        { src: '071.png', isediting: false, dsc: "071.png" },
        { src: '072.png', isediting: false, dsc: "072.png" },
        { src: '073.png', isediting: false, dsc: "073.png" },
        { src: '074.png', isediting: false, dsc: "074.png" },
        { src: '075.png', isediting: false, dsc: "075.png" },
        { src: '076.png', isediting: false, dsc: "076.png" },
        { src: '077.png', isediting: false, dsc: "077.png" },
        { src: '078.png', isediting: false, dsc: "078.png" },
        { src: '079.png', isediting: false, dsc: "079.png" },
        { src: '080.png', isediting: false, dsc: "080.png" },
        { src: '081.png', isediting: false, dsc: "081.png" },
        { src: '082.png', isediting: false, dsc: "082.png" },
        { src: '083.png', isediting: false, dsc: "083.png" },
        { src: '084.png', isediting: false, dsc: "084.png" },
        { src: '085.png', isediting: false, dsc: "085.png" },
        { src: '086.png', isediting: false, dsc: "086.png" },
        { src: '087.png', isediting: false, dsc: "087.png" },
        { src: '088.png', isediting: false, dsc: "088.png" },
        { src: '089.png', isediting: false, dsc: "089.png" },
        { src: '090.png', isediting: false, dsc: "090.png" },
        { src: '091.png', isediting: false, dsc: "091.png" },
        { src: '092.png', isediting: false, dsc: "092.png" },
        { src: '093.png', isediting: false, dsc: "093.png" },
        { src: '094.png', isediting: false, dsc: "094.png" },
        { src: '095.png', isediting: false, dsc: "095.png" },
        { src: '096.png', isediting: false, dsc: "096.png" },
        { src: '097.png', isediting: false, dsc: "097.png" },
        { src: '098.png', isediting: false, dsc: "098.png" },
        { src: '099.png', isediting: false, dsc: "099.png" },
        { src: '100.png', isediting: false, dsc: "100.png" },
        { src: '101.png', isediting: false, dsc: "101.png" },
        { src: '102.png', isediting: false, dsc: "102.png" },
        { src: '103.png', isediting: false, dsc: "103.png" },
        { src: '104.png', isediting: false, dsc: "104.png" },
        { src: '105.png', isediting: false, dsc: "105.png" },
        { src: '106.png', isediting: false, dsc: "106.png" },
        { src: '107.png', isediting: false, dsc: "107.png" },
        { src: '108.png', isediting: false, dsc: "108.png" },
        { src: '109.png', isediting: false, dsc: "109.png" },
        { src: '110.png', isediting: false, dsc: "110.png" },
        { src: '111.png', isediting: false, dsc: "111.png" },
        { src: '112.png', isediting: false, dsc: "112.png" },
        { src: '113.png', isediting: false, dsc: "113.png" },
        { src: '114.png', isediting: false, dsc: "114.png" },
        { src: '115.png', isediting: false, dsc: "115.png" },
        { src: '116.png', isediting: false, dsc: "116.png" },
        { src: '117.png', isediting: false, dsc: "117.png" },
        { src: '118.png', isediting: false, dsc: "118.png" },
        { src: '119.png', isediting: false, dsc: "119.png" },
        { src: '120.png', isediting: false, dsc: "120.png" },
        { src: '121.png', isediting: false, dsc: "121.png" },
        { src: '122.png', isediting: false, dsc: "122.png" },
        { src: '123.png', isediting: false, dsc: "123.png" },
        { src: '124.png', isediting: false, dsc: "124.png" },
        { src: '125.png', isediting: false, dsc: "125.png" },
        { src: '126.png', isediting: false, dsc: "126.png" },
        { src: '127.png', isediting: false, dsc: "127.png" },
        { src: '128.png', isediting: false, dsc: "128.png" },
        { src: '129.png', isediting: false, dsc: "129.png" },
        { src: '130.png', isediting: false, dsc: "130.png" },
        { src: '131.png', isediting: false, dsc: "131.png" },
        { src: '132.png', isediting: false, dsc: "132.png" },
        { src: '133.png', isediting: false, dsc: "133.png" }
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
        { src: '021.png', isediting: false, dsc: "021.png" },
        { src: '022.png', isediting: false, dsc: "022.png" },
        { src: '023.png', isediting: false, dsc: "023.png" },
        { src: '024.png', isediting: false, dsc: "024.png" },
        { src: '025.png', isediting: false, dsc: "025.png" },
        { src: '026.png', isediting: false, dsc: "026.png" },
        { src: '027.png', isediting: false, dsc: "027.png" },
        { src: '028.png', isediting: false, dsc: "028.png" },
        { src: '029.png', isediting: false, dsc: "029.png" },
        { src: '030.png', isediting: false, dsc: "030.png" },
        { src: '031.png', isediting: false, dsc: "031.png" },
        { src: '032.png', isediting: false, dsc: "032.png" },
        { src: '033.png', isediting: false, dsc: "033.png" },
        { src: '034.png', isediting: false, dsc: "034.png" },
        { src: '035.png', isediting: false, dsc: "035.png" },
        { src: '036.png', isediting: false, dsc: "036.png" },
        { src: '037.png', isediting: false, dsc: "037.png" },
        { src: '038.png', isediting: false, dsc: "038.png" },
        { src: '039.png', isediting: false, dsc: "039.png" },
        { src: '040.png', isediting: false, dsc: "040.png" },
        { src: '041.png', isediting: false, dsc: "041.png" },
        { src: '042.png', isediting: false, dsc: "042.png" },
        { src: '043.png', isediting: false, dsc: "043.png" },
        { src: '044.png', isediting: false, dsc: "044.png" },
        { src: '045.png', isediting: false, dsc: "045.png" },
        { src: '046.png', isediting: false, dsc: "046.png" },
        { src: '047.png', isediting: false, dsc: "047.png" },
        { src: '048.png', isediting: false, dsc: "048.png" },
        { src: '049.png', isediting: false, dsc: "049.png" },
        { src: '050.png', isediting: false, dsc: "050.png" },
        { src: '051.png', isediting: false, dsc: "051.png" },
        { src: '052.png', isediting: false, dsc: "052.png" },
        { src: '053.png', isediting: false, dsc: "053.png" },
        { src: '054.png', isediting: false, dsc: "054.png" },
        { src: '055.png', isediting: false, dsc: "055.png" },
        { src: '056.png', isediting: false, dsc: "056.png" },
        { src: '057.png', isediting: false, dsc: "057.png" },
        { src: '058.png', isediting: false, dsc: "058.png" },
        { src: '059.png', isediting: false, dsc: "059.png" },
        { src: '060.png', isediting: false, dsc: "060.png" },
        { src: '061.png', isediting: false, dsc: "061.png" },
        { src: '062.png', isediting: false, dsc: "062.png" },
        { src: '063.png', isediting: false, dsc: "063.png" },
        { src: '064.png', isediting: false, dsc: "064.png" },
        { src: '065.png', isediting: false, dsc: "065.png" },
        { src: '066.png', isediting: false, dsc: "066.png" },
        { src: '067.png', isediting: false, dsc: "067.png" },
        { src: '068.png', isediting: false, dsc: "068.png" },
        { src: '069.png', isediting: false, dsc: "069.png" },
        { src: '070.png', isediting: false, dsc: "070.png" },
        { src: '071.png', isediting: false, dsc: "071.png" },
        { src: '072.png', isediting: false, dsc: "072.png" },
        { src: '073.png', isediting: false, dsc: "073.png" },
        { src: '074.png', isediting: false, dsc: "074.png" },
        { src: '075.png', isediting: false, dsc: "075.png" },
        { src: '076.png', isediting: false, dsc: "076.png" },
        { src: '077.png', isediting: false, dsc: "077.png" },
        { src: '078.png', isediting: false, dsc: "078.png" },
        { src: '079.png', isediting: false, dsc: "079.png" },
        { src: '080.png', isediting: false, dsc: "080.png" },
        { src: '081.png', isediting: false, dsc: "081.png" },
        { src: '082.png', isediting: false, dsc: "082.png" },
        { src: '083.png', isediting: false, dsc: "083.png" },
        { src: '084.png', isediting: false, dsc: "084.png" },
        { src: '085.png', isediting: false, dsc: "085.png" },
        { src: '086.png', isediting: false, dsc: "086.png" },
        { src: '087.png', isediting: false, dsc: "087.png" },
        { src: '088.png', isediting: false, dsc: "088.png" },
        { src: '089.png', isediting: false, dsc: "089.png" },
        { src: '090.png', isediting: false, dsc: "090.png" },
        { src: '091.png', isediting: false, dsc: "091.png" },
        { src: '092.png', isediting: false, dsc: "092.png" },
        { src: '093.png', isediting: false, dsc: "093.png" },
        { src: '094.png', isediting: false, dsc: "094.png" },
        { src: '095.png', isediting: false, dsc: "095.png" },
        { src: '096.png', isediting: false, dsc: "096.png" },
        { src: '097.png', isediting: false, dsc: "097.png" },
        { src: '098.png', isediting: false, dsc: "098.png" },
        { src: '099.png', isediting: false, dsc: "099.png" },
        { src: '100.png', isediting: false, dsc: "100.png" },
        { src: '101.png', isediting: false, dsc: "101.png" },
        { src: '102.png', isediting: false, dsc: "102.png" },
        { src: '103.png', isediting: false, dsc: "103.png" },
        { src: '104.png', isediting: false, dsc: "104.png" },
        { src: '105.png', isediting: false, dsc: "105.png" },
        { src: '106.png', isediting: false, dsc: "106.png" },
        { src: '107.png', isediting: false, dsc: "107.png" },
        { src: '108.png', isediting: false, dsc: "108.png" },
        { src: '109.png', isediting: false, dsc: "109.png" },
        { src: '110.png', isediting: false, dsc: "110.png" },
        { src: '111.png', isediting: false, dsc: "111.png" },
        { src: '112.png', isediting: false, dsc: "112.png" },
        { src: '113.png', isediting: false, dsc: "113.png" },
        { src: '114.png', isediting: false, dsc: "114.png" },
        { src: '115.png', isediting: false, dsc: "115.png" },
        { src: '116.png', isediting: false, dsc: "116.png" },
        { src: '117.png', isediting: false, dsc: "117.png" },
        { src: '118.png', isediting: false, dsc: "118.png" },
        { src: '119.png', isediting: false, dsc: "119.png" },
        { src: '120.png', isediting: false, dsc: "120.png" },
        { src: '121.png', isediting: false, dsc: "121.png" },
        { src: '122.png', isediting: false, dsc: "122.png" },
        { src: '123.png', isediting: false, dsc: "123.png" },
        { src: '124.png', isediting: false, dsc: "124.png" },
        { src: '125.png', isediting: false, dsc: "125.png" },
        { src: '126.png', isediting: false, dsc: "126.png" },
        { src: '127.png', isediting: false, dsc: "127.png" },
        { src: '128.png', isediting: false, dsc: "128.png" },
        { src: '129.png', isediting: false, dsc: "129.png" },
        { src: '130.png', isediting: false, dsc: "130.png" },
        { src: '131.png', isediting: false, dsc: "131.png" },
        { src: '132.png', isediting: false, dsc: "132.png" },
        { src: '133.png', isediting: false, dsc: "133.png" }
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
