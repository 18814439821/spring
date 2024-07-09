<template>
  <div>
    <div style="margin-bottom:5px">
      <el-input placeholder="请输入名字" style="width: 200px;" suffix-icon="el-icon-search" v-model="name"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" size="small" style="margin-left: 5px;" @click="loadPost">查询</el-button>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="reSet">重置</el-button>
      <el-radio v-model="isvalid" label="Y" border size="small" style="margin-left: 10px;margin-top: 5px">在校</el-radio>
      <el-radio v-model="isvalid" label="N" border size="small" style="margin-left: 0px;margin-top: 5px">离校</el-radio>
      <el-button type="success" size="small" style="margin-left: 5px;" @click="downloadExcel">下载</el-button>
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
        <el-table-column prop="className" label="班级" >
        </el-table-column>
        <el-table-column prop="name" label="姓名">
        </el-table-column>
        <el-table-column prop="no" label="学号" >
        </el-table-column>
        <el-table-column prop="phone" label="电话">
        </el-table-column>
        <el-table-column prop="grade" label="年级" >
        </el-table-column>
        <el-table-column prop="annualHouseholdIncome" label="人均年收入情况" >
          <template slot-scope="scope">
            <el-tag
                :type="(scope.row.annualHouseholdIncome/scope.row.familyMember/12)<5000 ? 'success' : 'danger'"
                disable-transitions>{{ (scope.row.annualHouseholdIncome/scope.row.familyMember/12)<5000 ? '人均年收入低' : '人均年收入一般'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isvalid" label="是否为特殊群体" >
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.specialGroup !== 'null' ? 'success' : 'danger'"
                disable-transitions>{{scope.row.specialGroup !== 'null' ? '特殊群体' : '非特殊群体'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="emergencySituation" label="是否有突发状况" >
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.emergencySituation !== 'null' ? 'success' : 'danger'"
                disable-transitions>{{scope.row.emergencySituation !== 'null' ? '有突发状况' : '无突发状况'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isvalid" label="是否在校">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.isvalid === 'Y' ? 'success' : 'danger'"
                disable-transitions>{{scope.row.isvalid === 'Y' ? '在校' : '已毕业'}}</el-tag>
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
  </div>
</template>

<script>
import XLSX, {utils} from "xlsx";

export default {
  name: "PoorStuManage",
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
        password:'',
        phone:'',
        roleId:'2',
        familyMember: '',
        annualHouseholdIncome:'',
        specialGroup:'',
        emergencySituation:'',
      },
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
    handleClose(done){
      this.$confirm('确认关闭？')
          .then(()=> {

            done();
            this.resetForm();
          })
          .catch(()=> {});
    },
    add(){

      this.centerDialogVisible = true
      //异步执行
      this.$nextTick(()=>{
        this.resetForm()
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
      this.$axios.post(this.$httpUrl+'/stu/resultPS',{
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