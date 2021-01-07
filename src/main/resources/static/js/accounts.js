function getAccountByIban() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/accounts/'+document.forms["search"]["iban"].value;

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

function deleteAccountByIban() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/accounts/'+document.forms["search"]["iban"].value;

    xhr.open("DELETE", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
        alert(xhr.status);
        location.reload();
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        document.getElementById("statusdisplay").innerHTML = "User " + document.forms["search"]["user_id"].value + " succesfully deleted!";
        document.getElementById("datadisplay").innerHTML = "";
        document.getElementById("search").reset();
    }
}

function updateAccount() {

   var xhr =  new XMLHttpRequest();
    xhr.open('PUT','http://nazaragency.nl/accounts/'+document.forms["updateform"]["iban"].value);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        document.getElementById("updateform").reset();
    }
    xhr.send(JSON.stringify(
        {
            "balanceLimit": document.forms["updateform"]["balanceLimit"].value ,
            "transactionAmountLimit": document.forms["updateform"]["transactionAmountLimit"].value ,
            "transactionDayLimit": document.forms["updateform"]["transactionDayLimit"].value
        }
    ));
}


function getAllAccounts() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/accounts';

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

function getAccountByUserId() {

    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/users/'+document.forms["search"]["owner"].value+'/accounts';

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



function postAccount(){
    var xhr =  new XMLHttpRequest();
    var t = document.getElementById("account_type");

    var userId = sessionStorage.getItem("UserId");
    xhr.open('POST','http://nazaragency.nl/users/'+userId+'/accounts');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        location.reload();
    }
    xhr.send(JSON.stringify(
        {
            "account_type": t.options[t.selectedIndex].value,
            "balance": document.forms["createAccountForm"]["balance"].value ,
            "transactionDayLimit": document.forms["createAccountForm"]["transactionDayLimit"].value ,
            "transactionAmountLimit": document.forms["createAccountForm"]["transactionAmountLimit"].value ,
            "balanceLimit": document.forms["createAccountForm"]["balanceLimit"].value
        }
    ));
}
