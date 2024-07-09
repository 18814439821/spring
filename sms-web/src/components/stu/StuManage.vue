<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>

      <el-popconfirm title="确定删除吗？" @confirm="del()" style="margin-left: 5px">
        <el-button slot="reference" type="danger" size="small" >删除</el-button>
      </el-popconfirm>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="downloadExcel">下载</el-button>
      <el-radio v-model="isvalid" label="Y" border size="small" style="margin-left: 10px;margin-top: 5px">在校</el-radio>
      <el-radio v-model="isvalid" label="N" border size="small" style="margin-left: 0px;margin-top: 5px">离校</el-radio>
      <el-button >
        <el-upload
            class="upload"
            action="/api/upload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            :limit="3"
            :http-request="customUpload"
            :on-exceed="handleExceed"
            :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传文件</div>
        </el-upload>
      </el-button>
    </div>
    <el-main style="height: 100%; padding:0px; padding-bottom: 5px">
      <el-table :data="tableData"
                :header-cell-style="{backgroundColor:'#c2ccd0',color:'#000000',fontStyle:''}"
                border
                style="{border-radius: 4px}"
                ref="multipleTable"
                highlight-current-row
                @selection-change="handleSelectionChange"
      >
        <el-table-column
            type="selection"

            width="55"/>
        <el-table-column prop="id" label="ID" >
        </el-table-column>
        <el-table-column prop="className" label="班级" >
        </el-table-column>
        <el-table-column prop="name" label="姓名" >
        </el-table-column>
        <el-table-column prop="no" label="学号" >
        </el-table-column>
        <el-table-column prop="phone" label="电话" >
        </el-table-column>
        <el-table-column prop="grade" label="年级" >
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
        width="50%"
        center
        :before-close="handleClose">
      <el-form ref="form" :model="form"  label-width="80px">
        <el-form-item label="班级" prop="className" style="margin-left: 35%">
          <el-input v-model="form.className"  :disabled="true" style="width: 200px" ></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="name" style="margin-left: 35%">
          <el-input v-model="form.name"  :disabled="true" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="no" style="margin-left: 35%">
          <el-input v-model="form.no"  :disabled="true" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone" style="margin-left: 35%">
          <el-input v-model="form.phone"  style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="年级" prop="grade" style="margin-left: 35%">
          <el-input v-model="form.grade"  style="width: 200px"></el-input>
        </el-form-item>

        <div class="block" style="margin-left: 35%">
          出生日期
          <el-date-picker
              v-model="form.birthDate"
              type="date"
              placeholder="选择日期"
              style="margin-left: 10px;margin-bottom: 20px">
          </el-date-picker>
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
import { utils } from 'xlsx';
import XLSX from 'xlsx';
import { saveAs } from 'file-saver';
export default {
  name: "StuManage",
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
        no:'',
        phone:'',
        roleId:'2',
        className:'',
        grade:'',
        isvalid:'',
        birthDate:'',
      },
      multipleSelection: []
    }
  },
  methods:{
    //文件下载
    downloadExcel() {

      // 使用xlsx的utils.json_to_sheet函数将JSON数据转换为工作表对象
      const ws = utils.json_to_sheet(this.tableData);

      // 创建一个新的工作簿，并将工作表添加到工作簿中
      const wb = utils.book_new();
      utils.book_append_sheet(wb, ws, 'Sheet1'); // 注意这里也可以使用 XLSX.utils，但 utils 是从 xlsx 导入的，所以可以直接使用

      // 将工作簿写入blob对象
      // 注意这里使用 XLSX.write 而不是单独的 write
      const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });
      console.log('downloadExcel method finished');
      // 创建一个Blob对象
      const blob = new Blob([wbout], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

      // 使用FileSaver.js的saveAs函数保存文件
      saveAs(blob, 'students.xlsx');

    },
    //文件上传
    // 自定义上传方法
    customUpload(options) {
      let formData = new FormData();
      formData.append('file', options.file); // 假设后端接收的字段名为'file'

      // 使用axios或其他HTTP库发送请求
      this.$axios.post(this.$httpUrl+'/api/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        // 处理上传成功后的逻辑
        this.fileList.push({ name: options.file.name, url: response.data.url }); // 假设后端返回了文件的URL
      }).catch(error => {
        // 处理上传失败后的逻辑
        console.error(error);
      })},

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
        this.form.specialGroup=row.specialGroup;
        this.form.phone=row.phone;
        this.form.no=row.no;
        this.form.className=row.className;
        this.form.grade=row.grade;
        this.form.isvalid=row.isvalid;
        this.form.birthDate=row.birthDate;
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
      if (this.multipleSelection!=null){
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
      }
    },
    add(){

      this.centerDialogVisible = true
      //异步执行
      this.$nextTick(()=>{
        this.resetForm()
      })

    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/stu/update',this.form).then(res=>res.data).then(res=>{
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