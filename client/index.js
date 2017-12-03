var store = [{
	warning: "❗",
	pos: "1",
	classificacao: "3 - Verde",
	tipo_list: "Regulada",
	documento: "898001154992774",
	data_solicitacao: "28-07-2017",
	cidadao: "O D N",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
},{
	warning: "❗",
	pos: "22",
	classificacao: "2 - Amarelo",
	tipo_list: "Regulada",
	documento: "898050092758632",
	data_solicitacao: "08-11-2017",
	cidadao: "F C N",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
},{
	warning: "❗",
	pos: "45",
	classificacao: "3 - Verde",
	tipo_list: "Regulada",
	documento: "898001154946136",
	data_solicitacao: "21-10-2017",
	cidadao: "J S D S",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
},{
	warning: "",
	pos: "9",
	classificacao: "1 - Vermelho",
	tipo_list: "Regulada",
	documento: "898002837216919",
	data_solicitacao: "06-06-2017",
	cidadao: "M D P O",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
},{
	warning: "",
	pos: "6",
	classificacao: "1 - Vermelho",
	tipo_list: "Regulada",
	documento: "898002388212547",
	data_solicitacao: "04-08-2017",
	cidadao: "L C",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
},{
	warning: "",
	pos: "2",
	classificacao: "3 - Verde",
	tipo_list: "Regulada",
	documento: "898002907697953",
	data_solicitacao: "11-03-2017",
	cidadao: "Y V",
	nascimento: "13-05-1990",
	descricao_procedimento: "RAIO X CONTRASTADO PEDIATRICO"
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

	new autoComplete({
		selector: 'input[name="Cnes"]',
		minChars: 2,
		source: function query(term, suggest) {
			term = term.toUpperCase();

			var xhr = new XMLHttpRequest();
			xhr.addEventListener("load", function (event) {
				var resp = event.target.responseText;
				console.log("suggesting", resp);
				suggest(resp);
			});
			xhr.open("GET", `/autocnes?cnes=${term}`);
		},
		onSelect: function(e, term, item){
			console.log(e, term, item);
		}
	});

	new autoComplete({
		selector: 'input[name="Procedimento"]',
		minChars: 2,
		source: function query(term, suggest) {
			term = term.toUpperCase();

			var xhr = new XMLHttpRequest();
			xhr.addEventListener("load", function (event) {
				var resp = event.target.responseText;
				console.log("suggesting", resp);
				suggest(resp);
			});
			xhr.open("GET", `/autoproc?procedimeto=${term}`);
		},
		onSelect: function(e, term, item){
			console.log(e, term, item);
		}
	});
})();
