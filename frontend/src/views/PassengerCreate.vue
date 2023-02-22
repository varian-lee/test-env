<template>
    <div class="passenger-create">
        <h1> 승객 등록 </h1>
    </div>

    <form id="app" @submit="checkForm">
        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">이름 </label>
            <div class="col-sm-10">
                <input
                    id="name"
                    v-model="name"
                    type="text"
                    name="name"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="nationality" class="col-sm-2 col-form-label">국적 </label>
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
            <label for="birthdate" class="col-sm-2 col-form-label">생일 </label>
            <div class="col-sm-10">
                <input
                    id="birthdate"
                    v-model="birthdate"
                    type="date"
                    name="birthdate"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-11"></div>
            <input class="btn btn-primary col-sm-1" type="submit" value="Submit" name="createPassenger">
        </div>
    </form>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            passengers : []
        }
    },
    methods: {
        checkForm: function (e) {
            let self = this;
	        e.preventDefault()
            if (this.name && this.birthdate && this.nationality) {
                var passengerData = {
                    name: this.name,
                    birthdate: this.birthdate,
                    nationality: this.nationality,
                }
                axios.post("/flask/passenger", passengerData)
                    .then(function(response){
                        console.log(response);
                        self.$router.push('passenger-list')
                    })
                    .catch(function(error){
                        console.log(error);
                    })
                return true;
            } else {
                if (!this.name) {
                    window.alert('Name required.');
                }
                if (!this.birthdate) {
                    window.alert('birthdate required.');
                }
                if (!this.nationality) {
                    window.alert('nationality required.');
                }
            }
        }
  }
}
</script>
