window.addEventListener("load", function () {
    var logInButton = document.getElementById("logInButton");
    var registerButton = document.getElementById("registerButton");

    logInButton.addEventListener("click", logInButtonClicked);
    registerButton.addEventListener("click", registerButtonClicked);
}
);


function logInButtonClicked(e) {
    var logInUsername = document.getElementById("logInUsername");
    var logInPassword = document.getElementById("logInPassword");

    if (logInUsername === null || logInUsername.value === "") {
        (document.getElementById("logInErrorMessage")).value = "Please insert a username";
        return;
    }
    if ((logInUsername.value).indexOf(' ') >= 0) { //Se si aggiunge un ultimo spazio questo if scata, forse da cambiare?
        (document.getElementById("logInErrorMessage")).textContent = "The username must not have spaces";
        return;
    }
    if (logInPassword === null || logInPassword.value === "") {
        (document.getElementById("logInErrorMessage")).value = "Please insert a password";
        return;
    }
    if ((logInPassword.value).indexOf(' ') >= 0) {
        (document.getElementById("logInErrorMessage")).textContent = "The password must not have spaces";
        return;
    }

    var req = new XMLHttpRequest();
    req.onreadystatechange = function () {
        if (req.readyState == XMLHttpRequest.DONE) {
            switch (req.status) {
                case 200:
                    var message = JSON.parse(req.responseText);
                    sessionStorage.setItem("username", message.username);
                    sessionStorage.setItem("role", message.role);
                    if (message.role == "Client") {
                        window.location.href = "ClientHome.html";
                    }
                    else if (message.role == "Employee") {
                        window.location.href = "EmployeeHome.html";
                    }
                    else {
                        document.getElementById("error_message").textContent = "Invalid Role"
                    }
                    break;

                default: // bad request
                    var message = req.responseText;
                    (document.getElementById("logInErrorMessage")).textContent = message;
                    break;
            }
        }
    };
    req.open("POST", "CheckLogIn");
    var params = "username=" + logInUsername.value + "&" + "password=" + logInPassword.value;
    req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    req.send(params);
}


function registerButtonClicked(e) {
    var username = document.getElementById("registerUsername");
    var password = document.getElementById("registerPassword");
    var repeatPassword = document.getElementById("registerPasswordCheck");
    var userRoleSelect = document.getElementById("userRole")
    var userRole = userRoleSelect.options[userRoleSelect.selectedIndex].text;

    if(username === null || username.value == ""){
        (document.getElementById("regErrorMessage")).textContent = "Please insert a username";
        return;
    }else if(username.value.indexOf(" ") >= 0){
        (document.getElementById("regErrorMessage")).textContent = "The username must not contain spaces";
        return;
    }else if(password === null || password.value == ""){
        (document.getElementById("regErrorMessage")).textContent = "Please insert a password";
        return;
    }else if(password.value.indexOf(" ") >= 0){
        (document.getElementById("regErrorMessage")).textContent = "The password must not contain spaces";
        return;
    }else if(password.value !== repeatPassword.value){
        (document.getElementById("regErrorMessage")).textContent = "The passwords do not match";
        return;
    }else if(userRole === null || userRole === ""){
        (document.getElementById("regErrorMessage")).textContent = "You have to select your role";
        return;
    } 
    var req = new XMLHttpRequest();
    req.onreadystatechange = function (){
        if(req.readyState == XMLHttpRequest.DONE){
            switch(req.status){
                case 200:
                    var user = JSON.parse(req.responseText);
                    
                    sessionStorage.setItem("username", user.username);
                    sessionStorage.setItem("role", user.role);
                    
                    if(userRole == "Client"){
                        window.location.href = "ClientHome.html";
                    }else if(userRole == "Employee"){
                        window.location.href = "EmployeeHome.html";
                    }else{
                        (document.getElementById("regErrorMessage")).textContent = "We couldn't find your role";
                    }
                    break;

               	default:
               		(document.getElementById("regErrorMessage")).textContent = req.responseText;
                    break;
            }
        }        
    }
    req.open("POST", "CheckRegistration");
    var params = "username=" + username.value + "&" + "password=" + password.value + "&" + "passwordCheck=" + repeatPassword.value
    	+ "&" + "userRole=" + userRole;
    req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    req.send(params);
}
