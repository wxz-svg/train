/**
 * 主入口文件
 * 创建Vue应用，初始化状态管理器、路由和组件库，并将应用挂载到DOM元素上。
 * 同时注册所有全局组件。
 */

// 导入Vue框架及相关配置
import { createApp } from 'vue';
import App from './App.vue'; // 主组件
import router from './router'; // 路由管理器
import store from './store'; // 状态管理器
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css'; // 引入Ant Design Vue的样式文件
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";

// 创建Vue应用实例
const app = createApp(App)

// 使用Ant Design Vue组件库、状态管理和路由
app.use(Antd) // 引入并使用Ant Design Vue组件库
    .use(store) // 注册并使用状态管理器
    .use(router) // 注册并使用路由管理器
    .mount('#app'); // 将应用挂载到页面中id为app的元素上

// 注册全局组件
// 导入并注册Ant Design Vue的图标库
const icons = Icons; // 假设Icons为全局图标集合
for (const i in icons) {
    app.component(i, icons[i]) // 循环注册Icons中的所有图标组件
}

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});

/**
 * 配置后端端口
 */
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER);

