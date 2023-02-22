<template>
    <div class="airport-get">
        <h1> 정보 확인 </h1>
    </div>

    <form id="app">
        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">공항코드 </label>
            <div class="col-sm-10">
                <input
                    id="code"
                    v-model="airport.code"
                    type="text"
                    name="code"
                    class="form-control"
                    readonly="readonly" >
            </div>
        </div>

        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">공항명 </label>
            <div class="col-sm-10">
                <input
                    id="full_name"
                    v-model="airport.full_name"
                    type="text"
                    name="full_name"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="nationality" class="col-sm-2 col-form-label">국가 </label>
            <div class="col-sm-10">
                <input
                    id="nationality"
                    v-model="airport.nationality"
                    type="text"
                    name="nationality"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="birthdate" class="col-sm-2 col-form-label">위도 </label>
            <div class="col-sm-10">
                <input
                    id="lat"
                    v-model="airport.lat"
                    type="number"
                    name="lat"
                    class="form-control">
            </div>
        </div>
        <div class="form-group row">
            <label for="birthdate" class="col-sm-2 col-form-label">경도 </label>
            <div class="col-sm-10">
                <input
                    id="lon"
                    v-model="airport.lon"
                    type="number"
                    name="lon"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-9"></div>
            <button class="btn btn-primary col-sm-1" @click="updateAirport" name="updateAirport">
                Update
            </button>
            <button class="btn btn-danger col-sm-1" @click="deleteAirport" name="deleteAirport">
                Delete
            </button>
            <button class="btn btn-secondary col-sm-1" @click="cancel" name="cancelAirport">
                Cancel
            </button>
        </div>
    </form>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            id: 0,
            airport: {}
        }
    },
    methods: {
        updateAirport: function (e) {
	        e.preventDefault()
            let self = this;
            const res = axios.put('/flask/airport/' + this.code, this.airport)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push('airport-list');
                    })
                    .catch(function(error){
                        console.log(error);
                    });
        },
        deleteAirport: function (e) {
	        e.preventDefault()
            let self = this;
            if (confirm('삭제합니까?')) {
                const res = axios.delete('/flask/airport/' + this.code)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push('airport-list');
                    })
                    .catch(function(error){
                        console.log(error);
                    });
            } else {
                // Do nothing!
                console.log('Nothing to do.');
            }
        },
        cancel: function (e) {
	        e.preventDefault()
            this.$router.push( 'airport-list' );
        },
    },
    async created() {
        try {
            const urlParams = new URLSearchParams(window.location.search);
            this.code = urlParams.get("code");
            const res = await axios.get('/flask/airport/' + this.code);
            this.airport = res.data[0];
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
