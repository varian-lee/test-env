<template>
  <div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <router-link class="navbar-brand" to="/">Home</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              항공편
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="/flight-list">항공편 목록</router-link>
              <router-link class="dropdown-item" to="/flight-create">항공편 등록</router-link>
            </div>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              승객
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="/passenger-list">승객 목록</router-link>
              <router-link class="dropdown-item" to="/passenger-create">승객 등록</router-link>
            </div>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              공항
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="/airport-list">공항 목록</router-link>
              <router-link class="dropdown-item" to="/airport-create">공항 등록</router-link>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </div>

  <div class="container">
    <router-view></router-view>
  </div>
</template>

<script>

import LoadingSpinner from './components/LoadingSpinner.vue'

export default {
  name: 'app',
  components: {
    LoadingSpinner
  },
  async created() {
      try {
          // var context = {
          //     "id": "my-id",
          //     "username": "my-username",
          //     "email": "my@email.com"
          // }
          // this.$apm.setUserContext(context)
          // this.$apm.addLabels(context)
          
          // var transaction =this.$apm.startTransaction("test-transaction2","custom2");
          // transaction.id = 'asdasdasd';
          // const span = transaction.startSpan('My custom span2')
          // span.end()
          // transaction.end()


      } catch (error) {
          console.log(error);
      }
  }
}

</script>
