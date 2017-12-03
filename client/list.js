(function () {
	"use strict";
	
	window.addEventListener("load", function () {
		init();
	});
	
	function init() {
		var data = JSON.parse(localStorage.getItem('list'));
		var table = document.querySelector("#items-display");
		
		data.forEach(function (item) {
			var cols = Object.values(item).map(function (v) {
				return "<td>"+v+"</td>";
			});
			
			var cns = item.documento;
			cols[4] = `<td><a href="javascript:showCNS(${cns})"> ${cns} </a></td>`;
			var row = document.createElement("tr")
			row.innerHTML = cols.join("");
			table.appendChild(row);
		});
	}
	
	function showCNS(v) {
		console.log(v);
	}
})();