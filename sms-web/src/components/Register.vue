<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="login-content">
        <h1 class="login-title">用户注册</h1>
        <el-form :model="loginForm" label-width="100px"
                 :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="width: 200px" type="text" v-model="loginForm.no"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input style="width: 200px" type="password" v-model="loginForm.password"
                      auto-complete="off" size="small" ></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input style="width: 200px" type="text" v-model="loginForm.name"
                      auto-complete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>
          <el-form-item label="联系方式" prop="phone">
            <el-input style="width: 200px" type="text" v-model="loginForm.phone"
                      auto-complete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="register" :disabled="confirm_disabled">注 册</el-button>
            <el-button type="primary" @click="login" :disabled="confirm_disabled">返 回 登 录</el-button>
          </el-form-item>

        </el-form>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if (this.loginForm.id) {
        return callback();
      }
      this.$axios.get(this.$httpUrl + "/user/findByNo?no=" + this.loginForm.no).then(res => res.data).then(res => {
        if (res.code != 200) {
          callback();
        } else {
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      confirm_disabled: false,
      loginForm: {
        no: '',
        password: '',
        name: '',
        phone: '',
        roleId: '2',
        isValid: 'Y',
      },
      rules: {
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {validator: checkDuplicate, trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名字', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '手机号不能为空', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur'}
        ]
      }
    }
  },

  methods: {
    register() {
      console.log(this.loginForm)
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl + '/user/register', this.loginForm).then(res => res.data).then(res => {
            if (res.code === 200) {
              this.$message({
                showClose: true,
                message: '注册成功！',
                type: 'success'
              });
            }
          })
          this.$router.replace('/');
        } else {
          this.confirm_disabled = false;
          alert('校验失败，请检查账户，密码等是否正确输入！');
          return false;
        }
      })
    },
    login() {
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
  height: 500px;
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