<template>
  <Header/>
  <div class="relation-background">
    <div id="relation-container"  style="margin-top: 90px">
      <div class="card-title" style="color: #d2cfcf; font-size: 20px;margin: 10px">Travel Relation Graph</div>
    </div>
  </div>

</template>

<script>
import Header from "@/components/Header";
import "../assets/css/relation-graph.css";
import {drawRelationGraph} from "@/utils/relation-graph/relation-graph";

export default {
  name: "RelationGraph",
  components: {Header},
  mounted() {
    drawRelationGraph();
  }
}
</script>

<style scoped>
.relation-background{
  display: flex;
  justify-content: center;
  align-items: center;
  background: #599592;
  height: 100vh;
}
#relation-container{
  background: rgba(255,255,255,0.12);
  /*background: #47807e;*/
  width: 90%;
  height: 85%;
  border-radius: 30px;
}
</style>