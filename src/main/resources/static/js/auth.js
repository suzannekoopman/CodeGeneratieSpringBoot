
function POSTRegister()
{
    var xhr =  new XMLHttpRequest();
    xhr.open('POST','http://nazaragency.nl/register');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload= (e) => {
        alert(xhr.status);

    }
    xhr.send(JSON.stringify(
        {
            "firstName": document.forms["registerUserForm"]["firstName"].value ,
            "lastName": document.forms["registerUserForm"]["lastName"].value,
            "email": document.forms["registerUserForm"]["email"].value,
            "password": document.forms["registerUserForm"]["passwordRegister"].value
        }
    ));
}

function POSTLogin(){
    var xhr =  new XMLHttpRequest();
    xhr.open('POST','http://nazaragency.nl/login');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload= (e) => {
        alert(xhr.status);
        let response = JSON.parse(xhr.response);
        sessionStorage.setItem("AuthToken", response.authToken);
        sessionStorage.setItem("UserId", response.userId);
        var userId = sessionStorage.getItem("UserId");
        location.reload();
    }
    xhr.send(JSON.stringify(
        {
            "username": document.forms["loginForm"]["username"].value ,
            "password": document.forms["loginForm"]["password"].value
        }
    ));

}

