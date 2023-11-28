<template>
  <div id="app">
    <Header v-bind="headerParams"/>

    <Loader id="load" v-if="loading"/>
    <div id="contentContainer" v-else>
      <router-view/>
      <notifications group="error" position="bottom right" max="5" />
      <notifications group="success" position="bottom right" max="5" />
    </div>

    <Footer v-bind="footerParams"/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Loader from "@/components/Loader.vue";
import Footer from "@/components/Footer.vue";

import logoUrl from '/public/assets/logo.jpeg'


export default {
  name: 'App',
  components: {
    Footer,
    Header,
    Loader
  },
  data(){
    return {
      headerParams: {
        title: "Лабораторная работа №4",
        authorName: "Барсуков Максим",
        authorUrl: "https://github.com/maxbarsukov",
        authorGroup: 'P3215',
        variant: '14',
        varUrl: "https://github.com/maxbarsukov-itmo/web-4"
      },
      footerParams: {
        authorLogoPath: logoUrl,
        authorUrl: "https://github.com/maxbarsukov",
      },
      loading: true
    }
  },
  mounted(){
    setTimeout(() => {this.loading = false}, 1000)
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

body {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: whitesmoke;
}

#contentContainer {
  margin-top: 20px;
  margin-bottom: 120px;
}

#load {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-right: -50%;
  transform: translate(-50%, -50%);
  color: grey;
}

</style>
