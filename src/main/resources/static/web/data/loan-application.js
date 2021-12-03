const app = Vue.createApp({
    data() {
        return {
            accounts:[],
            client:[],
            loans:[],
            loanType:"Loans",
            paymentOptions:[],
            selectedPayment:1,
            loanAmount:0,
            destinyAccount:"",
            selectedLoan:[],
            total:0,
            partialPay:0,
        }
    },
    created() {
            axios.get('/api/clients/current')
            .then((resp)=>{
                this.client =resp.data;
                this.accounts= resp.data.accounts;
                this.accounts.sort((a,b)=> a.id > b.id? 1: -1);
             });
             axios.get('/api/loans')
             .then(resp=>{
                this.loans=resp.data;
                console.log(this.loans);
             })
             },
    methods:{
         formatDate: function(date){
               return new Date(date).toLocaleDateString('en-gb');
               },
         chargePayOption(){
                console.log(this.loanType)
            this.selectedLoan=this.loans.filter(loan=> loan.name==this.loanType);
                console.log(this.selectedLoan);
                console.log(this.selectedLoan[0].id)
            this.loans.forEach(loan=> {if(loan.name==this.loanType){this.paymentOptions=loan.payment}});
                console.log(this.paymentOptions);
         },
         logOut(){
               axios.post('/api/logout').then(
                 response => console.log('signed out!!!'))
                 location.replace(window.location.href.replace("loan-application.html","index.html",))
         },
         totalPayment(){
            this.total=this.loanAmount+this.loanAmount*20/100;
            this.partialPay=(this.total/this.selectedPayment).toFixed(2);
         },
         loanApply(){
                swal({title:"Are you sure?",
                 text:"this transaction can't be undone",
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
                                     this.newLoan(this.selectedLoan[0].id, this.loanAmount,this.selectedPayment,this.destinyAccount);
                                  }
                               })
                               .catch(err=>swal({title:"Something go wrong",
                                                text:err.response.data,
                                                icon:"error"}))
                },
         newLoan(){
                axios.post('/api/loans',{ id:this.selectedLoan[0].id, amount:this.loanAmount, payments:this.selectedPayment, number:this.destinyAccount})
                             .then(response =>swal("Creation Success","you have a new loan","success"))
                             .then(response=> window.location.reload())
                             .catch(err=>swal({title:"Something go wrong",
                                                text:err.response.data,
                                                icon:"error"}));
         },
       },
       computed:{
        quotescalc(){
            this.partialPay=(this.total/this.selectedPayment).toFixed(2);
            }

       },

   })
app.mount("#app")