<template>
    <div class="passenger-list">
        <h1> 승객 목록 </h1>
    </div>
    
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>

    <table class="table table-striped">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">이름</th>
                <th scope="col">국적</th>
                <th scope="col">생년월일</th>
                <th scope="col">Action</th>
            </tr>
        </thead>
        <tbody id="passengers">
            <tr v-for="passenger in passengers" :key="passenger.id">
                <td class="align-middle">{{passenger.id}}</td>
                <td class="align-middle">{{passenger.name}}</td>
                <td class="align-middle">{{passenger.nationality}}</td>
                <td class="align-middle">{{new Date(passenger.birthdate).toLocaleDateString()}}</td>
                <td><button class="btn btn-info" v-on:click="goto(passenger.id)" name="goToPassenger">변경</button></td>
            </tr>
        </tbody>
    </table>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            passengers : [],
            isLoading: false,
        }
    },
    methods: {
        goto: function (id) {
            this.$router.push("/passenger-get?id=" + id);
        }
    },
    async created() {
        try {
            this.isLoading = true;
            const res = await axios.get('/flask/passenger');
            this.isLoading = false;
            this.passengers = res.data;
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
