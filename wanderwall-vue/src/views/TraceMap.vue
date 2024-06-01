<template>
  <Header/>

  <div class="map-view">
    <div class="map-module-container" >
      <!--返回图标-->
      <svg  @click="backLink"  xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="white" class="bi bi-box-arrow-left"  viewBox="0 0 16 16" id="back-to-china-icon">
        <path fill-rule="evenodd" d="M6 12.5a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-9a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v2a.5.5 0 0 1-1 0v-2A1.5 1.5 0 0 1 6.5 2h8A1.5 1.5 0 0 1 16 3.5v9a1.5 1.5 0 0 1-1.5 1.5h-8A1.5 1.5 0 0 1 5 12.5v-2a.5.5 0 0 1 1 0v2z"/>
        <path fill-rule="evenodd" d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3z"/>
      </svg>
      <!--地图-->
      <div id="trace-map"></div>
      <!--操作提示-->
      <el-dialog class="no-code-dialog" v-model="dialogVisible.value" title="What do you wanna do?" align-center>
        <div class="nocode-body">
          <div class="tips">Please choose the next operation</div>
          <div class="wrap" style="display:flex;flex-direction:column;justify-content: center">
            <div class="dialog-btn" @click="handlePost">Post blog for this city</div>
            <div class="dialog-btn-2" @click="handleVisited" >Add Travel Count</div>
          </div>
        </div>
      </el-dialog>

      <!--仪表盘-->
      <div  class="gauge-container">
        <div id="city-gauge"></div>
        <div id="people-gauge"></div>
      </div>

      <!--筛选表格-->
      <div class="search-filters-container">
        <div class="search-filters job-board-filters-form js-filters" style="background-color: rgba(255,255,255,0.2)">
          <form class="job-filters js-job-search-form" id="options" action="/filter-map-data" accept-charset="UTF-8" method="get">
            <!--标签筛选-->
            <label class="map-filter-header" for="skills">Mark</label>
            <div class="checkbox-radio-group">
              <label class="checkbox-radio-label" for="mark1">
                <input class="js-specialty-checkbox" type="checkbox" value="1" name="mark1" id="specialty_ids_3" checked>
                Location Point
              </label>
              <label class="checkbox-radio-label" for="mark2">
                <input class="js-specialty-checkbox" type="checkbox" value="1" name="mark2" id="specialty_ids_4" checked>
                Route Arrow
              </label>
              <label class="checkbox-radio-label" for="mark3">
                <input class="js-specialty-checkbox" type="checkbox" value="1" name="mark3" id="specialty_ids_5">
                Place Name
              </label>
            </div>

            <div class="divider"></div>

            <!--时间筛选-->
            <fieldset class="find-shots-timeframe">
              <div style="display: flex;align-items: center;margin-bottom: 10px">
              <span >Time</span>
              </div>
              <span
                  class="btn-dropdown"
                  :class="{ 'btn-dropdown-neue': !isTimeSelectActive, 'open': isTimeSelectActive }"
                  @click="toggleTimeSelectActive"
              >
            <a class="form-btn outlined btn-dropdown-link" href="#" data-name="Timeframe" data-track-filter="" data-dropdown-state="open">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" role="img" class="icon btn-dropdown-caret">
<path d="M21.5265 8.77171C22.1578 8.13764 22.1578 7.10962 21.5265 6.47555C20.8951 5.84148 19.8714 5.84148 19.24 6.47555L11.9999 13.7465L4.75996 6.47573C4.12858 5.84166 3.10492 5.84166 2.47354 6.47573C1.84215 7.10979 1.84215 8.13782 2.47354 8.77188L10.8332 17.1671C10.8408 17.1751 10.8486 17.183 10.8565 17.1909C11.0636 17.399 11.313 17.5388 11.577 17.6103C11.5834 17.6121 11.5899 17.6138 11.5964 17.6154C12.132 17.7536 12.7242 17.6122 13.1435 17.1911C13.1539 17.1807 13.1641 17.1702 13.1742 17.1596L21.5265 8.77171Z"></path>
</svg>
              <span id="selected-time-option" data-prompt="Now" data-fade-default="true" class="default-option">
                {{timeOptions[timeSelected].label}}
              </span>
              <div id="app" class="btn-dropdown-options sets-querystring" style="width: 100%">
                <ul>
                <li v-for="(option, index) in timeOptions" :key="index" :class="{ 'default-filter-option': true, 'active': index === timeSelected }" @click="updateTimeOption(index)">
                <a>{{ option.label }}</a>
                </li>
                </ul>
              </div>
            </a>

          </span>
            </fieldset>

            <div class="divider"></div>
