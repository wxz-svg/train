const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // 其他配置项
  devServer: {
    // 配置开发服务器客户端选项
    client: {
      // 是否在浏览器中显示错误覆盖层
      overlay: false
    }
  }
})
