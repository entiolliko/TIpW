var clicked = false;


window.onstorage = message_receive;

function message_broadcast(message)
{
    localStorage.setItem('message',JSON.stringify(message));
    localStorage.removeItem('message');
}
function message_receive(ev)
{	
    if (ev.key!="message") return;
    var message = JSON.parse(ev.newValue);
    if (!message) return;

    if (message.command == "logout"){ 
		checkLogIn();
    }  
	else if(message.command == "priceUpdated"){
		closePrevInfo();
		readEmployeePreventives();
		readUnManagedPreventives();
	}
}

window.addEventListener("load", function () {
	checkLogIn();
	
	//Set up username header
	let employeeUsernameHeader = document.getElementById("employeeUsernameHeader");
	let text = document.createTextNode(localStorage.getItem("username"));
	employeeUsernameHeader.appendChild(text);

	//Set up log out button 
	document.getElementById("logOutButton").addEventListener("click", logOutButtonClicked);
	
	//Read Employee's Preventives
	readEmployeePreventives();
	
	//Read unamanged Preventives
	readUnManagedPreventives();
	
	//Add Event Handler for the Price Input
	document.getElementById("priceInput").addEventListener("click", function(){
		document.getElementById("priceInput").value = "";
	});
	
	//Add Event Handler to Close Preventive Page Button
	document.getElementById("closeInfoPageButton").addEventListener("click", closePrevInfo);
	
	//Add Event Handler to Insert Price Button
	document.getElementById("insertPriceButton").addEventListener("click", insertPriceButtonClicked);
});

function checkLogIn(){
	if (localStorage.getItem("username") == null || localStorage.getItem("username") == undefined) {
		window.location.href = "index.html";
	} else if (localStorage.getItem("role") == "Client"){
		window.location.href = "ClientHome.html";
	}
}

//Log Out Button Handler
function logOutButtonClicked() {
	$.ajax({
		'url': 'LogOut',
		'type': 'GET',
		'success': function (data) {
			window.location.href = "index.html";
			
			localStorage.clear();
			message_broadcast({'command':'logout'});
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "There has been an error when loging out.";
		}
	});
}

function readEmployeePreventives(){
	document.getElementById("managedPreventivesTableBody").innerHTML = "";		
	
	$.ajax({
		'url': 'GetPreventivesManagedByEmployee',
		'type': 'GET',
		'success': function(data){
			var managedPreventivesTableBody = document.getElementById("managedPreventivesTableBody");
			for(var i = 0; i < data.length; i++){
				var newRow = managedPreventivesTableBody.insertRow();
				
				var newCell = newRow.insertCell();
				var newText = document.createTextNode(data[i].preventiveID);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				newText = document.createTextNode(data[i].productCode);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				newText = document.createTextNode(data[i].productName);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				var newButton = document.createElement("input");
				newButton.type = "button";
				newButton.id = data[i].preventiveID;
				newButton.value = "Get Information";
				newButton.className = "normalButton";
				newCell.appendChild(newButton);
				newButton.addEventListener("click", function (){
					getPreventiveInfo(this.id);
				});
			}
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "It wasn't possible to get the preventives managed by you.\n" + error.responseText;
		}
	});
}

function getPreventiveInfo(preventiveID){
	$.ajax({
		'url': 'GetInformationAboutPreventive',
		'type': 'GET',
		'data': "preventiveID=" + preventiveID,
		'success': function(data){
			addDataToInfo(true, data);
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "It wasn't possible to get the informations about the preventive': " + error.responseText;
		}
	});
}

function addDataToInfo(isInfoPage, data){
	document.getElementById("mainPageDiv").className = "hiddenElement";
	document.getElementById("preventiveInfoDiv").className = "visibleElement";
	document.getElementById("closeInfoPageButton").className = "normalButton";
	
	cleanPrevInfo(isInfoPage);
	
	var newText = document.createTextNode(data.preventiveID);
	document.getElementById("preventiveIDInfo").appendChild(newText);

	newText = document.createTextNode(data.productCode);
	document.getElementById("productCodeInfo").appendChild(newText);
	
	newText = document.createTextNode(data.productName);
	document.getElementById("productNameInfo").appendChild(newText);
	
	if(isInfoPage){
		var img = document.createElement("img");
		img.src = data.imgPath;
		document.getElementById("productImageInfo").appendChild(img);
	}
	
	newText = document.createTextNode(data.clientUsername);
	document.getElementById("clientUsernameInfo").appendChild(newText);
	
	if(data.price === undefined || data.price < 0)
		newText = document.createTextNode("Not Defined");
	else
		newText = document.createTextNode(data.price);
	document.getElementById("preventivePriceInfo").appendChild(newText);
	
	if(data.employeeUsername === undefined || data.employeeUsername === null
	   || data.employeeUsername === "")
		newText = document.createTextNode("No employee");
	else
		newText = document.createTextNode(data.employeeUsername);
	document.getElementById("employeeUsernameInfo").appendChild(newText);
	
	var optionInfoTableBody = document.getElementById("optionInfoTableBody");
	for(var i = 0; i < data.optionsInPreventive.length; i++){
		var newRow = optionInfoTableBody.insertRow();
		
		var newCell = newRow.insertCell();
		var newText = document.createTextNode(data.optionsInPreventive[i].optionCode);
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();
		newText = document.createTextNode(data.optionsInPreventive[i].productCode);
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();
		newText = document.createTextNode(data.optionsInPreventive[i].optionName);
		newCell.appendChild(newText);
		
		newCell = newRow.insertCell();
		newText = document.createTextNode(data.optionsInPreventive[i].optionType);
		newCell.appendChild(newText);
	}
	
	clicked = true;
}