<!--            Location指区域 比如长三角 待斟酌-->
            <label class="map-filter-header" for="location">Location</label>
            <input type="text" name="location" id="location" placeholder="Enter Location…" class="js-location input-with-outline-only" style="margin-bottom: 15px">
            <div class="checkbox-radio-group">
              <label class="checkbox-radio-label" for="specialty_ids_3">
                <input type="checkbox" name="fav-place" id="anywhere" value="true">
                <span>Only show favorite place</span>
              </label>
            </div>

            <div class="submit submit-container">
              <button class="filter-button" type="submit" >Filter</button>
              <button class="filter-clear-button"  type="reset">Clear</button>
            </div>
          </form></div>
      </div>
    </div>

  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import "@/assets/css/tracemap.css";
import { ref } from 'vue';
import {drawGauge} from "@/utils/polargauge";
import {drawChinaMap,updateProvinceMap} from "@/utils/tracemap";

export default {
  components: {Header},
  data() {
    return {
      //next step提示框是否显示
      dialogVisible: {value:false},//对象 实现类似引用效果 showProvince里修改值

      //省份地图
      enterProvince:{value:false},//对象 实现类似引用效果 可在别的js文件修改该值

      isTimeSelectActive: false,
      timeSelected: 0, // 默认选中第一个选项
      timeOptions: [
        { label: 'The Past Month'},
        { label: 'This Past Year'},
        { label: 'All Time'}
      ],

      mark_options:{locPoint:true,routeArrow:true,provinceName:false}//点 箭头 仪表盘是否显示
    };
  },
  mounted() {
    // Axios请求都在各个函数里面
    drawChinaMap(this.dialogVisible,this.mark_options,this.enterProvince);
    drawGauge();


    //检测筛选表格提交行为，手动提交给后端
    const self = this;
    document.getElementById('options').addEventListener('submit', function(event) {
      event.preventDefault(); // 阻止默认提交行为

      let formData = new FormData(this);// time location show-fav-place

      // 将 FormData 转换为普通对象，便于后端传送
      let formObject = {};
      formData.forEach((value, key) => {
        if (!formObject[key]) {
          formObject[key] = value;
        } else {
          if (!Array.isArray(formObject[key])) {
            formObject[key] = [formObject[key]];
          }
          formObject[key].push(value);
        }
      });
      formObject['time'] = self.timeOptions[self.timeSelected].label;
      if (!formObject['fav-place']){
        formObject['fav-place'] = false; //人为添加false，不然没选择fav-place时该选项不会在formData里
      }
      console.log(formObject);

      // 点,箭头,仪表盘是否显示，不传给后端，直接在前端处理
      self.mark_options.locPoint = formObject['mark1'];
      self.mark_options.routeArrow = formObject['mark2'];
      self.mark_options.provinceName = formObject['mark3']
      if (!self.enterProvince.value){
        drawChinaMap(self.dialogVisible,self.mark_options);
      }else{
        updateProvinceMap(self.mark_options);
      }

      // 发送请求到后端
      // axios.get(this.action, {
      //   params: formObject
      // })
      //     .then(response => {
      //       console.log(response.data); // 处理返回的数据
      //       // 在这里更新页面上的内容
      //     })
      //     .catch(error => {
      //       console.error('Error:', error);
      //     });
    });
  },
  methods: {
    toggleTimeSelectActive() {
      this.isTimeSelectActive = !this.isTimeSelectActive;
    },
    updateTimeOption(index) {
      this.timeSelected = index;
    },
    backLink(){
      this.enterProvince.value = false;
      drawChinaMap(this.dialogVisible,this.mark_options);
    },

    handlePost(){
      this.dialogVisible.value = false;
      this.$router.push('/createblog');
      console.log('你选择了发布帖子');
    },
    handleVisited(){
      this.dialogVisible.value = false;
      console.log('你选择了标记去过');
    },

  },
};
</script>

<style scoped>
/* 模式选择 */
.mode-container {
  --header-height:80px;
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  height: 20px;
}

/* 按钮样式 */
.mode-button {
  padding: 10px 20px;
  margin: 10px;
  background-color: #ebf3ef;
  border-radius: 20px;
  font-size: 16px;
  cursor: pointer;

}

.map-view .map-module-container{
  display: flex;
}

