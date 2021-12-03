const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            client:[],
            cards:[],
            destinyOptions:[],
            originAccount:"",
            destinyAccount:"",
            transactionAmount: 0,
            description:"",
            other:"To yourself",
            allAccounts:[],
            allClients:[],
            destClient:[],
            sameOwner:"",
        }
    },
    created() {
        swal("Who do you want to transfer?", {
            buttons: {
                cancel: "My Own",
                catch: {
                text: "To another user",
                value: "other",
            },
        }}).then((value) => {
            if(value=="other"){
                this.other= "To others";
                axios.get(`/api/accounts`)
                            .then((resp)=>{
                                this.allAccounts=resp.data;
                            })
            }
        });
        axios.get('/api/clients/current')
            .then((resp)=>{
                this.client =resp.data;
                this.accounts= resp.data.accounts;
                this.accounts.sort((a,b)=> a.id > b.id? 1: -1)
                this.cards= resp.data.cards.sort((a,b) => a.id>b.id? 1: -1);
             });

             },
    methods:{
         logOut(){
               axios.post('/api/logout').then(
                 response => console.log('signed out!!!'))
                  location.replace(window.location.href.replace("transfers.html","index.html",))
         },
         destinyLoad(originAccount){
            this.destinyOptions= this.accounts.filter(account => account.number != originAccount);
         },
         otherToggle(){this.other="To yourself"},
         otherToggle2(){this.other="To others"},
         inputControl(){if(this.transactionAmount==e){this.transactionAmount=0}},
         createTransaction(){
            if(this.other==`yes`){
                if(this.destClient.length<1){swal({title:"Something go wrong",text:"please, select a valid destination",icon:"error"})}
              swal(`Are you sure to send money to ${this.destClient[0].firstName} ${this.destClient[0].lastName}?`, {
                  buttons: {
                      cancel: "No",
                      catch: {
                      text: "yes",
                      value: "transfer",
                      },
                  }})
                      .then((value) => {
                          if(value=="transfer"){
                            this.newTransaction(this.transactionAmount, this.description,this.originAccount,this.destinyAccount);
                          }
                          })
                }
            else{
                swal(`Are you sure to send money to ${this.destinyAccount}?`, {
                     buttons: {
                              cancel: "No",
                              catch: {
                                  text: "yes",
                                  value: "transfer",
                                  },
                                  }})
                                  .then((value) => {
                                       if(value=="transfer"){
                                           this.newTransaction(this.transactionAmount, this.description,this.originAccount,this.destinyAccount);
                                           }
                                  })}
                },
         newTransaction(amount,description,originAccount,destinyAccount){
                axios.post('/api/clients/current/transaction',`amount=${this.transactionAmount}&description=${this.description}&originAccount=${this.originAccount}&destinyAccount=${this.destinyAccount}`,
                          {headers:{'content-type':'application/x-www-form-urlencoded'}})
                            .then(response =>swal("Transaction Success","check it in your transactions","success"))
                            .then(response =>location.replace(window.location.href.replace("transfers.html","accounts.html")))
                             .catch(err=>swal({title:"Creation Error"
                                               ,text:err.response.data,
                                               icon:"error"}));
         },
         search(destinyAccount){
            axios.get('/api/clients')
            .then((resp)=>{
                this.allClients =resp.data;
                this.allClients.forEach(client=> {if(client.accounts.find(account=>account.number==destinyAccount)!=undefined){
                    if(this.destClient.length !=1){this.destClient.push(client)}}
                    else{this.destClient=[]}})
                console.log(this.destClient);
                if(this.accounts.find(account=>account.number==destinyAccount)!=undefined){this.sameOwner="YES"}
                    else{this.sameOwner="NO"}
                    }).catch(err =>console.log(err));
        },
       },
    computed:{
        selectedAccount() {
                let selectedAccount = this.accounts.filter(account => account.number==this.originAccount);
                return selectedAccount;
            },
    },
   })
app.mount("#app")