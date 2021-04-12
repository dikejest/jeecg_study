<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="小区名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="areaId">
              <!--              <a-input v-model="model.areaId" placeholder="请输入小区id"  ></a-input>-->
              <j-search-select-tag
                placeholder="请做出你的选择"
                v-model="model.areaId"
                :dictOptions="dictOptions">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="名字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入名字"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mobile">
              <a-input v-model="model.mobile" placeholder="请输入手机号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="门牌号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="doorNo">
              <a-input v-model="model.doorNo" placeholder="请输入门牌号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="事由" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="course">
              <a-input v-model="model.course" placeholder="请输入事由"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">
              <j-dict-select-tag type="list" v-model="model.temperature" dictCode="temperature" placeholder="请选择温度" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="身份证号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="idNo">
              <a-input v-model="model.idNo" placeholder="请输入身份证号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进门 or 出门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isOpenDoor">
              <j-dict-select-tag type="radio" v-model="model.isOpenDoor" dictCode="is_open_door" placeholder="请选择进门 or 出门" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'CommunityAccessRecordsForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        dictOptions:[{
          text:"选项一",
          value:"1"
        },{
          text:"选项二",
          value:"2"
        },{
          text:"选项三",
          value:"3"
        }],
        model:{
            temperature:1,
            isOpenDoor:1,
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/communityAccessRecords/communityAccessRecords/add",
          edit: "/communityAccessRecords/communityAccessRecords/edit",
          queryById: "/communityAccessRecords/communityAccessRecords/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
      this.queryAllArea();
    },
    methods: {
      //查询所有小区id，条件部门编码筛选,然后已text：value的形式显示出来
      queryAllArea(){
        let queryUrl =''
        queryUrl ="/community/QueryArea"
        httpAction(queryUrl,'','get').then((res)=>{
          console.log(res);
          this.dictOptions = res;
        })
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>