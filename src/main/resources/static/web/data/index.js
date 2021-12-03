const app = Vue.createApp({
    data() {
        return {
            password:"",
            mail:"",
            newMail:"",
            newFirstName:"",
            newLastName:"",
            newPassword:"",

        }
    },
    created() {
      },
    methods:{
    logIn(mail,password){
        axios.post("/api/login",`mail=${mail}&password=${password}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response =>
            swal("Login success","have a good day","success")).then( response=>
            location.replace(window.location.href.replace("index.html", "accounts.html")))
            .catch(err=>swal("Login failed", "check your email and password and try again", "error"));
        },
    register(newFirstName,newLastName,newMail,newPassword){
    axios.post("/api/clients",`firstName=${newFirstName}&lastName=${newLastName}&mail=${newMail}&password=${newPassword}`,
        {headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => this.logIn(newMail,newPassword)).catch(swal("Sign Up Failed","Verify that all fields are completed","error"))}
    },
})
app.mount("#app")