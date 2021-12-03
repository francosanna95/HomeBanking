const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            client:[],
            cards:[],
            debitCards:[],
            creditCards:[],
             selectedCardCredit:"1",
            selectedCardDebit:"1",
            deletedCard:0
        }
    },
    created() {
            axios.get('/api/clients/current')
            .then((resp)=>{
                this.client =resp.data;
                this.accounts= resp.data.accounts;
                this.accounts.sort((a,b)=> a.id > b.id? 1: -1)
                this.cards= resp.data.cards.sort((a,b) => a.id>b.id? 1: -1);
                this.cardDate(this.cards);
                this.debitCards= this.cards.filter(card => card.type == "DEBIT");
                this.creditCards= this.cards.filter(card => card.type == "CREDIT");
                if(this.creditCards.length>=1){this.selectedCardCredit= this.creditCards[0].id}
                if(this.debitCards.length>=1){this.selectedCardDebit= this.debitCards[0].id}
             })
             },
    methods:{
         formatDate: function(date){
               return new Date(date).toLocaleDateString('en-gb');
               },
         formatCardNumber: function(cardNumberString){
            return cardNumberString.replaceAll("_"," ").replaceAll("-"," ");
         },
         logOut(){
               axios.post('/api/logout').then(
                 response => console.log('signed out!!!'))
                  location.replace(window.location.href.replace("cards.html","index.html",))
         },
         goToCreate(){
            location.replace(window.location.href.replace("cards.html","create-cards.html"))},
        deleteCard(cardID){
            console.log(cardID);
            swal({title:"Are you sure?",
                             text:"delete can't be undone",
                             icon:"warning",
                                buttons: {
                                        cancel: "No",
                                        catch: {
                                           text: "yes",
                                           value: "transfer",
                                           },
                                        }})
                                           .then((value) => {
                                              if(value=="transfer"){
                                                 axios.delete(`/api/clients/current/cards/${cardID}`);
                                              }
                                           }).then(response=> window.location.reload())
        },
        cardDate(cards){
            cards.forEach(card=>
             {let date=card.thruDate.split();
                       let month=date[0].slice(0,2);
                       let year="20"+date[0].slice(3,5);
                       let properDate=new Date(parseInt(year),parseInt(month),0);
                       let actual=new Date();
                       card.expired="no";
                       if(properDate<actual)
                          {card.expired="yes";}
            })
        },
       }
   })
app.mount("#app")