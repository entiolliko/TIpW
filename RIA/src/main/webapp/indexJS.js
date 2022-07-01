window.addEventListener("load", function () {
	if (localStorage.getItem("role") == "Client") {
		window.location.href = "ClientHome.html";
	}else if (localStorage.getItem("role") == "Employee"){
		window.location.href = "EmployeeHome.html";
	}
    var logInButton = document.getElementById("logInButton");
    var registerButton = document.getElementById("registerButton");
    logInButton.addEventListener("click", logInButtonClicked);
    registerButton.addEventListener("click", registerButtonClicked);
});

function logInButtonClicked() {
    var logInUsername = document.getElementById("logInUsername");
    var logInPassword = document.getElementById("logInPassword");
    if (logInUsername === null || logInUsername.value === "" || (logInUsername.value).indexOf(' ') >= 0) {
        (document.getElementById("logInErrorMessage")).textContent = "Please insert a username with no spaces";
        return;
    } if (logInPassword === null || logInPassword.value === "" || (logInPassword.value).indexOf(' ') >= 0 || (logInPassword.value).length < 8) {
        (document.getElementById("logInErrorMessage")).textContent = "Please insert a password with no spaces and at least 8 characters";
        return;
    }
	$.ajax({
		'url': 'CheckLogIn',
		'type': 'POST',
		'data': "username=" + logInUsername.value + "&" + "password=" + logInPassword.value,
		'success': function (data) {
        	var message = data;
        	
        	localStorage.setItem("username", message.username);
        	localStorage.setItem("role", message.role);
        	
            if (message.role == "Client") {
                window.location.href = "ClientHome.html";
            }
            else if (message.role == "Employee") {
                window.location.href = "EmployeeHome.html";
            }
            else {
                document.getElementById("error_message").textContent = "Invalid Role";
            }
 		},
		'error': function (error) {
	         (document.getElementById("logInErrorMessage")).textContent = error.responseText;
		}
	});
}

function registerButtonClicked(e) {
    var username = document.getElementById("registerUsername");
    var password = document.getElementById("registerPassword");
    var repeatPassword = document.getElementById("registerPasswordCheck");
    var userRoleSelect = document.getElementById("userRole")
    var userRole = userRoleSelect.options[userRoleSelect.selectedIndex].text;

    if(username === null || username.value == "" || (username.value).indexOf(' ') >= 0){
        (document.getElementById("regErrorMessage")).textContent = "Please insert a username with no spaces";
        return;
    } if(password === null || password.value == "" || password.value.indexOf(" ") >= 0 || (password.value).length < 8){
        (document.getElementById("regErrorMessage")).textContent = "Please insert a password with no spaces and at least 8 characters";
        return;
    } if(password.value !== repeatPassword.value){
        (document.getElementById("regErrorMessage")).textContent = "The passwords do not match";
        return;
    } if(userRole === null || userRole === ""){
        (document.getElementById("regErrorMessage")).textContent = "You have to select a role";
        return;
    }
    
    $.ajax({
		'url': 'CheckRegistration',
		'type': 'POST',
		'data': "username=" + username.value + "&" + "password=" + password.value + "&" + "passwordCheck=" + repeatPassword.value + "&" + "userRole=" + userRole,
		'success': function (data) {
        	var user = data; 
        	
        	localStorage.setItem("username", user.username);
        	localStorage.setItem("role", user.role);
        	
	        if(user.role == "Client"){
	            window.location.href = "ClientHome.html";
	        }else if(user.role == "Employee"){
	            window.location.href = "EmployeeHome.html";
	        }else{
	            (document.getElementById("regErrorMessage")).textContent = "We couldn't find your role";
	        }
 		},
		'error': function (error) {
	         (document.getElementById("regErrorMessage")).textContent = error.responseText;
		}
	});
}
