<template>
<div style="width:100%;background-color: #efefef">
  <h1 style=" text-align: center; font-size: 40px">{{'欢迎您！'+user.name}}</h1>
  <el-descriptions title="个人中心" :column="2" size="40" border>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-s-custom"></i>
        账号
      </template>
      {{user.no}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-mobile-phone"></i>
        电话
      </template>
      {{user.phone}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-mobile-phone"></i>
        角色
      </template>
      <el-tag type="success" disable-transitions>
        {{user.roleId===0?"超级管理员":(user.roleId===1?"管理员":"用户")}}
      </el-tag>
    </el-descriptions-item>
  </el-descriptions>
  <div style="font-size:30px;text-align: center;padding: 10px">
    <DateUtil></DateUtil>
  </div>

</div>
</template>

<script>
import DateUtil from "./DateUtil"
export default {
  name: "Home",
  components:{DateUtil},
  data(){
    return{
      user:JSON.parse(sessionStorage.getItem(('CurUser'))),
      sex:'',
    }
  },
  computed:{

  },
  methods:{
    init(){
      const storedUser = JSON.parse(sessionStorage.getItem('CurUser'));
      console.log(storedUser)
      if (storedUser) {
        this.user = JSON.parse(storedUser);
        this.sex = this.user.sex
        console.log(this.user.sex)
      } else {
        // 处理 sessionStorage 中没有用户信息的情况
        console.error('当前 sessionStorage 中没有用户信息');
      }
    },
    created() {
      // 在组件创建后立即调用 init 方法初始化用户数据
      this.init();

    }
  }
}
</script>

<style scoped>

</style>