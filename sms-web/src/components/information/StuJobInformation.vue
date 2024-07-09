<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入学生姓名" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="add">新增</el-button>
    </div>
    <el-main style="height: 100%; padding:0px; padding-bottom: 5px">
      <el-table :data="tableData"
                :header-cell-style="{backgroundColor:'#c2ccd0',color:'#000000',fontStyle:''}"
                border
                style="{border-radius: 4px}"
                ref="multipleTable"

      >
        <el-table-column prop="id" label="ID" >
        </el-table-column>
        <el-table-column prop="name" label="学生姓名">
        </el-table-column>
        <el-table-column prop="jobName" label="工作名称">
        </el-table-column>
        <el-table-column prop="principal" label="负责人" >
        </el-table-column>
        <el-table-column prop="phone" label="联系方式" >
        </el-table-column>

        <el-table-column prop="address" label="操作区">
          <template slot-scope="scope">
            <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>


          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10,30,50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="50%"
        center
        :before-close="handleClose">
      <el-form ref="jobForm" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="学生姓名" prop="name">
          <el-input v-model="form.name" :disabled="true" ></el-input>
        </el-form-item>
        <el-form-item  label="工作名称" prop="jobName">
          <el-select v-model="form.jobName" placeholder="请选择分类" style="margin-left: 10px"
                     @change="jobNameChange">
            <el-option
                v-for="item in jobData"
                :key="item.jobName"
                :label="item.jobName"
                :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="负责人" prop="principal">
          <el-input v-model="form.principal"  :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" :disabled="true"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible1"
        width="50%"
        center
        :before-close="handleClose">
      <el-form ref="jobForm" :model="form" :rules="rules" label-width="150px">

        <el-form-item label="学生姓名" prop="name">
          <el-input v-model="form.name"  :disabled="true" ></el-input>
        </el-form-item>
        <el-form-item label="学生学号" prop="stuNo">
          <el-select v-model="form.stuNo" placeholder="请选择学生" style="margin-left: 10px"
                     @change="stuNameChange">
            <el-option
                v-for="item in stuData"
                :key="item.id"
                :label="item.no"
                :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item  label="工作名称" prop="jobName">
          <el-select v-model="form.jobName" placeholder="请选择工作名称" style="margin-left: 10px"
                     @change="jobNameChange">
            <el-option
                v-for="item in jobData"
                :key="item.jobName"
                :label="item.jobName"
                :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="剩余岗位" prop="surplusPosition">
          <el-input v-model="form.surplusPosition" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="负责人" prop="principal">
          <el-input v-model="form.principal"  :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" :disabled="true"></el-input>
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
  name: "SPTJInformation",
  data() {
    return {
      tableData: [],
      jobData:[],
      stuData:[],
      jobNameList:[],
      pageNum:1,
      pageSize:10,
      total:0,
      name:'',
      centerDialogVisible: false,
      centerDialogVisible1: false,
      oldJob:'',
      radio:'1',
      form:{
        id:'',
        name:'',
        jobName:'',
        jobPosts:'',
        principal:'',
        phone:'',
        stuId:'',
        jobId:'',
        stuNo:'',
        surplusPosition:'',
      },
      rules:{
        jobName:[
          {require:true,message:'请输入工作名字',trigger:'blur'}
        ],
        jobPosts:[
          {require:true,message:'请输入岗位总人数',trigger:'blur'}
        ],
        principal:[
          {require:true,message:'请输入联系人名称',trigger:'blur'}
        ],
        phone:[
          {require:true,message:'手机号不能为空',trigger:'blur'},
          {pattern:/^1[3|4|5|6|7|8|9][0-9]\d{8}$/,message: '请输入正确的手机号码',trigger: 'blur'}
        ]
      },
      multipleSelection: []
    }
  },
  methods:{
    jobNameChange(jobId){
      this.$axios.get(this.$httpUrl+"/job/findById?jobId="+jobId).then(res=>res.data).then(res=>{
        console.log(res);
        this.form.jobId=res.id;
        this.form.principal=res.principal;
        this.form.phone=res.phone;
        this.form.surplusPosition=res.surplusPosition;
      })
      console.log(jobId)
    },
    stuNameChange(stuId){
      console.log(stuId)
      this.$axios.get(this.$httpUrl+"/stu/findById?stuId="+stuId).then(res=>res.data).then(res=>{
        console.log(res);
        this.form.stuId=res.id;
        this.form.stuNo=res.no;
        this.form.name=res.name;
      })
          console.log(stuId)
    },
    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {
            done();
            this.$refs.jobForm.resetFields();
            this.form.id='';
            this.form.jobName='';
            this.form.jobPosts='';
            this.form.surplusPosition='';
            this.form.principal='';
            this.form.phone='';

          })
          .catch(()=> {});
    },
    resetForm() {
      this.form.id='';
      this.form.jobName='';
      this.form.jobPosts='';
      this.form.surplusPosition='';
      this.form.principal='';
      this.form.phone='';
      this.$refs.form.resetFields();
      console.log(this.form)
    },
    getId(Array){
      for (let i = 0; i < Array.length; i++) {

      }
    },
    del(){
      console.log(this.multipleSelection)
      const ids = this.multipleSelection.map(stu => stu.id);
      console.log(ids)
      this.$axios.post(this.$httpUrl+'/job/delete',ids).then(res=>res.data).then(res=>{
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
    mod(row){
      console.log(row)

      this.$nextTick(()=>{
        //赋值到表单
        this.form.id=row.id;
        this.form.jobId=row.jobId;
        this.form.stuId=row.stuId;
        this.form.jobName=row.jobName;
        this.form.jobPosts=row.jobPosts;
        this.form.name=row.name;
        this.oldJob=row.jobId;
        this.form.phone=row.phone;
        this.form.principal=row.principal;
        this.form.surplusPosition=row.surplusPosition;

        this.centerDialogVisible=true;
      })
    },
    doMod(){

      this.$axios.post(this.$httpUrl+'/stujob/update',{
        id:this.form.id,
        stu:this.form.stuId,
        job:this.form.jobId,
        old:this.oldJob,
      }).then(res=>res.data).then(res=>{
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
    doSave(){
      this.$axios.post(this.$httpUrl+'/stujob/add', {
        stu:this.form.stuId,
        job:this.form.jobId,
        surplusPosition:this.form.surplusPosition,
      }).then(res=>res.data).then(res=>{
        console.log(res)
        console.log(res.code)
        if (res.code===200){
          this.$message({
            showClose: true,
            message: '数据保存成功！',
            type: 'success'
          });
          this.centerDialogVisible1 = false
          this.loadPost()
        }else {
          this.$message({
            showClose: true,
            message: '数据保存失败！',
            type: 'error'
          });
        }
      })
    },
    save(){
      if (this.form.id){
        this.doMod()
      }else{
        this.doSave()
      }
    },
    add(){

      this.centerDialogVisible1 = true
      //异步执行
      this.$nextTick(()=>{
        this.resetForm()
      })

    },
    reSet(){
      this.name='';
      this.isvalid='';
      this.pageNum=1;
      this.specialGroup='null'
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
      this.$axios.post(this.$httpUrl+'/stujob/result',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          isvalid:this.isvalid,
          roleId:'2',
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
    loadJob(){
      this.$axios.get(this.$httpUrl+'/job/list').then(res=>res.data).then(res=>{
        // console.log(res)
        if (res.code===200){
          this.jobData=res.data
        }else{
          alert('获取数据失败')
        }
      })
    },
    loadStu(){
      this.$axios.get(this.$httpUrl+'/stu/list').then(res=>res.data).then(res=>{
        console.log("stu"+res.data)
        if (res.code===200){
          this.stuData=res.data
        }else{
          alert('获取数据失败')
        }
      })
    },
  },

  beforeMount() {
    this.loadPost();
    this.loadJob();
    this.loadStu();
  },
  mounted() {
    this.jobNameList = this.jobData;
  }

}
</script>

<style scoped>

</style>