function closePrevInfo(){
	cleanPrevInfo();
	clicked = false;
	document.getElementById("mainPageDiv").className = "";
	document.getElementById("preventiveInfoDiv").className = "hiddenElement";
	document.getElementById("closeInfoPageButton").className = "hiddenElement";
}

function cleanPrevInfo(isInfoPage){
	
	document.getElementById("pageName").innerHTML = (isInfoPage? "Preventive Info" : "Set Price");
	document.getElementById("productImageLabel").className = (isInfoPage? "visibleElement" : "hiddenElement");
	document.getElementById("productImageInfo").className = (isInfoPage? "visibleElement" : "hiddenElement");
	document.getElementById("priceInput").className = (isInfoPage? "hiddenElement" : "visibleElement");
	document.getElementById("insertPriceButton").className = (isInfoPage? "hiddenElement" : "visibleElement, normalButton");

	if(clicked){
		var tempArr = [ "preventiveIDInfo", "productCodeInfo",
						"productNameInfo", "clientUsernameInfo",
						"preventivePriceInfo", "employeeUsernameInfo",]
		
		for(var i = 0; i < tempArr.length; i++){
			var elem = document.getElementById(tempArr[i]);
			elem.removeChild(elem.lastChild);
		}
		
		document.getElementById("productImageInfo").innerHTML = "";
		document.getElementById("optionInfoTableBody").innerHTML = "";
	}
}

function readUnManagedPreventives(){
	document.getElementById("unmanagedPreventivesTableBody").innerHTML = "";		
	
	$.ajax({
		'url': 'GetUnmanagedPreventives',
		'type': 'GET',
		'success': function(data){
			var unmanagedPreventivesTableBody = document.getElementById("unmanagedPreventivesTableBody");
			for(var i = 0; i < data.length; i++){
				var newRow = unmanagedPreventivesTableBody.insertRow();
				
				var newCell = newRow.insertCell();
				var newText = document.createTextNode(data[i].preventiveID);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				newText = document.createTextNode(data[i].productCode);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				newText = document.createTextNode(data[i].productName);
				newCell.appendChild(newText);
				
				newCell = newRow.insertCell();
				var newButton = document.createElement("input");
				newButton.type = "button";
				newButton.id = data[i].preventiveID;
				newButton.value = "Set the price";
				newButton.className = "normalButton";
				newCell.appendChild(newButton);
				newButton.addEventListener("click", function (){
					getPreventiveInfoForPricing(this.id);
				});
			}
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "It wasn't possible to get the unmanaged preventives'\n" + error.responseText;
		}
	});
}

function getPreventiveInfoForPricing(preventiveID){
	$.ajax({
		'url': 'GetUnmanagedPreventiveInfo',
		'type': 'GET',
		'data': "preventiveID=" + preventiveID,
		'success': function(data){
			addDataToInfo(false, data);
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "It wasn't possible to show the page to price the preventive': " + error.responseText;
		}
	});
}

function insertPriceButtonClicked(){
	
	var preventiveID = document.getElementById("preventiveIDInfo").childNodes[1].textContent;
	var insertedPrice = document.getElementById("priceInput").value;
	
	if(isNaN(insertedPrice)){
		document.getElementById("errMessage").innerHTML = "You have to insert a number";
		return;
	}else if(Number(insertedPrice) <= 0){
		document.getElementById("errMessage").innerHTML = "The price must be greater than 0";
		return;
	}
	
	document.getElementById("errMessage").innerHTML = "";
	
	$.ajax({
		'url': 'UpdatePreventivePrice',
		'type': 'POST',
		'data': "preventiveID=" + preventiveID + "&price=" + insertedPrice,
		'success': function(){
			closePrevInfo();
			readEmployeePreventives();
			readUnManagedPreventives();
			message_broadcast({'command':'priceUpdated'});
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "It wasn't possible to update the price of the preventive.\n'" + error.responseText;
		}
	});
}



