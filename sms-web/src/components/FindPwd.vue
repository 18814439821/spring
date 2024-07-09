<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">密码找回</h1>
        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="width: 200px" type="text" v-model="loginForm.no"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input style="width: 200px" type="text" v-model="loginForm.name"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="phone">
            <el-input style="width: 200px" type="text" v-model="loginForm.phone"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="findPsw" :disabled="confirm_disabled">确 定</el-button>
            <el-button type="primary" @click="login" :disabled="confirm_disabled">返 回 登 录</el-button>
          </el-form-item>

        </el-form>
      </div>
      <!--    编辑框-->
      <el-dialog
          title="提示"
          :visible.sync="centerDialogVisible"
          width="40%"
          center
          :before-close="handleClose">
        <el-form ref="form" :model="loginForm" :rules="rules" label-width="80px">
          <el-form-item label="新密码" prop="password">
            <el-input v-model="loginForm.password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="重复输入" prop="password1">
            <el-input type="password"  v-model="from.password1"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>

      </el-dialog>
    </div>

  </div>
</template>

<script>
export default {
  name: "FindPwd",
  data(){

    let unValid=(rule,value,callback)=>{
          if(this.loginForm.id){
            return callback;
          }
          if(value!==this.loginForm.password){
            callback(new Error('两次输入密码不一致！'));
          }
        };

    return{
      confirm_disabled:false,
      centerDialogVisible: false,
      loginForm:{
        id:'',
        no:'',
        password:'',
        name:'',
        phone:'',
        isValid:'Y',
      },
      from:{
        password1:''
      },
      rules:{
        password:[
          {required:true,message:'请输入密码',trigger:'blur'},
          {min:3,max:8,message: '长度在 3 到 8 个字符',trigger: 'blur'}
        ],
        password1:[
          {required:true,message:'请再次输入密码',trigger:'blur'},
          {validator:unValid,trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {
            this.centerDialogVisible=false;
            this.resetForm();
            done();
          })
          .catch(()=> {});
    },
    save(){
      if(this.loginForm.password!==this.from.password1){
        this.$message({
          showClose: true,
          message: '两次密码不一致！',
          type: 'danger'
        })
      }else {
        this.$axios.post(this.$httpUrl+"/user/changePwd",this.loginForm).then(res=>res.data).then(res=>{
          if (res.code===200){

            this.$message({
              showClose: true,
              message: '修改成功！',
              type: 'success'
            });
          }
        })
        this.$router.replace('/');
      }
    },
    findPsw(){
      this.$axios.post(this.$httpUrl+"/user/findPwd",this.loginForm).then(res=>res.data).then(res=>{
        if(res.code===200){
          console.log(res.data[0])
          this.loginForm.id = res.data[0].id;
          this.loginForm.roleId = res.data[0].roleId;
          this.loginForm.no = res.data[0].no;
          this.loginForm.name = res.data[0].name;
          this.loginForm.phone = res.data[0].phone;
          this.centerDialogVisible=true;
        }else{
          alert('请检查账号，姓名，联系方式是否正确输入或该账户已被停用，请联系管理员！');
          return false;
        }
      })
    },
    resetForm() {
      this.$refs.loginFrom.resetFields();
      this.centerDialogVisible=false;
    },
    login(){
      this.$router.replace('/');
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
  height: 400px;
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