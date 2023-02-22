const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    port: 80,
    proxy : {
      '/spring': {
          target: 'http://backend-spring:8080',
          changeOrigin: true,
          pathRewrite: {
              '^/spring': ''
          }
      },
      '/flask': {
          target: 'http://backend-flask:8081',
          changeOrigin: true,
          pathRewrite: {
              '^/flask': ''
          }
      },
    }
  }
})
