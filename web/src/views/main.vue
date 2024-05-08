<template>
  <!-- 主布局容器，设置ID为components-layout-demo-top-side-2 -->
  <a-layout id="components-layout-demo-top-side-2">
    <!-- 顶部导航视图 -->
    <the-header-view></the-header-view>
    <!-- 主体布局 -->
    <a-layout>
      <!-- 侧边导航视图 -->
      <the-sider-view></the-sider-view>
      <!-- 中部内容布局，设置外边距和内边距以及背景色 -->
      <a-layout style="padding: 0 24px 24px">
        <!-- 面包屑导航，用于展示当前页面的路径 -->
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item>List</a-breadcrumb-item>
          <a-breadcrumb-item>App</a-breadcrumb-item>
        </a-breadcrumb>
        <!-- 页面主要内容区域，设置背景色、内边距和最小高度 -->
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          Content
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script>
import { defineComponent } from 'vue';
// 引入头部组件
import TheHeaderView from "@/components/the-header";
// 引入侧边栏组件
import TheSiderView from "@/components/the-sider";

/**
 * 定义一个名为 main-view 的 Vue 组件
 * @returns 无返回值
 */
export default defineComponent({
  name: "main-view", // 组件名称
  components: {
    // 注册组件 TheSiderView 为 TheSiderView 的别名
    TheSiderView,
    // 注册组件 TheHeaderView 为 TheHeaderView 的别名
    TheHeaderView,
  },
  setup() {
    // 组件的 setup 函数，用于设置组件的初始状态或逻辑
    return {
    };
  },
});
</script>

<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>