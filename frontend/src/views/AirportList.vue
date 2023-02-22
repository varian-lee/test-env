<template>
    <div class="airport-list">
        <h1> 공항 목록 </h1>
    </div>
    
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>
    
    <table class="table table-striped">
        <thead>
            <tr>
                <th scope="col">공항코드</th>
                <th scope="col">공항명</th>
                <th scope="col">국가</th>
                <th scope="col">좌표</th>
                <th scope="col">Action</th>
            </tr>
        </thead>
        <tbody id="airports">
            <tr v-for="airport in airports" :key="airport.id">
                <td class="align-middle">{{airport.code}}</td>
                <td class="align-middle">{{airport.full_name}}</td>
                <td class="align-middle">{{airport.nationality}}</td>
                <td class="align-middle">{{airport.lat}}, {{airport.lon}}</td>
                <td><button class="btn btn-info" v-on:click="goto(airport.code)" name="goToAirport">변경</button></td>
            </tr>
        </tbody>
    </table>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            airports : [],
            isLoading: false,
        }
    },
    methods: {
        goto: function (code) {
            this.$router.push("/airport-get?code=" + code);
        }
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
