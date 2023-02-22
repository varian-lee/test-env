<template>
    <div class="passenger-get">
        <h1> 정보 확인 </h1>
    </div>

    <form id="app">
        <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">이름 </label>
            <div class="col-sm-10">
                <input
                    id="name"
                    v-model="passenger.name"
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
                    v-model="passenger.nationality"
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
                    v-model="passenger.birthdate"
                    type="date"
                    name="birthdate"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-sm-9"></div>
            <button class="btn btn-primary col-sm-1" @click="updatePassenger" name="updatePassenger">
                Update
            </button>
            <button class="btn btn-danger col-sm-1" @click="deletePassenger" name="deletePassenger">
                Delete
            </button>
            <button class="btn btn-secondary col-sm-1" @click="cancel" name="cancelPassenger">
                Cancel
            </button>
        </div>
    </form>
</template>

<script>
function leftPad(value) { 
    if (value >= 10) { 
        return value; 
    } 
    return `0${value}`; 
}

function toStringByFormatting(source, delimiter = '-') { 
    const year = source.getFullYear(); 
    const month = leftPad(source.getMonth() + 1); 
    const day = leftPad(source.getDate()); 
    return [year, month, day].join(delimiter); 
}

import axios from "axios";
export default {
    data() {
        return {
            id: 0,
            passenger: {}
        }
    },
    methods: {
        updatePassenger: function (e) {
	        e.preventDefault()
            let self = this;
            const res = axios.put('/flask/passenger/' + this.id, this.passenger)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push('passenger-list');
                    })
                    .catch(function(error){
                        console.log(error);
                    });
        },
        deletePassenger: function (e) {
	        e.preventDefault()
            let self = this;
            if (confirm('삭제합니까?')) {
                const res = axios.delete('/flask/passenger/' + this.id)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push('passenger-list');
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
            this.$router.push( 'passenger-list' );
        },
    },
    async created() {
        try {
            const urlParams = new URLSearchParams(window.location.search);
            this.id = urlParams.get("id");
            const res = await axios.get('/flask/passenger/' + this.id);
            res.data.birthdate = toStringByFormatting(new Date(res.data.birthdate))
            this.passenger = res.data;
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
