function getAllTransactions() {
    var xhr =  new XMLHttpRequest();

    var url = 'http://nazaragency.nl/transactions';

    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
    }
    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSON(obj)
        document.getElementById("statusdisplay").innerHTML = "";
        // document.getElementById("search").reset();
    }
}

function getAllTransactionsForUser(){
    var xhr =  new XMLHttpRequest();
    var user_id = sessionStorage.getItem("UserId");
    var url = 'http://nazaragency.nl/users/'+user_id+'/transactions';
    //alert(url);
    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
    }
    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSON(obj)
        document.getElementById("statusdisplay").innerHTML = "";
        //document.getElementById("search").reset();
    }
}

function getTransactionById() {
    var xhr =  new XMLHttpRequest();
    var url = 'http://nazaragency.nl/users/'+document.forms["search"]["userId"].value+'/transactions';
    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));

    xhr.onload= (e) => {
    }

    xhr.send();

    xhr.onreadystatechange=(e)=>{
        var obj = JSON.parse(xhr.responseText);
        CreateTableFromJSON(obj);
        //document.getElementById("search").reset();
    }
}

function getAllTransactionsWithPagination(command, url) {
    //alert("getAllTransactionsWithPagination1");
    var offset = Number(document.forms["search"]["offset"].value);
    var limit = Number(document.forms["search"]["limit"].value);
    var xhr =  new XMLHttpRequest();

    var account_url =document.forms["search"]["url"].value;

    if(account_url!=''){
        url = account_url+'&offset='+offset+'&limit='+limit;
    }
    else if(url==""){
        url = 'http://nazaragency.nl/transactions?offset='+offset+'&limit='+limit;
    }else{
        url = url+'?offset='+offset+'&limit='+limit;
    }
    //alert(url);
    xhr.open("GET", url);
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
    }
    xhr.send();
    var obj ;
    xhr.onreadystatechange=(e)=>{
        obj = JSON.parse(xhr.responseText);
        CreateTableFromJSON(obj)
        document.getElementById("statusdisplay").innerHTML = "";
        var length= obj.length;
        if(length<limit && command==2){
            var new_offset = offset-limit;
            if(new_offset<0){new_offset=0;}
            document.forms["search"]["offset"].value = new_offset;
            //alert("end");

        }
    }
}
function searchTransactionByCustomer(){

    var user_id = sessionStorage.getItem("UserId");
    document.forms["search"]["offset"].value = 0;
    document.forms["search"]["userId"].value=user_id;
    document.forms["search"]["url"].value= '';
    getTransactionNext();
}

function getTransactionNextByCustomer(){
    var user_id = sessionStorage.getItem("UserId");
    document.forms["search"]["userId"].value=user_id;
    getTransactionNext();
}

function getTransactionPrevByCustomer(){
    var user_id = sessionStorage.getItem("UserId");
    document.forms["search"]["userId"].value=user_id;
    getTransactionPrev();

}

function searchTransactionById(){
    document.forms["search"]["offset"].value = 0;
    document.forms["search"]["url"].value= '';
    getTransactionNext();
}

function searchTransactionByIBAN(){
    document.forms["search"]["offset"].value = 0;
    var iban = document.forms["search"]["iban"].value;
    if(iban== ''){
        alert("Please enter iban")
    }else{
        var url = 'http://nazaragency.nl/transactions?iban='+iban;
        document.forms["search"]["url"].value= url;
        getTransactionNext();
    }
}


function getTransactionPrev(){
    //alert("getTransactionPrev");
    var user_id = document.forms["search"]["userId"].value;
    var url = 'http://nazaragency.nl/transactions';
    if(user_id != ''){
        url = 'http://nazaragency.nl/users/'+document.forms["search"]["userId"].value+'/transactions';
    }
    //alert(url);
    var offset = document.forms["search"]["offset"].value;
    var limit = document.forms["search"]["limit"].value;
    if(offset < 0){
        alert("End"); //no prev data
    }else{
        getAllTransactionsWithPagination(1,url);
        var new_offset = offset-limit;
        document.forms["search"]["offset"].value = new_offset;
        document.forms["search"]["limit"].value = limit;
    }

}

function getTransactionNext(){
    var user_id = document.forms["search"]["userId"].value;
    var url = 'http://nazaragency.nl/transactions';
    if(user_id!=""){
        url = 'http://nazaragency.nl/users/'+document.forms["search"]["userId"].value+'/transactions';
    }
    var offset = Number(document.forms["search"]["offset"].value);
    var limit = Number(document.forms["search"]["limit"].value);
    getAllTransactionsWithPagination(2,url);
    var new_offset = offset+limit;
    document.forms["search"]["offset"].value = new_offset;
    document.forms["search"]["limit"].value = limit;

}

function createNewTransaction() {

    var xhr =  new XMLHttpRequest();
    xhr.open('POST','http://nazaragency.nl/transactions');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        location.reload();
    }
    xhr.send(JSON.stringify(
        {
            "account_from": document.forms["createNewTransaction"]["account_from"].value,
            "account_to": document.forms["createNewTransaction"]["account_to"].value,
            "amount": document.forms["createNewTransaction"]["amount"].value
        }
    ));

}

function PostMachineTransfer()
{
    var amount = document.getElementById("amount").value;
    var t = document.getElementById("transferType");
    var transferType = t.options[t.selectedIndex].value;

    var xhr =  new XMLHttpRequest();

    xhr.open('POST','http://nazaragency.nl/users/'+sessionStorage.getItem("UserId")+ '/machine');
    xhr.setRequestHeader("Content-Type", "application/json" );
    xhr.setRequestHeader("ApiKeyAuth" ,sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
    //haal transactie op
        alert(xhr.status);
    }
    xhr.send(JSON.stringify(
        {
            "amount": amount,
            "transfer_type": transferType
        }
    ));
}
