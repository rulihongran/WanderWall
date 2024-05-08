<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
    <v-distpicker province="" city="" area=""></v-distpicker>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import VDistpicker from 'v-distpicker'

export default {
  name: 'HomeView',
  components: {
    HelloWorld,
    VDistpicker
  }
}
</script>
