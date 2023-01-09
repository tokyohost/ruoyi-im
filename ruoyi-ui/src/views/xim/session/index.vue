<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="sessionId" prop="sessionid">
        <el-input
          v-model="queryParams.sessionid"
          placeholder="请输入sessionId"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起会话用户" prop="sendUid">
        <el-input
          v-model="queryParams.sendUid"
          placeholder="请输入发起会话用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收会话用户" prop="receiveUid">
        <el-input
          v-model="queryParams.receiveUid"
          placeholder="请输入接收会话用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话类型" prop="status">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable>
          <el-option
            v-for="dict in dict.type.chat_session_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xim:session:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['xim:session:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xim:session:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xim:session:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sessionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="sessionId" align="center" prop="sessionid" />
      <el-table-column label="会话创建时间" align="center" prop="createtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会话类型" align="center" prop="type" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.chat_session_type" :value="scope.row.type"/>
        </template>

      </el-table-column>
      <el-table-column label="第一条消息发送时间" align="center" prop="firstMsgTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstMsgTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后一条消息发送时间" align="center" prop="lastMsgTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastMsgTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="uidHash" align="center" prop="uidHash" />
      <el-table-column label="发起会话用户" align="center" prop="sendUid" />
      <el-table-column label="接收会话用户" align="center" prop="receiveUid" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xim:session:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xim:session:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会话管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="sessionId" prop="sessionid">
          <el-input v-model="form.sessionid" placeholder="请输入sessionId" />
        </el-form-item>
        <el-form-item label="第一条消息发送时间" prop="firstMsgTime">
          <el-date-picker clearable
            v-model="form.firstMsgTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择第一条消息发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="最后一条消息发送时间" prop="lastMsgTime">
          <el-date-picker clearable
            v-model="form.lastMsgTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最后一条消息发送时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="会话类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择" clearable>
            <el-option
              v-for="dict in dict.type.chat_session_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="uidHash" prop="uidHash">
          <el-input v-model="form.uidHash" placeholder="请输入uid 的混淆结果" />
        </el-form-item>
        <el-form-item label="发起会话用户" prop="sendUid">
          <el-input v-model="form.sendUid" placeholder="请输入发起会话用户" />
        </el-form-item>
        <el-form-item label="接收会话用户" prop="receiveUid">
          <el-input v-model="form.receiveUid" placeholder="请输入接收会话用户" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSession, getSession, delSession, addSession, updateSession } from "@/api/xim/session";

export default {
  name: "Session",
  dicts: ['chat_session_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 会话管理表格数据
      sessionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sessionid: null,
        sendUid: null,
        receiveUid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sessionid: [
          { required: true, message: "sessionId不能为空", trigger: "blur" }
        ],
        uidHash: [
          { required: true, message: "uid 的混淆结果，简单混淆可以按一定规则不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询会话管理列表 */
    getList() {
      this.loading = true;
      listSession(this.queryParams).then(response => {
        this.sessionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sessionid: null,
        createtime: null,
        type: null,
        firstMsgTime: null,
        lastMsgTime: null,
        uidHash: null,
        sendUid: null,
        receiveUid: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会话管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSession(id).then(response => {
        this.form = response.data;
        this.form.type = this.form.type+""
        this.open = true;
        this.title = "修改会话管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSession(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSession(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除会话管理编号为"' + ids + '"的数据项？').then(function() {
        return delSession(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xim/session/export', {
        ...this.queryParams
      }, `session_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
