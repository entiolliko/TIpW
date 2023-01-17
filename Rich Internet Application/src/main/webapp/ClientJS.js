var optionList = [];
var clicked = false;
let productCToSimpleProductMap;

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
	else if(message.command == "preventiveCreated") 
		readClientPreventives();
}

window.addEventListener("load", function () {
	//Checks whether the user is loged or not
	checkLogIn();
	
	//Set up username header
	let userNameHeader = this.document.getElementById("clientUsernameHeader");
	let text = document.createTextNode(this.localStorage.getItem("username"));
	userNameHeader.appendChild(text);

	//Set up log out button 
	let logOutButton = this.document.getElementById("logOutButton");
	logOutButton.addEventListener("click", logOutButtonClicked);

	//Read product list for the option menu and add event
	updateProductList();
	let products = this.document.getElementById("productCodeSelect");
	products.addEventListener("change", renderOptionsForSelectedProduct);
	
	//Add clicked option to the selected table
	let options = this.document.getElementById("optionsForProduct");
	options.addEventListener("click", addSelectedOption);
	
	//Create Preventive button
	var createPreventiveButton = document.getElementById("createPreventiveButton");
	createPreventiveButton.addEventListener("click", createPreventive);
	
	//Read Client's Preventives'
	readClientPreventives();
	
	//Add Event Handler to Close Preventive Page Button
	var closeInfoPageButton = document.getElementById("closeInfoPageButton");
	closeInfoPageButton.addEventListener("click", closePrevInfo);
});

function checkLogIn(){
	if (localStorage.getItem("username") == null || localStorage.getItem("username") == undefined) {
		window.location.href = "index.html";
	} else if (localStorage.getItem("role") == "Employee"){
		window.location.href = "EmployeeHome.html";
	}
}

//Log Out Button Handler
function logOutButtonClicked() {
	$.ajax({
		'url': 'LogOut',
		'type': 'GET',
		'success': function () {
			window.location.href = "index.html";
			localStorage.clear();
			message_broadcast({'command':'logout'});
		},
		'error': function () {
			(document.getElementById("errMessage")).textContent = "There has been an error when loging out.";
		}
	});
}

//Legge la lista dei prodotti
function updateProductList() {
	$.ajax({
		'url': 'GetAllSimpleProducts',
		'type': 'GET',
		'success': function (data) {
			productCToSimpleProductMap = new Map(Object.entries(data));
			for (let x in data) {
				let child = document.createElement("option");
				child.id = data[x].productCode;
				child.innerHTML = data[x].productCode + " : " + data[x].productName;
				child.onclick = renderOptionsForSelectedProduct;
				(document.getElementById("productCodeSelect")).appendChild(child);
			}
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = error.responseText;
		}
	});
}

function renderOptionsForSelectedProduct() {
	
	var productCode = document.getElementById("productCodeSelect").options[document.getElementById("productCodeSelect").selectedIndex].id;;
	let data = productCToSimpleProductMap.get(productCode).options;
	
	let options = document.getElementById("optionsForProduct");
	options.innerHTML = '';
	
	let selectedOptions = document.getElementById("chosenOptionsForProduct");
	selectedOptions.innerHTML = '';
	optionList = [];

	let child = document.createElement("option");
	child.selected = true;
	child.disabled = true;
	child.innerHTML = " -- select option -- ";
	options.appendChild(child);

	for (let i = 0; i < data.length; i++) {
		let child = document.createElement("option");
		child.id = data[i];
		child.innerHTML = data[i];
		options.appendChild(child);
	}
}

function addSelectedOption(){
	//Prende l'opzione selezionata
	var options = document.getElementById("optionsForProduct");
	if(options.options[options.selectedIndex] == undefined)
		return;
	var optionID = options.options[options.selectedIndex].text;
 	options.removeChild(document.getElementById(optionID));
 	
 	//Aggiunge l'opzione alla tabella'
 	var chosenOptionsTableBody = document.getElementById("chosenOptionsForProduct");
	var newRow = chosenOptionsTableBody.insertRow();
	newRow.id = optionID;
	var newCell = newRow.insertCell();
	var newText = document.createTextNode(optionID);
	newCell.appendChild(newText);
	newRow.addEventListener("click", function(){
		//Quando clichiamo sull'opzione, essa viene rimessa nella lista originale
		let availableOptions = document.getElementById("optionsForProduct");
		let child = document.createElement("option");
		child.id = ($(event.target)).text();
		child.innerHTML = ($(event.target)).text();;
		availableOptions.appendChild(child);
		
		this.parentNode.removeChild(this);
	});
}

