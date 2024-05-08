<template>
  <!-- 侧边导航栏，固定宽度200，背景色白色 -->
  <a-layout-sider width="200" style="background: #fff">
    <!-- 导航菜单，采用内联模式，高度100%，无右侧边框 -->
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <!-- 子菜单项1，包含图标和标题 -->
      <a-sub-menu key="sub1">
        <template #title>
              <span>
                <user-outlined /> <!-- 用户图标 -->
                subnav 11
              </span>
        </template>
        <!-- 子菜单项1的选项 -->
        <a-menu-item key="1">option1</a-menu-item>
        <a-menu-item key="2">option2</a-menu-item>
        <a-menu-item key="3">option3</a-menu-item>
        <a-menu-item key="4">option4</a-menu-item>
      </a-sub-menu>
      <!-- 子菜单项2，包含图标和标题 -->
      <a-sub-menu key="sub2">
        <template #title>
              <span>
                <laptop-outlined /> <!-- 笔记本图标 -->
                subnav 2
              </span>
        </template>
        <!-- 子菜单项2的选项 -->
        <a-menu-item key="5">option5</a-menu-item>
        <a-menu-item key="6">option6</a-menu-item>
        <a-menu-item key="7">option7</a-menu-item>
        <a-menu-item key="8">option8</a-menu-item>
      </a-sub-menu>
      <!-- 子菜单项3，包含图标和标题 -->
      <a-sub-menu key="sub3">
        <template #title>
              <span>
                <notification-outlined /> <!-- 通知图标 -->
                subnav 3
              </span>
        </template>
        <!-- 子菜单项3的选项 -->
        <a-menu-item key="9">option9</a-menu-item>
        <a-menu-item key="10">option10</a-menu-item>
        <a-menu-item key="11">option11</a-menu-item>
        <a-menu-item key="12">option12</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref} from 'vue';
export default defineComponent({
  name: "the-sider-view", // 组件名称
  setup() {
    // 初始化选中键和打开的键
    return {
      selectedKeys2: ref(['1']),
      openKeys: ref(['sub1']),
    };
  },
});
</script>

<!-- 限制CSS作用域仅于此组件 -->
<style scoped>

</style>
