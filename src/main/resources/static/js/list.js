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
            var amtId = tokenName+"Amt";
            var regex = /[^0-9]/g;				// 숫자가 아닌 문자열을 선택하는 정규식
            var bfAmt = $('#' + tokenName+"Amt").text();
            bfAmt = bfAmt.replace(regex, "");
            var afAmt = eventJsonArr[i].nowAmt;
            afAmt = Math.floor(afAmt);

            if(Number(bfAmt) > Number(afAmt)) {
                document.getElementById(amtId).style.color = "blue";
            }
            $('#' + amtId).text(afAmt.toLocaleString() + " 원");

        }
    };
}