function createPreventive(){
	var chosenOptionsTable = document.getElementById("chosenOptionsTable");
	if(chosenOptionsTable.rows.length <= 1){
		var errMessage = document.getElementById("errMessage");
		errMessage.innerHTML = "You have to chose at least 1 option";
		return;
	}
	var errMessage = document.getElementById("errMessage");
	errMessage.innerHTML = "";
	
	for (var i = 1; i <  chosenOptionsTable.rows.length; i++) {
    	optionList.push(chosenOptionsTable.rows[i].cells[0].innerHTML);
    }
    
    var serializedOptionList = JSON.stringify(optionList);
    var productCode = document.getElementById("productCodeSelect").options[document.getElementById("productCodeSelect").selectedIndex].id;
    $.ajax({
		'url': 'AddPreventiveToDB',
		'type': 'POST',
		'data': "productCode=" + productCode + "&options=" + serializedOptionList,
		'success': function(){
			message_broadcast({'command':'preventiveCreated'});
			readClientPreventives();
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = error.responseText;
		}
	});
	optionList = [];
}

function readClientPreventives(){
	var clientPreventivesTableBody = document.getElementById("clientPreventivesTableBody");
	clientPreventivesTableBody.innerHTML = "";		
	
	$.ajax({
		'url': 'GetPreventivesCreatedByUser',
		'type': 'GET',
		'success': function(data){
			var clientPreventivesTableBody = document.getElementById("clientPreventivesTableBody");
			for(var i = 0; i < data.length; i++){
				var newRow = clientPreventivesTableBody.insertRow();
				
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
					(document.getElementById("mainPageDiv")).className="hiddenElement";
					getClientPreventiveInfo(this.id);
				});
			}
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = error.responseText;
		}
	});
}

function getClientPreventiveInfo(preventiveID){
	$.ajax({
		'url': 'GetInformationAboutPreventive',
		'type': 'GET',
		'data': "preventiveID=" + preventiveID,
		'success': function(data){
			addDataToInfo(data);
		},
		'error': function (error) {
			(document.getElementById("errMessage")).textContent = "C'è stato un error: " + error.responseText;
		}
	});
}

function addDataToInfo(data){
	document.getElementById("preventiveInfoDiv").className = "visibleElement";
	document.getElementById("closeInfoPageButton").className = "normalButton";
	
	cleanPrevInfo();
	
	var preventiveIDInfo = document.getElementById("preventiveIDInfo");
	var newText = document.createTextNode(data.preventiveID);
	preventiveIDInfo.appendChild(newText);

	var productCodeInfo = document.getElementById("productCodeInfo");
	var newText = document.createTextNode(data.productCode);
	productCodeInfo.appendChild(newText);
	
	var productNameInfo = document.getElementById("productNameInfo");
	var newText = document.createTextNode(data.productName);
	productNameInfo.appendChild(newText);
	
	var productImageInfo = document.getElementById("productImageInfo");
	var img = document.createElement("img");
	img.src = data.imgPath;
	productImageInfo.appendChild(img);
	
	var clientUsernameInfo = document.getElementById("clientUsernameInfo");
	var newText = document.createTextNode(data.clientUsername);
	clientUsernameInfo.appendChild(newText);
	
	var preventivePriceInfo = document.getElementById("preventivePriceInfo");
	var newText = document.createTextNode(data.price);
	preventivePriceInfo.appendChild(newText);
	
	var employeeUsernameInfo = document.getElementById("employeeUsernameInfo");
	if(data.employeeUsername === undefined)
		var newText = document.createTextNode("No employee");
	else
		var newText = document.createTextNode(data.employeeUsername);
	employeeUsernameInfo.appendChild(newText);
	
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
	document.getElementById("closeInfoPageButton").className = "hiddenElement";
	document.getElementById("preventiveInfoDiv").className = "hiddenElement";
	document.getElementById("mainPageDiv").className = "divselect";
}

function cleanPrevInfo(){
	if(clicked){
		var tempArr = [ "preventiveIDInfo",
						"productCodeInfo",
						"productNameInfo",
						"clientUsernameInfo",
						"preventivePriceInfo",
						"employeeUsernameInfo",]
		
		for(var i = 0; i < tempArr.length; i++){
			var elem = document.getElementById(tempArr[i]);
			elem.removeChild(elem.lastChild);
		}
		
		var productImageInfo = document.getElementById("productImageInfo");
		productImageInfo.innerHTML = "";
		var optionInfoTableBody = document.getElementById("optionInfoTableBody");
		optionInfoTableBody.innerHTML = "";
	}
}