.map-view {
  display: -ms-flexbox;
  padding-top: 100px;
  width: 100%;
  height: 100%;
  background: #599592;
}
.map-module-container{
  max-width: 1820px;
}
.bi-box-arrow-left {
  margin-right: -80px;
  margin-left: 40px;
  margin-top: 30px;
  z-index: 10;
}
.map-view #trace-map {
  max-width: 1100px;
  width:95%;
  background: #4d807e;
  height: 660px;
  margin-top:20px;
  margin-bottom: 20px;
  margin-left: 2%;
  border: 10px solid #4d807e; /* 边框宽度和颜色 */
  border-radius: 30px;   /* 圆角半径 */
  /* 可选，为内容添加内边距 */
}
.map-view .gauge-container{
  margin:20px 10px 20px 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.map-view #city-gauge,#people-gauge {
  width:260px;
  background: #4d807e;
  height: 50%;
  margin-top:0;
  margin-left: 3%;
  margin-right: 3%;
  border: 10px solid #4d807e; /* 边框宽度和颜色 */
  border-radius: 30px;   /* 圆角半径 */
  /* 可选，为内容添加内边距 */
}
.map-view #city-gauge{
  margin-bottom: 20px;
}
.search-filters-container {
  display:-ms-flexbox;
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  -ms-flex-align: center;
  align-items: center;
  margin: 20px;
  /*background-color: rgba(255,255,255,0.2);*/
}

.search-filters {
  height: 100%;
  display: none;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  /*margin-bottom: 24px;*/
  padding: 32px;
  border: 1px solid #e7e7e9;
  border-radius: 12px
}

@media (min-width: 768px) {
  .search-filters {
    display:block;
    width: 244px;
    margin-bottom: 0
  }
}

@media (min-width: 920px) {
  .search-filters {
    width:336px
  }
}

.search-filters.open {
  display: block
}

.search-filters .divider {
  margin: 24px 0;
  border-top: 1px solid #e7e7e9
}

.search-filters .submit-container {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column;
  -ms-flex-align: center;
  align-items: center;
  margin-top: 32px
}

.checkbox-radio-group label {
  margin-bottom: 0
}

.checkbox-radio-group>*:not(:last-child) {
  margin-bottom: 16px
}

.checkbox-radio-group-horizontal {
  display: -ms-flexbox;
  display: flex;
  -ms-flex-direction: column;
  flex-direction: column
}

@media (min-width: 768px) {
  .checkbox-radio-group-horizontal {
    -ms-flex-direction:row;
    flex-direction: row;
    -ms-flex-align: center;
    align-items: center
  }
}

.checkbox-radio-group-horizontal label {
  margin-bottom: 0
}

.checkbox-radio-group-horizontal>*:not(:last-child) {
  margin-bottom: 16px
}

@media (min-width: 768px) {
  .checkbox-radio-group-horizontal>*:not(:last-child) {
    margin-right:24px;
    margin-bottom: 0
  }
}

@media (min-width: 768px) {
  .checkbox-radio-group-2-columns {
    -webkit-column-gap:0;
    -moz-column-gap: 0;
    column-gap: 0;
    -webkit-column-count: 2;
    -moz-column-count: 2;
    column-count: 2
  }

  .checkbox-radio-group-2-columns .checkbox-radio-label {
    margin-bottom: 6px;
    padding: 4px
  }
}

.checkbox-radio-label {
  font-family: "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 14px;
  font-weight: 400;
  line-height: 20px;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  -ms-flex-align: center;
  align-items: center;
  color: #3d3d4e
}

.inline-radios .checkbox-radio-label {
  margin-right: 16px;
  margin-bottom: 0
}

.inline-radios .checkbox-radio-label:last-child {
  margin-right: 0
}

.checkbox-radio-label.is-disabled {
  color: #9e9ea7
}

.checkbox-radio-label label {
  margin-bottom: 0
}

.checkbox-radio-label:hover input {
  -webkit-box-shadow: 0 0 0 4px rgba(234,76,137,0.1);
  box-shadow: 0 0 0 4px rgba(234,76,137,0.1);
  cursor: pointer
}

.v-checkbox {
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none
}

.v-checkbox.is-disabled {
  opacity: 0.6
}

.v-checkbox.is-disabled .checkbox-radio-label:hover {
  -webkit-box-shadow: none;
  box-shadow: none;
  cursor: not-allowed
}

.v-checkbox.is-disabled input[type='checkbox']:hover {
  -webkit-box-shadow: none;
  box-shadow: none;
  cursor: not-allowed
}

input[type='text'],input[type='number'],input[type='search'],input[type='password'],input[type='email'],input[type='url'],input[type='tel'],.braintree-input,.selectize-input,textarea,.ql-container .ql-editor,select,.input-with-prefix {
  font-family: "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 16px;
  font-weight: 400;
  line-height: 28px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 100%;
  height: 56px;
  padding: 18px 20px;
  -webkit-transition: background-color 200ms ease, outline 200ms ease, color 200ms ease, -webkit-box-shadow 200ms ease;
  transition: background-color 200ms ease, outline 200ms ease, color 200ms ease, -webkit-box-shadow 200ms ease;
  transition: background-color 200ms ease, outline 200ms ease, color 200ms ease, box-shadow 200ms ease;
  transition: background-color 200ms ease, outline 200ms ease, color 200ms ease, box-shadow 200ms ease, -webkit-box-shadow 200ms ease;
  border-radius: 12px;
  outline: none;
  background-color: #fff;
  color: #0d0c22;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  -webkit-font-feature-settings: 'ss01';
  font-feature-settings: 'ss01'
}

