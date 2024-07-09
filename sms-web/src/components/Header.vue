<template>
  <div style="display: flex">
    <div style="margin-top: 5px">
      <i :class="icon" style="font-size: 20px; cursor: pointer" @click="setCollapse"></i>
    </div>
    <div style="flex: 1;text-align: center;font-size: 34px">
      <span>欢迎来到高校贫困生甄别系统</span>
    </div>
    <span>{{user.name}}</span>
    <el-dropdown>
      <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return{
      user:JSON.parse(sessionStorage.getItem(('CurUser'))),
    }
  },
  props:{
    icon:String
  },
  methods:{
    toUser(){
      console.log("to_user")
      this.$router.push("/Home")
    },
    logOut(){
      console.log("logout")
      this.$confirm('是否退出当前账号？','提示',{
        confirmButtonText:'确定',
        type:'warning',
        center:true,//文字居中
        closeOnClickModal:true,//是否可以点击空白处关闭弹窗
      })
      .then(()=>{
        this.$message({
          type:'success',
          message:'退出登录成功'
        })
        this.$router.push("/")
        sessionStorage.clear()
      })
      .catch(()=>{
        this.$message({
          type:'info',
          message:'已取消退出登录'
        })
      })

    },
    setCollapse(){
      this.$emit('setCollapse')
    }
  },
  created(){
    this.$router.push("/Home")
  }
}
</script>

<style scoped>

</style>