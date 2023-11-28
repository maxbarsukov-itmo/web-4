<template>
  <div id="content">
    <h1>Вход в систему</h1>
    <hr>
    <form id="form" @sumbit.prevent="logIn">
      <div id="email">
        <p v-if="!isValidEmail" class="error">
          <b>Email невалидный</b>
        </p>
        <p v-else class="error"><b>&nbsp;</b></p>

        <label for="emailInput">Введите e-mail:</label>
        <input type="email"
               id="emailInput"
               required
               placeholder="Введите ваш email"
               v-model.trim="email"/>
      </div>
      <div id="password">
        <label for="passwordInput">Введите пароль:</label>
        <input type="password"
               id="passwordInput"
               required
               placeholder="Пароль"
               v-model.trim="password"/>
      </div>
      <div id="buttons">
        <Button :disabled='!isValidEmail' color="blue" :style="[{ 'color': isValidEmail ?'black' : '#ddd' }]" id="loginButton" label="Войти в систему" @click.native="logIn"/>
        <Button :disabled='!isValidEmail' color="white" :style="[{ 'color': isValidEmail ? 'black' : '#ddd' }]" label="Зарегистрироваться" @click.native="register"/>
      </div>
    </form>
  </div>
</template>

<script>
import Button from '@/components/Button.vue';
import { loginApi, registerApi } from "@/services/soapApi";

export default {
  name: "Registration",
  components: {
    Button,
  },
  data(){
    return{
      email: "",
      password: ""
    }
  },
  computed: {
    isValidEmail() {
      return /^[^@]+@\w+(\.\w+)+\w$/.test(this.email);
    }
  },
  watch: {
    email(val) {
      const emailInput = document.getElementById("emailInput");
      emailInput.style.color = this.checkValidEmail(val) ? '' : '#ff0018';
    }
  },
  methods: {
    checkValidEmail(email) {
      return /^[^@]+@\w+(\.\w+)+\w$/.test(email);
    },
    logIn(e){
      e.preventDefault()
      this.$router.push({name: 'app-page'});

      loginApi(
        { email: this.email, password: this.password },
        (data) => {
          localStorage.setItem("jwt", data.token);
          this.$notify({
            group: 'success',
            title: 'Вход в аккаунт',
            text: data.status,
            type: 'success'
          });
          this.$router.push({name: 'app-page'});
        }, (error) => {
          this.errorHandler(error.status);
          console.error(error);
        }
      );
    },
    register(e){
      e.preventDefault();

      registerApi(
        { email: this.email, password: this.password },
        (data) => {
          this.$notify({
            group: 'success',
            title: 'Регистрация',
            text: data.status,
            type: 'success'
          });
        }, (error) => {
          this.errorHandler(error.status);
          console.error(error);
        }
      );
    },
    errorHandler(errorText){
      this.$notify({
        group: 'error',
        title: 'Error',
        text: errorText,
        type: 'error'
      })
    }
  }
}
</script>

<style scoped>
.error {
  text-align: center;
  font-size: 14px;
  color: #ff0018;
}

#form button {
  margin: 20px 10px 10px 10px;
}

#email, #password {
  margin: 5px;
}


#form {
  position: relative;
  font-size: 20px;
  flex-direction: column;
  margin: 30px auto auto;
  background-color: ghostwhite;
  padding: 20px;
  display: table;
  text-align: center;
  box-shadow: 0 0 10px 1px black;
}

input {
  margin-left: 13px;
  padding: 5px 0 5px 2px;
}
</style>
