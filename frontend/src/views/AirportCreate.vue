<template>
    <div class="airport-create">
        <h1> 공항 등록 </h1>
    </div>

    <form id="app" @submit="checkForm">
        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">공항코드 </label>
            <div class="col-sm-10">
                <input
                    id="code"
                    v-model="code"
                    type="text"
                    name="code"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">공항명 </label>
            <div class="col-sm-10">
                <input
                    id="full_name"
                    v-model="full_name"
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
                    v-model="nationality"
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
                    v-model="lat"
                    type="number"
                    step="any"
                    name="lat"
                    class="form-control">
            </div>
        </div>
        <div class="form-group row">
            <label for="birthdate" class="col-sm-2 col-form-label">경도 </label>
            <div class="col-sm-10">
                <input
                    id="lon"
                    v-model="lon"
                    type="number"
                    step="any"
                    name="lon"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-11"></div>
            <input class="btn btn-primary col-sm-1" type="submit" value="Submit"  name="createAirport">
        </div>
    </form>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            airports : []
        }
    },
    methods: {
        checkForm: function (e) {
            let self = this;
	        e.preventDefault()
            if (this.code && this.full_name && this.nationality && this.lat && this.lon) {
                var airportData = {
                    code: this.code,
                    full_name: this.full_name,
                    nationality: this.nationality,
                    lat: this.lat,
                    lon: this.lon,
                }
                axios.post("/flask/airport", airportData)
                    .then(function(response){
                        console.log(response);
                        self.$router.push('airport-list')
                    })
                    .catch(function(error){
                        console.log(error);
                    })
                return true;
            } else {
                if (!this.code) {
                    window.alert('Code required.');
                }
                if (!this.full_name) {
                    window.alert('Airport name required.');
                }
                if (!this.nationality) {
                    window.alert('nationality required.');
                }
                if (!this.lat) {
                    window.alert('Latitude required.');
                }
                if (!this.lon) {
                    window.alert('Longitude required.');
                }
            }
        }
  }
}
</script>
