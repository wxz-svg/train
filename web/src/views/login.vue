<template>
  <!-- 登录页面布局 -->
  <a-row class="login">
    <!-- 中心对齐的登录表单 -->
    <a-col :span="8" :offset="8" class="login-main">
      <!-- 页面标题，包含应用名称和图标 -->
      <h1 style="text-align: center"><rocket-two-tone />&nbsp;仿12306售票系统</h1>
      <!-- 登录表单，包含手机号、验证码输入和登录按钮 -->
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
          @finish="onFinish"
          @finishFailed="onFinishFailed"
      >
        <!-- 手机号输入框 -->
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号!' }]"
        >
          <a-input v-model:value="loginForm.mobile" placeholder="手机号"/>
        </a-form-item>

        <!-- 验证码输入框，附带获取验证码按钮 -->
        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
        >
          <a-input v-model:value="loginForm.code">
            <template #addonAfter>
              <!-- 获取验证码按钮，点击发送验证码 -->
              <a @click="sendCode">获取验证码</a>
            </template>
          </a-input>
        </a-form-item>

        <!-- 登录按钮 -->
        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>

      </a-form>
    </a-col>
  </a-row>
</template>

template
<script>
import { defineComponent, reactive } from 'vue';
import axios from "axios";
import { notification } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import store from "@/store";


// 定义一个登录组件
export default defineComponent({
  name: "login-view", // 组件名称
  setup() {
    const router = useRouter();
    // 使用reactive创建一个响应式对象，用于存储登录表单的数据
    const loginForm = reactive({
      mobile: '13000000000', // 默认手机号
      code: '', // 默认验证码
    });

    /**
     * 提交表单时的处理函数
     * @param {Object} values 提交的表单值，包含mobile和code
     * 当表单提交成功时，打印成功信息及表单值
     */
    const onFinish = values => {
      console.log('Success:', values);
    };

    /**
     * 表单提交失败的处理函数
     * @param {Object} errorInfo 提交失败的信息
     * 当表单提交失败时，打印失败信息
     */
    const onFinishFailed = errorInfo => {
      console.log('Failed:', errorInfo);
    };

    /**
     * 发送验证码的处理函数
     */
    const sendCode = () => {
      axios.post("/member/member/send-code",{
        mobile: loginForm.mobile
      }).then(response => {
        console.log(response);
        let data = response.data;
        if (data.success) {
          notification.success(({description: '验证码已发送成功!'}));
          loginForm.code = "8888";
        }else {
          notification.error({description: data.message});
        }
      })
    };

    /**
     * 登录的处理函数
     */
    const login = () => {
      axios.post("/member/member/login",loginForm).then(response => {
        let data = response.data;
        if (data.success) {
          notification.success({description: '登陆成功!'});
          // console.log('登陆成功', data.content);
          // 登录成功，跳转的控台页面
          router.push('/');
          // store保存登录信息
          store.commit("setMember", data.content);
        }else {
          notification.error({ description: data.message });
        }
      })
    }

    // 返回loginForm, sendCode,onFinish和onFinishFailed供模板使用
    return {
      loginForm,
      onFinish,
      onFinishFailed,
      sendCode,
      login
    };
  },
});
</script>

<style>
.login-main h1 {
  font-size: 25px;
  font-weight: bold;
}
.login-main {
  margin-top: 100px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>