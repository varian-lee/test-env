import {createRouter, createWebHistory} from 'vue-router'

import Home from '@/views/Home.vue'
import About from '@/views/About.vue'
import PassengerList from '@/views/PassengerList.vue'
import PassengerGet from '@/views/PassengerGet.vue'
import PassengerCreate from '@/views/PassengerCreate.vue'
import FlightList from '@/views/FlightList.vue'
import FlightGet from '@/views/FlightGet.vue'
import FlightCreate from '@/views/FlightCreate.vue'
import AirportList from '@/views/AirportList.vue'
import AirportGet from '@/views/AirportGet.vue'
import AirportCreate from '@/views/AirportCreate.vue'

const routes = [
    {path: '/', name:'Home', component:Home},
    {path: '/about', name:'About', component:About},
    {path: '/passenger-list', name:'PassengerList', component:PassengerList},
    {path: '/passenger-get', name:'PassengerGet', component:PassengerGet},
    {path: '/passenger-create', name:'PassengerCreate', component:PassengerCreate},
    {path: '/airport-list', name:'AirportList', component:AirportList},
    {path: '/airport-get', name:'AirportGet', component:AirportGet},
    {path: '/airport-create', name:'AirportCreate', component:AirportCreate},
    {path: '/flight-list', name:'FlightList', component:FlightList},
    {path: '/flight-get', name:'FlightGet', component:FlightGet},
    {path: '/flight-create', name:'FlightCreate', component:FlightCreate}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router