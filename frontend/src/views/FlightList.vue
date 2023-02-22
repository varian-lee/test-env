<template>
    <div class="passenger-list">
        <h1> 항공편 목록 </h1>
    </div>
    
    <LoadingSpinner v-if="isLoading"></LoadingSpinner>
    <!-- <button @click="showModal = true" class="button">Show Modal</button>
    <transition name="fade" appear>
        <div class="modal-overlay" 
            v-if="showModal" 
            @click="showModal = false"></div>
    </transition>
    <transition name="pop" appear>
        <div class="modal" 
            role="dialog" 
            v-if="showModal"
            >
        <h1>Vue Transitions</h1>
        <p>The <code>&lt;transition&gt;</code> component in Vue can create wonderful animated entrances and exits.</p>
        <button @click="showModal = false" class="button">Hide Modal</button>

        </div>
    </transition> -->
    <div class="form-group row">
        <div class="col-sm-3">
            <select class="form-select" aria-label="Default select example" v-model="airport_from" @change="onChangeFrom($event)">
                <option v-for="airport in airports" :value="airport">{{airport.full_name}}</option>
            </select>
        </div>
        
        <div class="col-sm-3">
            <select class="form-select" aria-label="Default select example" v-model="airport_to" @change="onChangeTo($event)">
                <option v-for="airport in airports" :value="airport">{{airport.full_name}}</option>
            </select>        
        </div>
        
        <button class="btn btn-success col-sm-2" @click="getFlights" name="getFlights">
            search
        </button>
    </div>

    <table class="table table-striped" v-if="flights.length > 0">
        <thead>
            <tr>
                <th scope="col">출발 시간</th>
                <th scope="col">도착 시간</th>
                <th scope="col">가격</th>
                <th scope="col">티켓 수량</th>
                <th scope="col">Action</th>
                <!-- <th scope="col">Action2</th> -->
            </tr>
        </thead>
        <tbody id="flights">
            <tr v-for="flight in flights">
                <td class="align-middle">{{new Date(1000*flight.startSeconds).toLocaleString()}}</td>
                <td class="align-middle">{{new Date(1000*flight.startSeconds + 1000*3600*flight.hour).toLocaleString()}}</td>
                <td class="align-middle">{{flight.price}}</td>
                <td class="align-middle">{{flight.quantity}}</td>
                <td><button class="btn btn-info" v-on:click="goto(flight)" name="goToFlight">변경</button></td>
                <!-- <td><button @click="showModal = true?false:true" class="button">modal</button></td> -->
            </tr>
        </tbody>
    </table> 

    <div v-if="flights.length <= 0">
        <br>
        <h1> 항공편이 없습니다! </h1>
    </div>

</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            from: "",
            to: "",
            airports : [],
            isLoading: false,
            airport_from: undefined,
            airport_to: undefined,
            flights: [],
            showModal: true
        }
    },
    methods: {
        onChangeFrom(event) {
            console.log(this.airport_from)
        },
        onChangeTo(event) {
            console.log(this.airport_to)
        },
        getFlights: function (e) {
	        e.preventDefault()
            let self = this;
            if (this.airport_from && this.airport_to) {
                //var params = {from : this.airport_from.code, to: this.airport_to.code}
                const res = axios.get('/spring/flight?from='+this.airport_from.code+'&to='+this.airport_to.code)//, params)
                    .then(function(response){
                        self.flights = []
                        for (const res of response.data) {
                            var startSeconds = res.score;
                            var values = res.value.split(" ");
                            var quantity = parseInt(values[0]);
                            var hour = parseInt(values[1]);
                            var price = parseInt(values[2]);
                            var flight = {startSeconds : startSeconds, quantity:quantity, hour:hour, price:price}
                            self.flights.push(flight);
                        }
                        console.log(self.flights);
                    })
                    .catch(function(error){
                        console.log(error);
                    });

            }
        },
        goto: function (flight) {
            this.$router.push("/flight-get?from=" + this.airport_from.code + "&to=" + this.airport_to.code + "&startSeconds=" + flight.startSeconds);
        },
    },
    async created() {
        try {
            this.isLoading = true;
                
            const urlParams = new URLSearchParams(window.location.search);
            this.from = urlParams.get("from");
            this.to = urlParams.get("to");

            var airport_url = '/flask/airport'
            const res = await axios.get(airport_url);
            this.isLoading = false;
            this.airports = res.data;
            if (this.from && this.to) {
                for (var airport of this.airports) {
                    if (airport.code == this.from) {
                        this.airport_from = airport;
                    } 
                    if (airport.code == this.to) {
                        this.airport_to = airport;
                    } 
                }

                let self = this;
                var flight_url = '/spring/flight'
                flight_url = flight_url + "?from=" + this.from + "&to=" + this.to;
                const res2 = await axios.get(flight_url)
                    .then(function(response){
                        self.flights = []
                        for (const res of response.data) {
                            var startSeconds = res.score;
                            var values = res.value.split(" ");
                            var quantity = parseInt(values[0]);
                            var hour = parseInt(values[1]);
                            var price = parseInt(values[2]);
                            var flight = {startSeconds : startSeconds, quantity:quantity, hour:hour, price:price}
                            self.flights.push(flight);
                        }
                        console.log(self.flights);
                    })
                    .catch(function(error){
                        console.log(error);
                    });

            }


        } catch (error) {
            console.log(error);
        }
    }
}
</script>
