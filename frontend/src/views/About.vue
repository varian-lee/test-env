<template>
    <div class="about">
        <h1> About !!! </h1>
    <p>{{ msg }}</p>
    </div>
</template>

<script>
export default {
  name: 'Ping',
  data() {
    return {
      msg: 'Hello!',
    };
  },
};
</script>