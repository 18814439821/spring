<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
      @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="sex" clearable placeholder="请选择性别" style="width: 120px;margin-left: 5px">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="add">新增</el-button>
    </div>
    <el-main style="height: 100%; padding:0px; padding-bottom: 5px">
      <el-table :data="tableData"
                :header-cell-style="{background:'darkgray',color:'#000000',}"
                border
                style="border-radius: 4px"
      >
        <el-table-column prop="id" label="ID" width="60">
        </el-table-column>
        <el-table-column prop="no" label="账号" width="120">
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120">
        </el-table-column>
        <el-table-column prop="roleId" label="角色" width="120">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '普通账号')}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sex" label="性别" width="80">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.sex === 1 ? 'primary' : 'success'"
                disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
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
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别" >
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>

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
  name: "Main",
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
      tableData: [],
      pageNum:1,
      pageSize:5,
      total:0,
      name:'',
      sex:'',
      centerDialogVisible: false,
      form:{
        id:'',
        name:'',
        no:'',
        password:'',
        age:'',
        sex:'1',
        phone:'',
        roleId:'1',
      },
      rules:{
        no:[
          {required:true,message:'请输入账号',trigger:'blur'},
          {min:3,max:8,message: '长度在 3 到 8 个字符',trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name:[
          {required:true,message:'请输入名字',trigger:'blur'}
        ],
        password:[
          {required:true,message:'请输入密码',trigger:'blur'},
          {min:3,max:8,message: '长度在 3 到 8 个字符',trigger: 'blur'}
        ],
        age:[
          {required:true,message:'请输入年龄',trigger:'blur'},
          {min:1,max:3,message: '长度在 1 到 8 个字符',trigger:'blur'},
          {pattern:/^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数',trigger: 'blur'},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone:[
          {required:true,message:'手机号不能为空',trigger:'blur'},
          {pattern:/^1[3|4|5|6|7|8|9][0-9]\d{8}$/,message: '请输入正确的手机号码',trigger: 'blur'}
        ]
      },
      sexs:[{
        value:'1',
        label:'男'
      },{
        value:'0',
        label:'女'
      }]
    }
  },
  methods:{


    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {
            done();
          })
          .catch(()=> {});
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    mod(row){
      console.log(row)

      this.$nextTick(()=>{
        //赋值到表单
        this.form.id=row.id;
        this.form.name=row.name;
        this.form.sex=row.sex +'';
        this.form.password='';
        this.form.age=row.age +'';
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
      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
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
    loadGet(){
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
        console.log(res)
      })
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/result',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          sex:this.sex
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
    // this.loadGet();
    this.loadPost();
  },


}
</script>

<style scoped>

</style>