<template>
  <!-- 布局头部，包含logo和水平导航菜单 -->
  <a-layout-header class="header">
    <div class="logo" />
    <!-- 水平导航菜单，采用深色主题，模式为水平，行高设置为64px -->
    <div class="logo" />
    <div style="float: right; color: white">
      您好: {{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">导航项1</a-menu-item>
      <a-menu-item key="2">导航项2</a-menu-item>
      <a-menu-item key="3">导航项3</a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent, ref} from 'vue';
import store from "@/store";
/**
 * TheHeaderView 组件定义。
 * 此组件表示一个带有导航菜单的头部布局。
 *
 * @returns 返回组件实例。
 */
export default defineComponent({
  name: "the-header-view",
  setup() {
    let member = store.state.member;
    // 初始化并返回菜单选中项的键值。
    // 组件加载时，默认选中键值为 '2'。
    return {
      selectedKeys1: ref(['2']),
      member
    };
  },
});
</script>

<!-- 添加scoped属性限制此样式仅应用于当前组件。 -->
<style scoped>

</style>
