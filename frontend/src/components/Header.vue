<template>
  <header class="header">
    <div>
      <span class="lab-name">{{title}}</span>
    </div>
    <div>
      <span>
        <a target="_blank" v-bind:href="authorUrl">{{authorName}}</a>
        , {{authorGroup}}
      </span>
      <span>
        Вариант
        <a target="_blank" v-bind:href="varUrl">{{variant}}</a>
      </span>
    </div>
  </header>
</template>

<script>
export default {
  name: "Header",
  props: {
    title: String,
    authorName: String,
    authorUrl: String,
    authorGroup: String,
    variant: String,
    varUrl: String
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Bebas+Neue&family=MuseoModerno&display=swap");

header{
  margin: 0;
  padding: 0;
}

.header {
  height: 25%;
  min-width: 440px;
  display: flex;
  background-color: black;
  justify-content: space-between;
  box-shadow: 0 0 20px 5px darkred;
  font-family: "MuseoModerno", cursive;
}

.header .lab-name{
  color: whitesmoke;
  font-size: 1.5em;
  font-weight: bold;
  margin-top: 10px;
}

.header > div {
  display: flex;
  align-items: center;
  margin: 5px 20px 5px;
  flex-direction: column;
}

.header span, .header a{
  color: white;
  font-size: 0.9em;
  font-weight: bold;
  text-decoration: none;
}

.header a:hover {
  color: orange;
  font-size: 0.95em;
  transition: 0.5s;
  text-shadow: 0 0 20px red;
  text-decoration: none;
}
</style>
