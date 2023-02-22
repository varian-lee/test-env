<template>
    <div class="flight-create">
        <h1> 항공편 등록 </h1>
    </div>

    <!-- <button id="show-modal" @click="showModal = true">Show Modal</button>
    <Teleport to="body">
        <modal :show="showModal" @close="showModal = false">
        <template #header>
            <h3>custom header</h3>
        </template>
        </modal>
    </Teleport> -->

    <form id="app" @submit="checkForm">
        <div class="form-group row">
            <label for="from" class="col-sm-2 col-form-label">출발 공항 </label>
            <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" v-model="flight.from">
                    <option v-for="airport in airports" :value="airport.code">{{airport.full_name}}</option>
                </select>
            </div>
        </div>

        <div class="form-group row">
            <label for="to" class="col-sm-2 col-form-label">도착 공항 </label>
            <div class="col-sm-10">
                <select class="form-select" aria-label="Default select example" v-model="flight.to">
                    <option v-for="airport in airports" :value="airport.code">{{airport.full_name}}</option>
                </select>
            </div>
        </div>

        <div class="form-group row">
            <label for="quantity" class="col-sm-2 col-form-label">티켓 수량 </label>
            <div class="col-sm-10">
                <input
                    id="quantity"
                    v-model="flight.quantity"
                    type="text"
                    name="quantity"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="hour" class="col-sm-2 col-form-label">소요시간 </label>
            <div class="col-sm-10">
                <input
                    id="hour"
                    v-model="flight.hour"
                    type="text"
                    name="hour"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="price" class="col-sm-2 col-form-label">가격 </label>
            <div class="col-sm-10">
                <input
                    id="price"
                    v-model="flight.price"
                    type="text"
                    name="price"
                    class="form-control">
            </div>
        </div>

        <div class="form-group row">
            <label for="depature_date" class="col-sm-2 col-form-label">출발일 </label>
            <div class="col-sm-10">
                <input
                    id="depature_date"
                    v-model="flight.depature_date"
                    type="date"
                    name="depature_date"
                    class="form-control">
            </div>
        </div>


        <div class="form-group row">
            <div class="col-sm-11"></div>
            <input class="btn btn-primary col-sm-1" type="submit" value="Submit" name="createFlight">
        </div>
    </form>
</template>

<script>
import axios from "axios";
import Modal from '../components/AirportModal.vue'
export default {
    components:{
        Modal
    },
    data() {
        return {
            airports: [],
            flight : {
                from: undefined,
                to: undefined,
                quantity: undefined,
                hour: undefined,
                price: undefined,
                startSeconds: undefined,
                depature_date: undefined,
                validate: function() {
                    return (this.from && this.to && this.quantity && this.hour && this.price && this.depature_date);
                }
            }
        }
    },
    methods: {
        checkForm: function (e) {
            let self = this;
	        e.preventDefault()
            if (this.flight.validate()) {
                depature_date = this.flight.depature_date.split("-");
                var depature_date = new Date( depature_date[0], depature_date[1] - 1, depature_date[2]);
                this.flight.startSeconds = depature_date.getTime()/1000;

                axios.post("/spring/flight", this.flight)
                    .then(function(response){
                        console.log(response);
                        self.$router.push("/flight-list?from=" + self.flight.from + "&to=" + self.flight.to);
                    })
                    .catch(function(error){
                        console.log(error);
                    })
                return true;
            } else {
                window.alert('Param required.');
            }
        }
    },
    async created() {
        try {
            const res = await axios.get('/flask/airport');
            this.airports = res.data;
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
