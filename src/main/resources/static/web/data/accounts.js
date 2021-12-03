const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            client:[],
            loans:[],
            selectedAccount:"",
            selectedType:"",
        }
    },
    created() {
        this.loadData();
    //    this.loadAccounts();
      },
    methods:{
      loadData(){
        axios.get('/api/clients/current')
        .then((resp)=>{
          this.client =resp.data;
          this.accounts= resp.data.accounts;
          this.accounts.sort((a,b)=> a.id > b.id? 1: -1)
          this.loans= resp.data.loans;
         console.log(this.client);
         console.log(this.accounts);
         console.log(this.loans);
         this.selectedAccount=this.accounts[0].id;
         console.log(this.selectedAccount)
               }).catch(err =>this.logOut());

      },
      logOut(){
      axios.post('/api/logout').then(
        response => console.log('signed out!!!'))
         location.replace(window.location.href.replace("accounts.html","index.html",))
      },
      createAccount(){
        axios.post('/api/clients/current/accounts',`accountType=${this.selectedType}`)
        .then(response =>swal("Creation Success","you have a new account","success"))
        .then(response =>location.reload());
      },
      deleteAcc(accId){
          console.log(accId);
          swal({
            title:"Are you sure?",
                text:"delete can't be undone",
                icon:"warning",
                buttons: {
                     cancel: "No",
                     catch: {
                            text: "Yes",
                            value: "transfer",
                            },
                }})
                .then((value) => {
                     if(value=="transfer"){
                            axios.delete(`/api/clients/current/accounts/${accId}`)
                                .then(response =>location.reload())
                                .catch(err=> swal({
                                                   title:err,
                                                   text:"You can't delete accounts with money, transfer it and try again",
                                                   buttons:{
                                                        cancel:"Ok",
                                                        catch:{ text:"Transfer",
                                                                value:"next",
                                                        }
                                                   }
                                              }))
                                              .then((val)=>{
                                                if(val=="next"){
                                                    location.replace(window.location.href.replace("accounts.html","transfers.html"))
                                                }
                                              })
                     }
                })
        },
    }
})
app.mount("#app")