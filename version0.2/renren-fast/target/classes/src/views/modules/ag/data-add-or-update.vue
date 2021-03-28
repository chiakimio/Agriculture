<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="数据来源" prop="datafrom">
      <el-input v-model="dataForm.datafrom" placeholder="数据来源"></el-input>
    </el-form-item>
    <el-form-item label="国家名称" prop="countrycn">
      <el-input v-model="dataForm.countrycn" placeholder="国家名称"></el-input>
    </el-form-item>
    <el-form-item label="国家名称-英文" prop="countryen">
      <el-input v-model="dataForm.countryen" placeholder="国家名称-英文"></el-input>
    </el-form-item>
    <el-form-item label="省份名称" prop="provincecn">
      <el-input v-model="dataForm.provincecn" placeholder="省份名称"></el-input>
    </el-form-item>
    <el-form-item label="省份名称 英文" prop="provinceen">
      <el-input v-model="dataForm.provinceen" placeholder="省份名称 英文"></el-input>
    </el-form-item>
    <el-form-item label="年份" prop="yearjc">
      <el-input v-model="dataForm.yearjc" placeholder="年份"></el-input>
    </el-form-item>
    <el-form-item label="物种" prop="species">
      <el-input v-model="dataForm.species" placeholder="物种"></el-input>
    </el-form-item>
    <el-form-item label="经度（°E）" prop="longitude">
      <el-input v-model="dataForm.longitude" placeholder="经度（°E）"></el-input>
    </el-form-item>
    <el-form-item label="纬度（°N）" prop="dimension">
      <el-input v-model="dataForm.dimension" placeholder="纬度（°N）"></el-input>
    </el-form-item>
    <el-form-item label="δ13C" prop="a13c">
      <el-input v-model="dataForm.a13c" placeholder="δ13C"></el-input>
    </el-form-item>
    <el-form-item label="δ15N" prop="a15n">
      <el-input v-model="dataForm.a15n" placeholder="δ15N"></el-input>
    </el-form-item>
    <el-form-item label="δ2H" prop="a2h">
      <el-input v-model="dataForm.a2h" placeholder="δ2H"></el-input>
    </el-form-item>
    <el-form-item label="δ18O" prop="a18o">
      <el-input v-model="dataForm.a18o" placeholder="δ18O"></el-input>
    </el-form-item>
    <el-form-item label="δ32S" prop="a32s">
      <el-input v-model="dataForm.a32s" placeholder="δ32S"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          datafrom: '',
          countrycn: '',
          countryen: '',
          provincecn: '',
          provinceen: '',
          yearjc: '',
          species: '',
          longitude: '',
          dimension: '',
          a13c: '',
          a15n: '',
          a2h: '',
          a18o: '',
          a32s: ''
        },
        dataRule: {
          datafrom: [
            { required: true, message: '数据来源不能为空', trigger: 'blur' }
          ],
          countrycn: [
            { required: true, message: '国家名称不能为空', trigger: 'blur' }
          ],
          countryen: [
            { required: true, message: '国家名称-英文不能为空', trigger: 'blur' }
          ],
          provincecn: [
            { required: true, message: '省份名称不能为空', trigger: 'blur' }
          ],
          provinceen: [
            { required: true, message: '省份名称 英文不能为空', trigger: 'blur' }
          ],
          yearjc: [
            { required: true, message: '年份不能为空', trigger: 'blur' }
          ],
          species: [
            { required: true, message: '物种不能为空', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '经度（°E）不能为空', trigger: 'blur' }
          ],
          dimension: [
            { required: true, message: '纬度（°N）不能为空', trigger: 'blur' }
          ],
          a13c: [
            { required: true, message: 'δ13C不能为空', trigger: 'blur' }
          ],
          a15n: [
            { required: true, message: 'δ15N不能为空', trigger: 'blur' }
          ],
          a2h: [
            { required: true, message: 'δ2H不能为空', trigger: 'blur' }
          ],
          a18o: [
            { required: true, message: 'δ18O不能为空', trigger: 'blur' }
          ],
          a32s: [
            { required: true, message: 'δ32S不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ag/data/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.datafrom = data.data.datafrom
                this.dataForm.countrycn = data.data.countrycn
                this.dataForm.countryen = data.data.countryen
                this.dataForm.provincecn = data.data.provincecn
                this.dataForm.provinceen = data.data.provinceen
                this.dataForm.yearjc = data.data.yearjc
                this.dataForm.species = data.data.species
                this.dataForm.longitude = data.data.longitude
                this.dataForm.dimension = data.data.dimension
                this.dataForm.a13c = data.data.a13c
                this.dataForm.a15n = data.data.a15n
                this.dataForm.a2h = data.data.a2h
                this.dataForm.a18o = data.data.a18o
                this.dataForm.a32s = data.data.a32s
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ag/data/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'datafrom': this.dataForm.datafrom,
                'countrycn': this.dataForm.countrycn,
                'countryen': this.dataForm.countryen,
                'provincecn': this.dataForm.provincecn,
                'provinceen': this.dataForm.provinceen,
                'yearjc': this.dataForm.yearjc,
                'species': this.dataForm.species,
                'longitude': this.dataForm.longitude,
                'dimension': this.dataForm.dimension,
                'a13c': this.dataForm.a13c,
                'a15n': this.dataForm.a15n,
                'a2h': this.dataForm.a2h,
                'a18o': this.dataForm.a18o,
                'a32s': this.dataForm.a32s
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
