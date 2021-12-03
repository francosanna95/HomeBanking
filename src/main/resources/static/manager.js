const app = Vue.createApp({
    data() {
        return {
            clients:[],
            nuevo:{
            firstName:"",
            lastName:"",
            mail:""}
        }
    },
    created() {
        this.loadData();
      },
    methods:{
      loadData(){
        axios.get('/rest/clients')
        .then((resp)=>{
          this.clients =resp.data._embedded.clients;
          console.log(this.clients)
        })
      },
      addClient(){
      if(this.nuevo.firstName !=`` && this.nuevo.lastName !=`` && this.nuevo.mail !=``)
        this.postClient();
      else{alert("los 3 campos deben estar llenos, intentelo nuevamente")}},
    postClient(){
      axios.post("/rest/clients",this.nuevo)
                        .then((resp)=> {
                        this.loadData();
                        this.nuevo.firstName="";
                        this.nuevo.lastName="";
                        this.nuevo.mail="";})
                        .catch((error)=>{console.log(error)});

    }
    }
})
app.mount("#app")