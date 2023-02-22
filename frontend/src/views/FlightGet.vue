<template>
    <div class="flight-get">
        <h1> 정보 확인 </h1>
    </div>

    <form id="app">
        <div class="form-group row">
            <label for="from" class="col-sm-2 col-form-label">출발 공항 코드</label>
            <div class="col-sm-10">
                <input
                    id="from"
                    v-model="flight.from"
                    type="text"
                    name="from"
                    class="form-control"
                    readonly="readonly" >
            </div>
        </div>

        <div class="form-group row">
            <label for="to" class="col-sm-2 col-form-label">도착 공항 코드</label>
            <div class="col-sm-10">
                <input
                    id="to"
                    v-model="flight.to"
                    type="text"
                    name="to"
                    class="form-control"
                    readonly="readonly" >
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
            <label for="hour" class="col-sm-2 col-form-label">소요 시간 </label>
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
            <div class="col-sm-9"></div>
            <button class="btn btn-primary col-sm-1" @click="updateFlight" name="updateFlight">
                Update
            </button>
            <button class="btn btn-danger col-sm-1" @click="deleteFlight" name="deleteFlight">
                Delete
            </button>
            <button class="btn btn-secondary col-sm-1" @click="cancel" name="cancel">
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
            from: "",
            to: "",
            startSeconds: 0,
            flight: {}
        }
    },
    methods: {
        updateFlight: function (e) {
	        e.preventDefault()
            let self = this;
            const res = axios.put('/spring/flight', this.flight)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push('flight-list');
                    })
                    .catch(function(error){
                        console.log(error);
                    });
        },
        deleteFlight: function (e) {
	        e.preventDefault()
            let self = this;
            if (confirm('삭제합니까?')) {
                const res = axios.delete("/spring/flight/?from=" + this.flight.from + "&to=" + this.flight.to + "&startSeconds=" + this.flight.startSeconds)
                    .then(function(response){
                        window.alert(response.statusText);
                        self.$router.push( "flight-list?from=" + this.flight.from + "&to=" + this.flight.to);
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
            this.$router.push( "flight-list?from=" + this.flight.from + "&to=" + this.flight.to);
        },
    },
    async created() {
        try {
            const urlParams = new URLSearchParams(window.location.search);
            this.from = urlParams.get("from");
            this.to = urlParams.get("to");
            this.startSeconds = urlParams.get("startSeconds");
            const res = await axios.get("/spring/flight?from=" + this.from + "&to=" + this.to + "&startSeconds=" + this.startSeconds);
            var values = res.data[0].value.split(" ");
            var quantity = parseInt(values[0]);
            var hour = parseInt(values[1]);
            var price = parseInt(values[2]);
            this.flight = {
                from: this.from,
                to: this.to,
                quantity: quantity,
                hour: hour,
                price: price,
                startSeconds: this.startSeconds,
            }
            console.log(this.flight);
        } catch (error) {
            console.log(error);
        }
    }
}
</script>
