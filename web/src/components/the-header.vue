<template>
  <!-- 布局头部，包含logo和水平导航菜单 -->
  <a-layout-header class="header">
    <div class="logo" />
    <!-- 水平导航菜单，采用深色主题，模式为水平，行高设置为64px -->
    <div class="logo" />
    <div style="float: right; color: white">
      您好: {{member.mobile}} &nbsp;&nbsp;&nbsp;
      <router-link to="/login" style="color: white">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <!--欢迎-->
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <CoffeeOutlined /> &nbsp;欢迎
        </router-link>
      </a-menu-item>

      <!--乘车人管理-->
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <UserOutlined /> &nbsp;乘车人管理
        </router-link>
      </a-menu-item>

    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import store from "@/store";
import router from "@/router";
/**
 * TheHeaderView 组件定义。
 * 此组件表示一个带有导航菜单的头部布局。
 *
 * @returns 返回组件实例。
 */
export default defineComponent({
  name: "the-header-view",
  setup() {
    // 从store中获取成员信息
    let member = store.state.member;

    // 初始化一个ref来保存选中的路径（路由）
    const selectedKeys = ref([]);

    // 监听当前路由路径的变化，并相应地更新selectedKeys
    watch(
        () => router.currentRoute.value.path, // 监视当前路由路径
        (newValue) => {
          console.log('watch', newValue); // 打印新的路由路径以供调试
          selectedKeys.value = []; // 重置selectedKeys数组
          selectedKeys.value.push(newValue); // 将新的路由路径添加到selectedKeys数组
        },
        { immediate: true } // 立即执行监视器，使用当前路由值
    );

    return {
      member,
      selectedKeys
    };
  },
});
</script>

<!-- 添加scoped属性限制此样式仅应用于当前组件。 -->
<style scoped>

</style>
