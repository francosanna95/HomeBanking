const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            client:[],
            cards:[],
            debitCards:[],
            creditCards:[],
            cardType:"CardType",
            cardColor:"CardColor",
        }
    },
    created() {
            axios.get('/api/clients/current')
            .then((resp)=>{
                this.client =resp.data;
                this.accounts= resp.data.accounts;
                this.accounts.sort((a,b)=> a.id > b.id? 1: -1)
                this.cards= resp.data.cards.sort((a,b) => a.id>b.id? 1: -1);
                this.debitCards= this.cards.filter(card => card.type == "DEBIT");
                this.creditCards= this.cards.filter(card => card.type == "CREDIT");
             })
             },
    methods:{
         formatDate: function(date){
               return new Date(date).toLocaleDateString('en-gb');
               },
         formatCardNumber: function(cardNumberString){
            return cardNumberString.replaceAll("_"," ");
         },
         logOut(){
               axios.post('/api/logout').then(
                 response => console.log('signed out!!!'))
                  location.replace(window.location.href.replace("create-cards.html","index.html",))
         },
         createCard(){
                this.newCard(this.cardType, this.cardColor);
                },
         newCard(cardType,cardColor){
                axios.post('/api/clients/current/cards',`cardType=${cardType}&cardColor=${cardColor}`,
                          {headers:{'content-type':'application/x-www-form-urlencoded'}})
                             .then(response =>swal("Creation Success","you have a new card","success"))
                             .then(response =>location.replace(window.location.href.replace("create-cards.html","cards.html")))
                             .catch(err=>swal("Creation Error","please verify that both fields are selected","error"));
         },
       }

   })
app.mount("#app")