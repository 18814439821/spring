<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="add">新增</el-button>
    </div>
    <el-main style="height: 100%; padding:0px; padding-bottom: 5px">
      <el-table :data="tableData"
                :header-cell-style="{backgroundColor:'#c2ccd0',color:'#000000',}"
                border
                highlight-current-row
                style="border-radius: 4px"
                @current-change="handleCurrentChange1"
      >
        <el-table-column
            type="selection"
            width="55"/>
        <el-table-column prop="id" label="ID">
        </el-table-column>
        <el-table-column prop="no" label="账号" >
        </el-table-column>
        <el-table-column prop="name" label="姓名" >
        </el-table-column>
        <el-table-column prop="roleId" label="角色" >
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '教师')}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话">
        </el-table-column>
        <el-table-column prop="address" label="操作区">
          <template slot-scope="scope">
            <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)" style="margin-left: 5px">
              <el-button slot="reference" type="danger" size="small" >删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[1, 3, 5, 10]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center
        :before-close="handleClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="no">
          <el-input v-model="form.no"></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <div style="margin-top: 20px;margin-left: 85px">
          <el-radio-group v-model="form.roleId" size="medium">
            <el-radio-button label="1" >管理员  </el-radio-button>
            <el-radio-button label="2" >普通教师</el-radio-button>
          </el-radio-group>
        </div>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    let checkDuplicate=(rule,value,callback)=>{
      if (this.form.id){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
        if (res.code!=200){
          callback();
        }else {
          callback(new Error('账号已经存在'));
        }
      })
    };
    let checkAge=(rule,value,callback)=>{
      if(value>150){
        callback(new Error('年龄过大'));
      }else {
        callback();
      }
    };

    return {
      user:[],
      tableData: [],
      selectData:[],
      pageNum:1,
      pageSize:5,
      total:0,
      name:'',
      selRoleId:'',
      centerDialogVisible: false,
      form:{
        id:'',
        name:'',
        no:'',
        password:'',
        age:'',
        phone:'',
        roleId:'',
      },
      rules:{
        no:[
          {require:true,message:'请输入账号',trigger:'blur'},
          {min:3,max:8,message: '长度在 3 到 8 个字符',trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name:[
          {require:true,message:'请输入名字',trigger:'blur'}
        ],
        phone:[
          {require:true,message:'手机号不能为空',trigger:'blur'},
          {pattern:/^1[3|4|5|6|7|8|9][0-9]\d{8}$/,message: '请输入正确的手机号码',trigger: 'blur'}
        ]
      },
    }
  },
  methods:{


    handleCurrentChange1(val) {

      this.selectData=val;
      this.form.id=this.selectData.id;
    },
    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {

              done();
              this.resetForm();
          })
          .catch(()=> {});
    },
    resetForm() {
      this.form.id='';
      this.form.name='';
      this.form.password='';
      this.form.phone='';
      this.form.no='';
      this.form.roleId='';
      this.$refs.form.resetFields();
      console.log(this.form)
    },
    mod(row){
      console.log(row)

      this.$nextTick(()=>{
        //赋值到表单
        this.form.id=row.id;
        this.form.name=row.name;
        this.form.password='';
        this.form.phone=row.phone;
        this.form.no=row.no;
        this.form.roleId=row.roleId;
        this.centerDialogVisible=true;
      })
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/user/delete?id='+id).then(res=>res.data).then(res=>{
        if (res.code===200){
          this.$message({
            showClose: true,
            message: '数据删除成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
        }else {
          this.$message({
            showClose: true,
            message: '数据删除失败！',
            type: 'error'
          });
        }
      })
      // console.log(id)
    },
    add(){

      this.centerDialogVisible = true
      //异步执行
      this.$nextTick(()=>{
        this.resetForm()
      })

    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        console.log(res.code)
        if (res.code===200){
          this.$message({
            showClose: true,
            message: '数据添加成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
        }else {
          this.$message({
            showClose: true,
            message: '数据添加失败！',
            type: 'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        console.log(res.code)
        if (res.code===200){
          this.$message({
            showClose: true,
            message: '数据修改成功！',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
        }else {
          this.$message({
            showClose: true,
            message: '数据修改失败！',
            type: 'error'
          });
        }
      })
    },
    save(){
      this.$refs.form.validate((valid)=>{
        if(valid){
          if (this.form.id){
            this.doMod()
          }else{
            this.doSave()
          }

        }else{
          console.log('error submit!');
          return false;
        }
      })

    },

    reSet(){
      this.name='';
      this.sex='';
      this.pageNum=1;
      this.loadPost()
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val;
      this.pageNum=1;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    loadPost(){
    const user = sessionStorage.getItem("CurUser")
    if(user){
      this.user = JSON.parse(user);
    }
    console.log(this.user)
    console.log(this.user.roleId)
    this.$axios.post(this.$httpUrl+'/user/result',{
      pageSize:this.pageSize,
      pageNum:this.pageNum,
      param:{
        name:this.name,
        roleId:this.user.roleId+''
      }

    }).then(res=>res.data).then(res=>{
      console.log(res)
      if (res.code===200){
        this.tableData=res.data;
        this.total=res.total;
      }else {
        alert('获取数据失败')
      }
    })
  },
},
  beforeMount() {
    this.loadPost();
  },


}
</script>

<style scoped>

</style>