import { createStore } from 'vuex';

export default createStore({
  state() {
    return {
      isLoggedIn: false,
      user_id:null,
      user_name:null,
      policyJsonString : "{\"Version\" : \"2012-10-17\",\"Statement\":[{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + "UserInfo" + "/*\"]}]}"
    };
  },
  mutations: {
    setLoginState(state, isLoggedIn) {
      state.isLoggedIn = isLoggedIn;
    },
    setCurUser(state, id){
      state.user_id = id;
    },
    setUserName(state, name){
      state.user_name = name;
    }
  },
  actions: {
  },
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    userID:state => state.user_id,
    userName:state=>state.user_name,
    policyJsonString:state=>state.policyJsonString
  }
});