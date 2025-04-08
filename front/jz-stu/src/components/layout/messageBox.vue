<template>
  <div class="city_select">
    <el-tabs :tab-position="tabPosition" style="height: 400px" class="demo-tabs">
      <el-tab-pane label="热门城市">
        <div class="tab_pane">
          <div class="list-select-list">
            <ul>
              <li v-for="city in a" :key="city.id" :style="{ width: '124px' }">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="ABCDE">
        <div class="tab_pane" v-for="item in 'A.B.C.D.E'.split('.')" :key="item">
          <span>{{ item }}</span>
          <div class="list-select-list">
            <ul>
              <li v-for="city in groupedCity[item]" :key="city.id">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="FGHIJ">
        <div class="tab_pane" v-for="item in 'F.G.H.J'.split('.')" :key="item">
          <span>{{ item }}</span>
          <div class="list-select-list">
            <ul>
              <li v-for="city in groupedCity[item]" :key="city.id">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div></el-tab-pane
      >
      <el-tab-pane label="KLMN">
        <div class="tab_pane" v-for="item in 'K.L.M.N'.split('.')" :key="item">
          <span>{{ item }}</span>
          <div class="list-select-list">
            <ul>
              <li v-for="city in groupedCity[item]" :key="city.id">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div></el-tab-pane
      >
      <el-tab-pane label="PQRST">
        <div class="tab_pane" v-for="item in 'P.Q.R.S.T'.split('.')" :key="item">
          <span>{{ item }}</span>
          <div class="list-select-list">
            <ul>
              <li v-for="city in groupedCity[item]" :key="city.id">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="WXYZ">
        <div class="tab_pane" v-for="item in 'W.X.Y.Z'.split('.')" :key="item">
          <span>{{ item }}</span>
          <div class="list-select-list">
            <ul>
              <li v-for="city in groupedCity[item]" :key="city.id">
                <a href="#" @click="changedCity(city)">{{ city }}</a>
              </li>
            </ul>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import type { TabsInstance } from 'element-plus'
import { getCityData } from '@/api/city.js'
import pinyin from 'pinyin'
import { log } from 'console'
import { getJobInfo } from '@/api/job.js'
import { useJobStore } from '@/stores/job.js'

const jobStore = useJobStore()

const changedCity = async (city: string) => {
  jobStore.page = 1
  //当点击城市之后更新job.js的beux的WorkLocation数据
  if (city == '全国') {
    jobStore.updateWorkLocation('')
  } else {
    jobStore.updateWorkLocation(city)
  }
  //再次查询job的page信息（带参数）
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  )
  jobStore.setResData(res)
  jobStore.page++
}

const tabPosition = ref<TabsInstance['tabPosition']>('top')
// 所有省市区数据
const allList = ref([])
// 城市数据
const cityList = ref([])
// 选择的城市
const selectedCity = ref('')
// 按照拼音分组城市
const groupedCity = ref({})
//点击的地点的数据
const clickedCity = ref([])

onMounted(async () => {
  // 获取城市数据
  const res = await getCityData()
  //将所有数据都翻入allList
  allList.value.push(...res)
  //将所有城市数据都填入cityList
  allList.value.forEach((aItem) => {
    aItem.areaList.forEach((bItem) => {
      cityList.value.push(bItem.name)
    })
  })

  //将处理之后的dealGroupedCities按照拼音分组城市
  const groupedCities = dealGroupedCities(cityList.value).reduce((acc, city) => {
    const pinyinResult = pinyin(city, { style: pinyin.STYLE_FIRST_LETTER })
    if (pinyinResult && pinyinResult[0] && pinyinResult[0][0]) {
      const firstLetter = pinyinResult[0][0].toUpperCase()
      if (!acc[firstLetter]) {
        acc[firstLetter] = []
      }
      acc[firstLetter].push(city)
    }
    return acc
  }, {})
  // 输出分组后的城市并赋值groupedCity
  groupedCity.value = groupedCities
})

const dealGroupedCities = (citys) => {
  return citys.map((item) => {
    // 检查是否包含“市”或“自治区”，如果包含则去掉
    if (item.includes('市')) {
      return item.replace('市', '') // 去掉“市”
    } else if (item.includes('自治区')) {
      return item.replace('自治区', '') // 去掉“自治区”
    }
    return item // 如果不包含，则返回原字符串
  })
}
</script>

<style lang='less'>
.city_select {
  width: 670px;

  .el-tab-pane {
    .tab_pane {
      display: flex;
      margin-top: 2px;
      padding: 0px;
      span {
        padding: 0 40px;
        width: 10px;
        font-size: 20px;
        margin-right: 4px;
      }
      .list-select-list {
        ul {
          margin: 0px;
          padding: 0px;
          font-size: 13px;
          li {
            display: inline-flex; //挤下去会发现上下不齐如果用行内快=块
            justify-content: center; /* 水平居中对齐 */
            width: 103px;
            height: 40px;
            margin-right: 10px;
            margin-top: 2px;
            text-align: center;
            a {
              color: #4c5569;
              text-decoration: none;
            }
          }
        }
      }
    }
  }
  .el-tabs__content {
    padding: 0px !important;
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 8px; /* 设置滚动条的宽度 */
      // height: 10px; /* 设置滚动条的高度（对于水平滚动条） */
    }
    &::-webkit-scrollbar-thumb {
      background-color: #cbcbcb; /* 滚动条的颜色 */
      border-radius: 15px; /* 滚动条的圆角 */
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: #b3b2b2; /* 鼠标悬停时的颜色 */
    }
  }
  .list-select-list {
  }
  .el-tabs__item {
    margin-right: 30px;
  }
}

.demo-tabs > .el-tabs__content {
  padding: 40px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
  height: 100%;
}
</style>