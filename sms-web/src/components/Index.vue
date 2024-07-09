<template>
  <el-container  style=" border: 1px solid #eee">
    <el-aside width="auto"  style="margin-left: -1px;margin-top: -1px;height: 100vh">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right; font-size: 12px;height: 100%;border-bottom: #535C64 1px solid;">
        <Header @setCollapse="setCollapse" :icon="icon"></Header>
      </el-header>

      <el-main style="height: 100%">
<!--        <Main></Main>-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: "Index",
  components: {Header, Aside},
  data(){
    return{
      isCollapse:false,
      icon:'el-icon-s-fold'
    }
  },
  methods:{
    setCollapse() {
      this.isCollapse = !this.isCollapse
      this.isCollapse===true?this.icon='el-icon-s-unfold':this.icon='el-icon-s-fold'
    }
  }

}
</script>

<style scoped>
.el-header {
  /*background-color: #B3C0D1;*/
  color: #333;
  line-height: 60px;
}
.el-main{
  padding: 5px;
}
.el-aside {
  color: #333;
}
</style>