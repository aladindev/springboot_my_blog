window.onload = function(){

    var exchngCdArr = "";
    var reqUrl = "/api/v3/sse/subscribe?";
    var queryStr = "exchngCd=";

    for(var i = 0 ; i < document.getElementsByName("exchngCdArr").length ; i++) {
        if(i == document.getElementsByName("exchngCdArr").length-1) {
            queryStr += document.getElementsByName("exchngCdArr")[i].value;
            break;
        }
        queryStr += document.getElementsByName("exchngCdArr")[i].value;
        queryStr += "&exchngCd=";
    }

    const eventSource = new EventSource(reqUrl + queryStr);

    eventSource.onopen = (e) => {
        console.log("open >");
    };

    eventSource.onmessage = event => {
        var eventJsonArr = JSON.parse(event.data);

        for(var i = 0 ; i < eventJsonArr.length ; i++) {
            var tokenName = eventJsonArr[i].tokenName;

            if($('#' + tokenName + "Amt").text() == "" || $('#' + tokenName + "Amt").text() == undefined) {
                console.log("코인 새로 추가");

//                var addRow = "<td>";
//                addRow +=       '<img th:src=' + '"' + eventJsonArr[i].imgUrl + '"' + 'style="width: 30%; height: 30%;"/>';
//                addRow +=   '</td>';
//                addRow +=   '<td>';
//                addRow +=       '<a href="#">';
//                addRow +=           '<strong class="jb-large" th:id="${accounts.tokenName}" th:text="${accounts.tokenName}"></strong>';
//                addRow +=       '</a>';
//                addRow +=   '</td>';
//                addRow +=   '<td>';
//                addRow +=       '<p class="jb-large" th:value="${accounts.tokenName}" th:id="|${accounts.tokenName}Cnt|" th:text="|${#numbers.formatInteger(accounts.coinAmount, 1, ';
//                addRow +=               "'" + 'COMMA' + "'" + ')} 원|"></p>';
//                addRow +=   '</td>';
//                addRow +=   '<td>';
//                addRow +=       '<strong class="jb-large" th:value="${accounts.nowAmt}" th:id="|${accounts.tokenName}Amt|" th:text="|${#numbers.formatInteger(accounts.nowAmt, 1, '
//                                        "'" + 'COMMA' + "'" + ')} 원|"></strong><span class=""></span>';
//                addRow +=   '</td>';
//                addRow +=   '<td>';
//                addRow +=       '<strong class="jb-large" th:text="${accounts.positionSide}"></strong>';
//                addRow +=   '</td>';
//
//                $('#coinTable > tbody:last').append(addRow);


            } else {
                var amtId = tokenName+"Amt";
                var regex = /[^0-9]/g;				// 숫자가 아닌 문자열을 선택하는 정규식
                var bfAmt = $('#' + tokenName+"Amt").text();
                bfAmt = bfAmt.replace(regex, "");
                var afAmt = eventJsonArr[i].nowAmt;
                afAmt = Math.floor(afAmt);

                if(Number(bfAmt) > Number(afAmt)) {
                    document.getElementById(amtId).style.color = "blue";
                } else if(Number(bfAmt) < Number(afAmt)) {
                    document.getElementById(amtId).style.color = "red";
                } else {
                    document.getElementById(amtId).style.color = "black";
                }
                $('#' + amtId).text(afAmt.toLocaleString() + " 원");
            }



        }
    };

    /* tab */
    $('ul.tabs li').click(function(){
    		var tab_id = $(this).attr('data-tab');

    		$('ul.tabs li').removeClass('current');
    		$('.tab-content').removeClass('current');

    		$(this).addClass('current');
    		$("#"+tab_id).addClass('current');
    })


    //iframe
//    var iframe = document.getElementsByTagName('iframe')[0];
//    var url = iframe.src;
//    var getData = function (data) {
//        if (data && data.query && data.query.results && data.query.results.resources && data.query.results.resources.content && data.query.results.resources.status == 200) loadHTML(data.query.results.resources.content);
//        else if (data && data.error && data.error.description) loadHTML(data.error.description);
//        else loadHTML('Error: Cannot load ' + url);
//    };
//    var loadURL = function (src) {
//        url = src;
//        var script = document.createElement('script');
//        script.src = 'https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20data.headers%20where%20url%3D%22' + encodeURIComponent(url) + '%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=getData';
//        document.body.appendChild(script);
//    };
//    var loadHTML = function (html) {
//        iframe.src = 'about:blank';
//        iframe.contentWindow.document.open();
//        iframe.contentWindow.document.write(html.replace(/<head>/i, '<head><base href="' + url + '"><scr' + 'ipt>document.addEventListener("click", function(e) { if(e.target && e.target.nodeName == "A") { e.preventDefault(); parent.loadURL(e.target.href); } });</scr' + 'ipt>'));
//        iframe.contentWindow.document.close();
//    }
//
//    loadURL(iframe.src);

}