@media (min-width: 768px) {
  input[type='text'],input[type='number'],input[type='search'],input[type='password'],input[type='email'],input[type='url'],input[type='tel'],.braintree-input,.selectize-input,textarea,.ql-container .ql-editor,select,.input-with-prefix {
    font-size:14px
  }
}

input:hover[type='text'],input:hover[type='number'],input:hover[type='search'],input:hover[type='password'],input:hover[type='email'],input:hover[type='url'],input:hover[type='tel'],.braintree-input:hover,.selectize-input:hover,textarea:hover,.ql-container .ql-editor:hover,select:hover,.input-with-prefix:hover {
  border-color: rgba(0,0,0,0.1);
  background-color: #fff;
  -webkit-box-shadow: 0 0 0 4px rgba(234,100,217,0.1);
  box-shadow: 0 0 0 4px rgba(234,100,217,0.1)
}

input[type='checkbox'],input[type='radio'] {
  display: -ms-flexbox;
  display: flex;
  position: relative;
  -ms-flex-negative: 0;
  flex-shrink: 0;
  -ms-flex-align: center;
  align-items: center;
  -ms-flex-pack: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  margin: 0 12px 0 0;
  padding: 0;
  -webkit-transition: background 200ms ease, border 200ms ease, -webkit-box-shadow 200ms ease;
  transition: background 200ms ease, border 200ms ease, -webkit-box-shadow 200ms ease;
  transition: box-shadow 200ms ease, background 200ms ease, border 200ms ease;
  transition: box-shadow 200ms ease, background 200ms ease, border 200ms ease, -webkit-box-shadow 200ms ease;
  border: 1px solid #dbdbde;
  border-radius: 6px;
  outline: none;
  background: #fff;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none
}

input:checked[type='checkbox'],input:checked[type='radio'] {
  border-color: #6ca88f;
  background-color: #87d9b7
}

input[type='checkbox']:checked:after {
  content: '';
  position: absolute;
  width: 12px;
  height: 12px;
  background-image: url(../assets/icons/check-lg.svg);
  background-size: 12px 12px;
  background-repeat: no-repeat
}

input[type='checkbox']:disabled {
  background-color: #f3f3f4
}

.map-filter-header{
  text-align: left;
  font-family: "Mona Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 16px;
  font-weight: 500;
  line-height: 22px;
  display: block;
  margin-bottom: 10px;
  color: #0d0c22;
}

.filter-button{
  margin: 10px 5px;
  padding: 10px 80px;
  border-radius: 20px;
}
.filter-clear-button{
  background-color: transparent;
  margin: 5px;
  padding: 10px 80px;
  border-radius: 20px;
}

.no-code-dialog .nocode-body .tips {
  color: #6c6c6c;
  font-family: 'PingFang SC', sans-serif;
  font-size: 14px;
  font-weight: 600;
  padding: 10px 0 15px;
}

.no-code-dialog .nocode-body .wrap .row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.no-code-dialog .nocode-body .wrap .row i {
  display: inline-block;
  width: 7px;
  height: 7px;
  transform: rotate(45deg);
  background-color: #000;
  align-items: center;
}

.no-code-dialog .nocode-body .wrap .row span {
  color: #000;
  font-family: 'PingFang SC', sans-serif;
  font-size: 14px;
  margin-left: 10px;
  align-items: center;
}

.no-code-dialog .dialog-btn, .dialog-btn-2{
  align-self: center;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 372px;
  height: 40px;
  border-radius: 4px;
  background: #000;
  color: #fff;
  font-family: 'PingFang SC', sans-serif;
  font-size: 14px;
  font-weight: 500;
  margin: 10px;
}
.no-code-dialog .dialog-btn:hover{
  background: #3d3d3d;
  color: #f0f0f0;
  transition: ease 0.2s;
}
.no-code-dialog .dialog-btn-2{
  background: #ffffff;
  color: black;
  border: solid 1px black;
}
.no-code-dialog .dialog-btn-2:hover{
  background: #f6f6f6;
  color: #313131;
  border: solid 1px black;
  transition: ease 0.2s;
}
/* .el-overlay */
.el-overlay .el-overlay-dialog .el-dialog.no-code-dialog {
  --el-dialog-width: 420px;
  --el-dialog-bg-color: #fff;
}

.el-overlay .el-overlay-dialog .el-dialog.no-code-dialog .el-dialog__header .el-dialog__title {
  color: #000;
}
</style>