<template>
  <!-- 侧边导航栏，固定宽度200，背景色白色 -->
  <a-layout-sider width="200" style="background: #fff">
    <!-- 导航菜单，采用内联模式，高度100%，无右侧边框 -->
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <!-- 首页导航项，链接到"/welcome"页面 -->
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <CoffeeOutlined /> &nbsp;欢迎
        </router-link>
      </a-menu-item>

      <!-- 乘车人管理导航项，链接到"/passenger"页面 -->
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <UserOutlined /> &nbsp;乘车人管理
        </router-link>
      </a-menu-item>

    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";
export default defineComponent({
  name: "the-sider-view", // 组件名称
  setup() {
    const selectedKeys = ref([]);


    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    // 初始化选中键和打开的键
    return {
      selectedKeys
    };
  },
});
</script>

<!-- 限制CSS作用域仅于此组件 -->
<style scoped>

</style>
