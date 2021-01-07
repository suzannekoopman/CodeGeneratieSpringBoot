function GETAllUsers() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users';

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
    }
    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSONUserPage(obj)
        document.getElementById("statusdisplay").innerHTML = "";
        document.getElementById("search").reset();
    }
}

function GETUserById() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users/'+document.forms["search"]["user_id"].value;

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
        alert(xhr.status);
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse("[" + xhr.responseText + "]");
        CreateTableFromJSONUserPage(obj);
        document.getElementById("search").reset();
    }
}

function GETCurrentUserById(userId) {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users/'+userId;

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);

        document.getElementById("currentuser").innerHTML = "Hello " + obj.firstName + "!";
    }
}

function GETUserByEmail() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users?email='+document.forms["search"]["email"].value;

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
        alert(xhr.status);
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSONUserPage(obj);
        document.getElementById("search").reset();
    }
}

function GETUserByName() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users?name='+document.forms["search"]["lastName"].value;

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
        alert(xhr.status);
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSONUserPage(obj);
        document.getElementById("search").reset();
    }
}

function DELETEUserById() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users/'+document.forms["search"]["user_id"].value;

    xhr.open("DELETE", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
        alert(xhr.status);
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        document.getElementById("statusdisplay").innerHTML = "User " + document.forms["search"]["user_id"].value + " succesfully deleted!";
        document.getElementById("datadisplay").innerHTML = "";
        document.getElementById("search").reset();
    }
}

function POSTUser() {

   var xhr =  new XMLHttpRequest();
    xhr.open('POST','http://nazaragency.nl/users');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        location.reload();
    }
    xhr.send(JSON.stringify(
        {
            "firstName": document.forms["createUserform"]["firstName"].value ,
            "lastName": document.forms["createUserform"]["lastName"].value ,
            "email": document.forms["createUserform"]["email"].value ,
            "password": document.forms["createUserform"]["password"].value,
            "user_type": document.forms["createUserform"]["user_type"].value
        }
    ));

}

function PUTUser() {

   var userid = document.getElementById("uid").value;

   var xhr =  new XMLHttpRequest();
    xhr.open('PUT','http://nazaragency.nl/users/'+userid);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        document.getElementById("updateform").reset();
    }
    xhr.send(JSON.stringify(
        {
            "firstName": document.forms["updateform"]["firstName"].value ,
            "lastName": document.forms["updateform"]["lastName"].value ,
            "email": document.forms["updateform"]["email"].value ,
            "password": document.forms["updateform"]["password"].value
        }
    ));

}

function GetUserRequests()
{
  let table = document.getElementById("userRequestsTable");
        table.innerHTML = "   <tr>\n" +
            "       <th>Request id</th>\n" +
            "       <th>Firstname</th>\n" +
            "       <th>Lastname</th>\n" +
            "       <th>Email</th>\n" +
            "       <th>Accept/Decline</th>\n" +
            "   </tr>";


        var xhr =  new XMLHttpRequest();
        xhr.open('GET','http://nazaragency.nl/users/requests');
        xhr.setRequestHeader("ApiKeyAuth" ,sessionStorage.getItem("AuthToken"));
        xhr.onload= (e) => {
            alert(xhr.status);
            let response = JSON.parse(xhr.response);
            for (i=0;i<response.length;i++){
                table.innerHTML+="<tr><td>%j</td><td>%k</td><td>%l</td><td>%m</td><td>%n</td></tr>"
                    .replace("%j",JSON.stringify(response[i].registerId))
                    .replace("%k",JSON.stringify(response[i].firstName))
                    .replace("%l",JSON.stringify(response[i].lastName))
                    .replace("%m",JSON.stringify(response[i].email))
                    .replace("%n","<button onclick='acceptRequest("+ response[i].registerId+ ", "+ JSON.stringify(response[i].firstName) +","+ JSON.stringify(response[i].lastName)+ "," +JSON.stringify(response[i].password) + ","+JSON.stringify(response[i].email)+ ")'>Accept</button><button onclick='declineRequest("+ response[i].registerId+ ")'>Decline</button>")

            }
        }
        xhr.send();
}
function acceptRequest(requestId, firstName, lastName, password, email)
{

        var xhr =  new XMLHttpRequest();
        xhr.open('POST','http://nazaragency.nl/users');
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
        xhr.onload= (e) => {
            alert(xhr.status);
            declineRequest(requestId);
        }

        xhr.send(JSON.stringify(
            {
                "firstName": firstName,
                "lastName": lastName,
                "email":  email,
                "password": password,
                "user_type": "customer"
            }
        ));
}

function declineRequest(requestId)
{
    var xhr =  new XMLHttpRequest();
        xhr.open('DELETE','http://nazaragency.nl/users/requests/' + requestId);
        xhr.setRequestHeader("ApiKeyAuth" ,sessionStorage.getItem("AuthToken"));
        xhr.onload= (e) => {
            alert(xhr.status);
            location.reload();
        }
        xhr.send();;
}
