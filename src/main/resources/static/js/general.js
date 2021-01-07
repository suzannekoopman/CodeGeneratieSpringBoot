
function DELETELogout()
{
  var xhr =  new XMLHttpRequest();
    xhr.open('DELETE','http://nazaragency.nl/logout');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("ApiKeyAuth", sessionStorage.getItem("AuthToken"));
    xhr.onload= (e) => {
        alert(xhr.status);
        sessionStorage.setItem("AuthToken", "");
        sessionStorage.setItem("UserId", "");
        location.reload();
    }
    xhr.send();
}

function DisplayCurrentUser() {
     var userId = sessionStorage.getItem("UserId");
           var loginbtn = document.getElementById("login");
           var logoutbtn = document.getElementById("logout");

           if (userId === null || userId === "") {
               document.getElementById("currentuser").innerHTML = "Not logged in";
               logoutbtn.style.display='none';
           }
           else {
               GETCurrentUserById(userId);
               loginbtn.style.display='none';
           }
}

function CreateTableFromJSON(obj) {
    var col = [];
    for (var i = 0; i < obj.length; i++) {
        for (var key in obj[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }

    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < obj.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = obj[i][col[j]];
        }
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById("datadisplay");

    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

function CreateTableFromJSONUserPage(obj) {
    var col = [];
    for (var i = 0; i < obj.length; i++) {
        for (var key in obj[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }

    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < obj.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = obj[i][col[j]];
        }
    }

    for (var i = 1; i < table.rows.length; i++) {
        for (var j = 0; j < 1; j++)
        table.rows[i].cells[j].onclick = function () {
            toUpdateScreen(this);
        };
    }

    function toUpdateScreen(tableCell) {
    var idArr = tableCell.outerHTML.split(">");
    var idArr2 = idArr[1].split("<");
    var id = idArr2[0];
    sessionStorage.setItem("UpdateId", id);
    if (id.length > 10) {
        window.location.href = "http://nazaragency.nl/updateAccount.html";
    }
    else {
        window.location.href = "http://nazaragency.nl/updateUser.html";
    }
    var divContainer = document.getElementById("datadisplay");
    divContainer.innerHTML = sessionStorage.getItem("UpdateId");
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById("datadisplay");

    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}



window.onload = function() {
   var userId = sessionStorage.getItem("UserId");
   var loginbtn = document.getElementById("login");
   var logoutbtn = document.getElementById("logout");



   if (userId === null || userId === "") {
       document.getElementById("currentuser").innerHTML = "Not logged in";
       logoutbtn.style.display='none';
   }
   else {
       GETCurrentUserById(userId);
       loginbtn.style.display='none';
   }
}