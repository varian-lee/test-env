<template>
    <div class="home">
        <h1> Sample Webpage - HOME </h1>
        <!-- {{span}} -->
        <button class="btn btn-info" v-on:click="make_error()" name="make_error">JS error</button>
        <button class="btn btn-danger" v-on:click="make_400()" name="make_400">400에러</button>
        <button class="btn btn-primary" v-on:click="health_flask()" name="health_flask">Flask Service Health</button>
        <button class="btn btn-success" v-on:click="health_spring()" name="health_spring">Spring Service Health</button>
        <button class="btn btn-danger" v-on:click="make_spring_npe()" name="make_spring_npe">Make NPE</button>
    </div>

</template>

<script>
import axios from "axios";

const delay = ms => new Promise(res => setTimeout(res, ms));

export default {
    data() {
        return {
        }
    },
    methods: {
        make_error: function () {
            var test = undefined;
            var letsmake_error = test.not_exsisting_variable[0];
        },
        make_400: async function (e) {
            console.log("슬립 시작!");
            await delay(3000);
            console.log("슬립 끝!");

            // 특정 Query String 없으면 400 반환
            const res = axios.get('/spring/flight')
                .then(function(response){
                    window.alert(response.statusText);
                })
                .catch(function(error){
                    if (error.response) {
                        //console.log(error.response.data);
                        window.alert(error.response.status);
                        //console.log(error.response.headers);
                    } else if (error.request) {
                        console.log(error.request);
                    } else {
                        console.log('Error', error.message);
                    }
            });
        },
        health_flask: function (e) {
            const res = axios.get('/flask/health')
                .then(function(response){
                    alert("Flask backend service is healthy!");
                })
                .catch(function(error){
                    if (error.response) {
                        //console.log(error.response.data);
                        window.alert(error.response.status);
                        //console.log(error.response.headers);
                    } else if (error.request) {
                        console.log(error.request);
                    } else {
                        console.log('Error', error.message);
                    }
            });
        },
        health_spring: function (e) {
            const res = axios.get('/spring/health')
                .then(function(response){
                    alert("Spring backend service is healthy!");
                })
                .catch(function(error){
                    if (error.response) {
                        //console.log(error.response.data);
                        window.alert(error.response.status);
                        //console.log(error.response.headers);
                    } else if (error.request) {
                        console.log(error.request);
                    } else {
                        console.log('Error', error.message);
                    }
            });
        },
        make_spring_npe: function (e) {
            const res = axios.get('/spring/health/error')
                .then(function(response){
                    window.alert(response.statusText);
                })
                .catch(function(error){
                    if (error.response) {
                        console.log(error.response.status);
                        //window.alert(error.response.status);
                        //console.log(error.response.headers);
                    } else if (error.request) {
                        console.log(error.request);
                    } else {
                        console.log('Error', error.message);
                    }
            });
        },

    },
    async created() {
        try {
            this.isLoading = true;
            const res = await axios.get('/flask/airport');
            this.isLoading = false;
            this.airports = res.data;
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
