(function () {
	"use strict";
	
	window.addEventListener("load", function () {
		init();
	});
	
	function init() {
		var data = JSON.parse(localStorage.getItem('list'));
		var table = document.querySelector("#items-display");
		
		data.forEach(function (item) {
			var row = document.createElement("tr");
			
			for (var k in item) {
				var td = document.createElement("td");
				if (k === "documento") {
					var a = document.createElement("a");
					a.onclick = showCNS.bind(null, item[k]);
					a.innerHTML = item[k];
					a.href = "javascript:void(0);";
					td.appendChild(a);
				} else {
					td.innerHTML = item[k];
				}
				row.appendChild(td);
			}

			table.appendChild(row);
		});
	}
	
	function showCNS(v) {
		console.log(v);
	}
})();