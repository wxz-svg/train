import { createStore } from 'vuex'

const MEMBER = "MEMBER"; // 定义存储成员信息的键名

/**
 * 创建一个 Vuex store 实例
 * @return {Object} 返回一个 Vuex store 实例
 */
export default createStore({
  state: {
    // 初始化成员信息，从 SessionStorage 中获取，如果不存在则为空对象
    member: window.SessionStorage.get(MEMBER) || {}
  },
  getters: {
    // 此处定义 getters，用于从 state 中派生出可被响应式访问的属性
  },
  mutations: {
    /**
     * 更新成员信息
     * @param {Object} state Vuex 的 state 对象
     * @param {Object} _member 新的成员信息对象
     */
    setMember(state, _member) {
      state.member = _member; // 更新 state 中的成员信息
      window.SessionStorage.set(MEMBER, _member); // 将更新后的成员信息存储到 SessionStorage 中
    }
  },
  actions: {
    // 此处定义 actions，用于触发 mutations 中的方法进行状态更新，通常与异步操作相关
  },
  modules: {
    // 此处可定义子模块，用于实现模块化管理的复杂应用状态
  }
})