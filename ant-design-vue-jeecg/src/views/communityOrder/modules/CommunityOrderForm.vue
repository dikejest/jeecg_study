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
<!--          <a-col :span="24">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <j-select-depart v-model="model.sysOrgCode" multi  />
            </a-form-model-item>
          </a-col>-->
<!--          <a-col :span="24">
            <a-form-model-item label="入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderId">
              <a-input v-model="model.orderId" placeholder="请输入入库单号"  ></a-input>
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="口罩" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mask">
              <a-input-number v-model="model.mask" placeholder="请输入口罩" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="橡胶手套" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rubberGloves">
              <a-input-number v-model="model.rubberGloves" placeholder="请输入橡胶手套" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="一次性手套" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disposableGloves">
              <a-input-number v-model="model.disposableGloves" placeholder="请输入一次性手套" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="护目镜" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goggle">
              <a-input-number v-model="model.goggle" placeholder="请输入护目镜" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="消毒液" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="disinfectant">
              <a-input-number v-model="model.disinfectant" placeholder="请输入消毒液" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="酒精" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alcohol">
              <a-input-number v-model="model.alcohol" placeholder="请输入酒精" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="体温枪" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperatureGun">
              <a-input-number v-model="model.temperatureGun" placeholder="请输入体温枪" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>
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
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: 'CommunityOrderForm',
    components: {JSearchSelectTag
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
        selectValue:"",
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
          add: "/communityOrder/communityOrder/add",
          edit: "/communityOrder/communityOrder/edit",
          queryById: "/communityOrder/communityOrder/queryById",
          queryArea: "/community/QueryArea"
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