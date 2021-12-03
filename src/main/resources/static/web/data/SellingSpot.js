const app = Vue.createApp({
    data() {
        return {
            cardNumber:"",
            cvv:"",
            quantity:1,
            productPrice:0,
            email:"",
            productName:"",
        }
    },
    methods:{
         buy(){
                axios.post('/api/clients/current/cardTransaction',
                { number:this.cardNumber, cvv:this.cvv, amount:this.productPrice*this.quantity, description:this.productName,mail:this.email})
                             .then(response =>swal("Success","you have a product","success"))
                             .then(response=> window.location.reload());
         },
       },
   })
app.mount("#app")