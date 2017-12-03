var store = [{
	warning: "!",
	pos: "1",
	classificacao: "3 - Verde",
	tipo_list: "Regulada",
	documento: "12340987102394",
	data_solicitacao: "28-07-2017",
	cidadao: "O D N",
	nascimento: "13-05-1990",
	descricao_procedimento: "Atendimento medico"
},{
	warning: "",
	pos: "2",
	classificacao: "3 - Verde",
	tipo_list: "Regulada",
	documento: "1f310584800394",
	data_solicitacao: "08-11-2017",
	cidadao: "F C N",
	nascimento: "13-05-1990",
	descricao_procedimento: "Atendimento medico"
}];

(function () {
	"use strict";
	window.addEventListener("load", function () {
		var form = document.getElementById("initialForm");
		form.addEventListener("submit", function (event) {
			event.preventDefault();
			sendData(form);
		});
	});
	
	function sendData(form) {
		var cnes = form.querySelector("#cnes").value;
		var proc = form.querySelector("#proc").value;
		
		var xhr = new XMLHttpRequest();
		xhr.addEventListener("load", onSuccess); // Success callback
		xhr.addEventListener("error", onError); // Error Handling
		xhr.open("GET", `/escopo?cnes=${cnes}&procedimento=${proc}`);
		xhr.send();
	}
	
	function onSuccess(event) {
		localStorage.setItem('list', JSON.stringify(store));
		// localStorage.setItem('list', JSON.stringify(event.target.responseText));
		window.location.href = window.location + "list.html";
	}
	
	function onError(event) {
		console.log('Network error');
	}
})();