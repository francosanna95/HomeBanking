const app = Vue.createApp({
    data() {
        return {
            account:[],
            transactions:[],
            client:[],
            sinceDate:"",
            toDate:""
        }
    },
    created() {
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('id');
        axios.get(`/api/client/currents/accounts/${myParam}`)
        .then((resp)=>{
            this.account =resp.data;
            this.transactions=resp.data.transactions;
            this.transactions.sort((a,b)=> a.id < b.id? 1: -1)
            console.log(myParam);
            console.log(this.account)})
            .catch(err => console.error(err.message));
            this.loadClient();
      },

    methods:{
    loadClient(){
            axios.get('/api/clients/current')
            .then((resp)=>{
              this.client =resp.data;
              console.log(this.client);})
              .catch(err =>this.logOut() );
          },
      formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
            },
      pdfDownload(){
      let filterDTO={"accountNumber":"VIN-001",
                        "sinceDate":2021-11-30,
                        "thruDate":2021-12-03};
        axios({
         method:'POST',
         url:'/api/clients/current/transactionspdf',
          data:{"accountNumber":`${this.account.number}`,"sinceDate":`${this.sinceDate}`,"thruDate":`${this.toDate}`},
         responseType:'blob',
        }).then((resp)=>{
            let fileUrl=window.URL.createObjectURL(new Blob([resp.data]));
            let fileLink=document.createElement('a');

            fileLink.href=fileUrl;
            fileLink.setAttribute('download','file.pdf');
            document.body.appendChild(fileLink);

            fileLink.click();
        }).catch(err=>console.log(err));

      },
      logOut(){
      axios.post('/api/logout')
      .then(response => location.replace(window.location.href.replace("account.html","index.html")))},
    }
})
app.mount("#app")
// axios.post({'/api/clients/current/transactionspdf',`account=VIN-001&fromDate=2021-12-01&toDate=2021-12-03`},
//        }).then((resp)=>{console.log(pasamos)}).catch(err=>console.log(err));