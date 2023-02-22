import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'

import { ApmVuePlugin } from '@elastic/apm-rum-vue'

const app = createApp(App)
            .use(router)
            // .use(ApmVuePlugin, {
            //   router,
            //   config: {
            //     serviceName: 'frontend',
            //     serverUrl: 'https://7982e33051ef4284b11cba764a863ad3.apm.us-central1.gcp.cloud.es.io:443',
            //     //environment: 'local-docker-compose',
            //     environment: 'production',
            //     serviceVersion: '0.0.1',
            //     //disableInstrumentations: ["page-load", "eventtarget", "click"], 
            //     transactionSampleRate: 1,
            //     propagateTracestate: true
            //   }
            // })
            .mount('#app')
