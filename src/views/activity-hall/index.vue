<template>
   <HeaderBar
      :currentName="_t18('menu_activity_hall')"
    ></HeaderBar>
  <div class="activity-hall-container">
    
    <div class="activity-filters">
      <div class="filter-group">
        <span class="filter-label">{{ t('filter_by') }}:</span>
        <div class="filter-options">
          <div class="filter-option" 
               v-for="(filter, index) in filters" 
               :key="index"
               :class="{ active: activeFilter === index }"
               @click="activeFilter = index">
            {{ t(filter) }}
          </div>
        </div>
      </div>
      
      <div class="search-box">
        <input type="text" :placeholder="t('search_activities')" v-model="searchQuery">
      </div>
    </div>
    
    <div class="activities-grid">
      <div class="activity-card" v-for="(activity, index) in filteredActivities" :key="index" @click="activity.link ? navigateTo(activity.link) : viewActivity(activity)">
        <div class="activity-image" :style="{ backgroundImage: `url(${activity.image})` }">
          <div class="activity-status" :class="activity.status">{{ t(activity.status) }}</div>
        </div>
        <div class="activity-content">
          <h3 class="activity-title">{{ activity.title }}</h3>
          <div class="activity-period">{{ activity.startDate }} - {{ activity.endDate }}</div>
          <div class="activity-description">{{ activity.description }}</div>
        </div>
        <div class="activity-footer">
          <div class="activity-rewards">{{ t('rewards') }}: {{ activity.rewards }}</div>
          <button class="activity-button" 
                  :class="{ disabled: activity.status === 'ended' }" 
                  v-if="activity.status !== 'ended'">
            {{ t(activity.status === 'ongoing' ? 'join_now' : activity.status === 'upcoming' ? 'remind_me' : 'view_details') }}
          </button>
        </div>
      </div>
    </div>
    
    <div class="pagination">
      <button class="page-button" :disabled="currentPage === 1" @click="currentPage--">{{ t('previous') }}</button>
      <div class="page-numbers">
        <div class="page-number" 
             v-for="page in totalPages" 
             :key="page"
             :class="{ active: currentPage === page }"
             @click="currentPage = page">
          {{ page }}
        </div>
      </div>
      <button class="page-button" :disabled="currentPage === totalPages" @click="currentPage++">{{ t('next') }}</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'

const { t } = useI18n()
const router = useRouter()
const activeFilter = ref(0)
const searchQuery = ref('')
const currentPage = ref(1)
const itemsPerPage = 6

const filters = ['all_activities', 'ongoing', 'upcoming', 'ended']

const activities = [
 
  // {
  //   id: 1,
  //   title: t('activity_title_1'),
  //   description: t('activity_description_1'),
  //   startDate: '2023-08-01',
  //   endDate: '2023-08-31',
  //   status: 'ongoing',
  //   rewards: t('activity_rewards_1'),
  //   image: 'https://exeedcollege.com/wp-content/uploads/2023/07/web3-scaled.jpg',
  //   link: '/sign-in'
  // },
  {
    id: 2,
    title: t('activity_title_2'),
    description: t('activity_description_2'),
    startDate: '2023-07-15',
    endDate: '2023-09-15',
    status: 'ongoing',
    rewards: t('activity_rewards_2'),
    image: 'https://www.xrtoday.com/wp-content/uploads/2022/10/What_Web3_Going_2023.jpg'
  },
  {
    id: 5,
    title: t('activity_title_5'),
    description: t('activity_description_5'),
    startDate: '2023-01-01',
    endDate: '2023-12-31',
    status: 'ongoing',
    rewards: t('activity_rewards_5'),
    image: 'https://www.zinomall.com/wp-content/uploads/2021/08/ZM_MallBanner_2108-01-1-1024x536.jpg',
    link: '/plug'
  },
  {
    id: 3,
    title: t('activity_title_3'),
    description: t('activity_description_3'),
    startDate: '2023-09-01',
    endDate: '2023-12-31',
    status: 'ended',
    rewards: t('activity_rewards_3'),
    image: 'https://www.blockglobe24.com/wp-content/uploads/2020/09/Bitcoin-Mining-1000x521-1-640x333-1.jpg'
  },
  {
    id: 4,
    title: t('activity_title_4'),
    description: t('activity_description_4'),
    startDate: '2023-07-10',
    endDate: '2023-07-20',
    status: 'ended',
    rewards: t('activity_rewards_4'),
    image: 'https://community.iotex.io/uploads/default/original/2X/7/78bbfdf2c9a6d6ffd0fb8e7ff07646a40b81c4a5.png'
  },
  {
    id: 6,
    title: t('activity_title_6'),
    description: t('activity_description_6'),
    startDate: '2023-09-15',
    endDate: '2023-09-30',
    status: 'upcoming',
    rewards: t('activity_rewards_6'),
    image: 'https://img.foresightnews.pro/202303/ecf7fdb26accb79049b2a022d01ffdee.png?x-oss-process=style/scale70'
  },
  {
    id: 7,
    title: t('activity_title_7'),
    description: t('activity_description_7'),
    startDate: '2023-06-01',
    endDate: '2023-07-31',
    status: 'ended',
    rewards: t('activity_rewards_7'),
    image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEhNecQtS1WJkPbhmbeYjSmBCJo2pKaSiBNn_ukMXNOBpyLqVfP6PsohzJrU3C97jTtok&usqp=CAU'
  },
  {
    id: 8,
    title: t('activity_title_8'),
    description: t('activity_description_8'),
    startDate: '2023-09-10',
    endDate: '2023-09-20',
    status: 'upcoming',
    rewards: t('activity_rewards_8'),
    image: 'https://pic.52112.com/2021/02/11/23/11231813_bffc7e0afd_small.jpg'
  }
]

