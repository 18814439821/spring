<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>
      <el-radio v-model="isvalid" label="Y" border size="small" style="margin-left: 10px;margin-top: 5px">在校</el-radio>
      <el-radio v-model="isvalid" label="N" border size="small" style="margin-left: 0px;margin-top: 5px">离校</el-radio>
    </div>
    <el-main style="height: 100%; padding:0px; padding-bottom: 5px">
      <el-table :data="tableData"
                :header-cell-style="{backgroundColor:'#c2ccd0',color:'#000000',fontStyle:''}"
                border
                style="{border-radius: 4px}"
                ref="multipleTable"
                @selection-change="handleSelectionChange"
      >
        <el-table-column prop="id" label="ID" >
        </el-table-column>
        <el-table-column prop="name" label="姓名" >
        </el-table-column>
        <el-table-column prop="familyMember" label="家庭成员数" >
        </el-table-column>
        <el-table-column prop="annualHouseholdIncome" label="家庭年收入" >
        </el-table-column>
        <el-table-column prop="familyAddress" label="家庭地址" >
        </el-table-column>
        <el-table-column prop="isvalid" label="是否在校" >
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.isvalid === 'Y' ? 'success' : 'danger'"
                disable-transitions>{{scope.row.isvalid === 'Y' ? '在校' : '已毕业'}}</el-tag>
          </template>
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
        width="30%"
        center
        :before-close="handleClose">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="名字" prop="name" >
          <el-input v-model="form.name"  :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="家庭成员数" prop="familyMember">
        <el-input v-model="form.familyMember"></el-input>
        </el-form-item>
        <el-form-item label="家庭地址" prop="familyAddress">
          <el-input
            type="textarea"
            autosize
            placeholder="请输入内容"
            v-model="form.familyAddress"
            style="margin-bottom: 20px">
          </el-input>
        </el-form-item>
        <el-form-item label="家庭年收入" prop="familyAddress">
          <el-input v-model="form.annualHouseholdIncome"></el-input>
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
  name: "BFInformation",
  data() {
    return {
      tableData: [],
      pageNum:1,
      pageSize:10,
      total:0,
      name:'',
      isvalid:'null',
      centerDialogVisible: false,
      radio:'1',
      form:{
        id:'',
        name:'',
        familyMember:'',
        annualHouseholdIncome:'',
        familyAddress:'',
      },
      multipleSelection: []
    }
  },
  methods:{
    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {

            done();
            this.resetForm();
          })
          .catch(()=> {});
    },
    mod(row){
      console.log(row)

      this.$nextTick(()=>{
        //赋值到表单
        this.form.id=row.id;
        this.form.name=row.name;
        this.form.familyMember=row.familyMember;
        this.form.annualHouseholdIncome=row.annualHouseholdIncome;
        this.form.familyAddress=row.familyAddress;
        this.centerDialogVisible=true;
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    getId(Array){
      for (let i = 0; i < Array.length; i++) {

      }
    },
    del(){
      console.log(this.multipleSelection)
      const ids = this.multipleSelection.map(stu => stu.id);
      console.log(ids)
      this.$axios.post(this.$httpUrl+'/stu/delete',ids).then(res=>res.data).then(res=>{
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
    doMod(){
      this.$axios.post(this.$httpUrl+'/stu/save',this.form).then(res=>res.data).then(res=>{
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
          }
        }else{
          console.log('error submit!');
          return false;
        }
      })

    },

    reSet(){
      this.name='';
      this.isvalid='';
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
      this.$axios.post(this.$httpUrl+'/stu/result',{
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
  },
  beforeMount() {
    this.loadPost();
  },


}
</script>

<style scoped>

</style>