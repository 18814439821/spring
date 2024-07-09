<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">用户登录</h1>
        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="width: 200px" type="text" v-model="loginForm.no"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 200px" type="password" v-model="loginForm.password"
                      auto-complete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirm" :disabled="confirm_disabled">登 录</el-button>
            <el-button type="primary" @click="register" :disabled="confirm_disabled" style="margin-left: 45px">注 册</el-button>

          </el-form-item>
          <el-link @click="findPwd" target="_blank" style="margin-left:80%">找回密码</el-link>
        </el-form>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      confirm_disabled:false,
      loginForm:{
        no:'',
        password:'',
      },
      rules:{
        no:[
          {required:true,message:'请输入账号',trigger:'blur'}
        ],
        password:[
          {required:true,message:'请输入密码',trigger:'blur'}
        ],
      }
    }
  },
  methods:{
    findPwd(){
      this.$router.replace('/FindPwd');
    },
    register(){
      this.$router.replace('/Register');
    },
    confirm(){
      this.$refs.loginForm.validate((valid)=>{  //validate为el-form的验证机制，验证通过后为true
        if (valid){//valid成功为true，失败为false
          //去后台验证用户名和密码
          this.$axios.post(this.$httpUrl+'/user/login',this.loginForm).then(res=>res.data).then(res=>{
            console.log("res"+res)
            if(res.code===200){
              //存储
              sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
              console.log(res.data.menu)
              //使用store.commit提交，第一个参数的方法名，第二个参数是值
              //把获取到的menu设置为动态路由
              this.$store.commit("setMenu",res.data.menu)
              //跳转到主页
              this.$router.replace('/Index');
            }else{
              this.confirm_disabled=false;
              alert('校验失败，请检查用户名，密码是否错误或该账号已被停用！');
              return false;
            }
          })
        }else {
          this.confirm_disabled=false;
        }
      });
    }
  }
}
</script>

<style scoped>
.loginBody{
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #b3c0d1;
}
.loginDiv{
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 450px;
  height: 330px;
  background: #fff;
  border-radius: 5%;
}
.login-title{
  margin: 20px 0;
  text-align: center;
}
.login-content{
  width: 400px;
  height: 250px;
  position: absolute;
  top: 25px;
  left: 25px;
}
</style>