const filteredActivities = computed(() => {
  let result = activities

  // 应用过滤器
  if (activeFilter.value > 0) {
    const filterStatus = filters[activeFilter.value]
    result = result.filter(activity => activity.status === filterStatus)
  }

  // 应用搜索
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(activity => 
      activity.title.toLowerCase().includes(query) || 
      activity.description.toLowerCase().includes(query)
    )
  }

  // 分页
  const startIndex = (currentPage.value - 1) * itemsPerPage
  return result.slice(startIndex, startIndex + itemsPerPage)
})

const totalPages = computed(() => {
  let filteredCount = activities.length
  
  // 应用过滤器计算总数
  if (activeFilter.value > 0) {
    const filterStatus = filters[activeFilter.value]
    filteredCount = activities.filter(activity => activity.status === filterStatus).length
  }
  
  // 应用搜索计算总数
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filteredCount = activities.filter(activity => 
      activity.title.toLowerCase().includes(query) || 
      activity.description.toLowerCase().includes(query)
    ).length
  }
  
  return Math.ceil(filteredCount / itemsPerPage)
})

const viewActivity = (activity) => {
  console.log('View activity:', activity)
  // 这里可以实现查看活动详情的逻辑
}

const navigateTo = (link) => {
  router.push(link)
}
</script>

<style scoped>
.activity-hall-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  color: #fff;
}

.page-title {
  font-size: 28px;
  margin-bottom: 32px;
  text-align: center;
}

.activity-filters {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 16px;
}

.filter-label {
  color: #7A7A97;
  white-space: nowrap;
}

.filter-options {
  display: flex;
  gap: 8px;
}

.filter-option {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
  white-space: nowrap;
}

.filter-option:hover {
  background: rgba(255, 255, 255, 0.1);
}

.filter-option.active {
  background: linear-gradient(to right, #30CFD0, #c43ad6);
  color: #fff;
}

.search-box input {
  padding: 10px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: none;
  border-radius: 6px;
  color: #fff;
  width: 250px;
}

.search-box input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.1);
}

.activities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.activity-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.activity-image {
  height: 180px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.activity-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
}

.activity-status.ongoing {
  background: #4CAF50;
  color: white;
}

.activity-status.upcoming {
  background: #2196F3;
  color: white;
}

.activity-status.ended {
  background: #9E9E9E;
  color: white;
}

.activity-content {
  padding: 16px;
}

.activity-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}

.activity-period {
  color: #7A7A97;
  font-size: 14px;
  margin-bottom: 12px;
}

.activity-description {
  color: #e0e0e0;
  font-size: 14px;
  line-height: 1.5;
  height: 63px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.activity-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-rewards {
  color: #30CFD0;
  font-weight: bold;
}

.activity-button {
  padding: 8px 16px;
  background: linear-gradient(to right, #30CFD0, #c43ad6);
  border: none;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  transition: opacity 0.3s;
}

.activity-button:hover {
  opacity: 0.9;
}

.activity-button.disabled {
  background: #9E9E9E;
  cursor: default;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.page-button {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  color: white;
  cursor: pointer;
}

.page-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 8px;
}

.page-number {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 6px;
  cursor: pointer;
}

.page-number.active {
  background: linear-gradient(to right, #30CFD0, #c43ad6);
}

@media (max-width: 768px) {
  .activity-filters {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .search-box input {
    width: 100%;
  }
  
  .activities-grid {
    grid-template-columns: 1fr;
  }
}
</style> 