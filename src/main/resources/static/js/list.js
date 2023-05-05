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
                console.log("코인 새로 추가 로직 추가");
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

    		console.log(tab_id);
    });


    /* 매매일지 */
    for(var i = 0 ; i < document.getElementsByName("diffAmt").length ; i++) {

        var diffAmt = document.getElementsByName("diffAmtVal")[i].value;
        var diffId = document.getElementsByName('diffAmt')[i].id;
        var perId = document.getElementsByName('percent')[i].id;

        if(Number(diffAmt) < 0) {
            document.getElementById(diffId).style.color = "blue";
            document.getElementById(perId).style.color = "blue";
        } else if(Number(diffAmt) > 0) {
            document.getElementById(diffId).style.color = "red";
            document.getElementById(perId).style.color = "red";
        } else {
            document.getElementById(diffId).style.color = "black";
            document.getElementById(perId).style.color = "black";
        }
    }
}


function tabClick(tabId) {
    var eventSource;
    var map = { 'tabId' : tabId };

    switch(tabId) {
        case 'tab1' :
            $("#thCol2").text("당일시작가");
            $("#thCol3").text("당일현재가");
            $("#thCol4").text("손익금액");
            $("#thCol5").text("%");
            break;
        case 'tab2' :
            $("#thCol2").text("전일종료가");
            $("#thCol3").text("당일현재가");
            $("#thCol4").text("손익금액");
            $("#thCol5").text("%");
            break;
        case 'tab3' :
            $("#thCol2").text("주간시작가");
            $("#thCol3").text("당일현재가");
            $("#thCol4").text("손익금액");
            $("#thCol5").text("%");
            break;
        default : break;
    }

    $.ajax({
        url: "/accounts/tab_change",
        type: "GET",
        data: map,
        success: function(data){
            var resultJson = JSON.stringify(data);
            resultJson = JSON.parse(resultJson);

            for(var i = 0 ; i < resultJson.length ; i++) {
                var diffAmt = resultJson[i].diffAmt;
                var exchngCd = resultJson[i].exchngCd;
                var diffId = resultJson[i].exchngCd + "diffAmt";
                var perId = resultJson[i].exchngCd + "per";

                if(Number(diffAmt) < 0) {
                    document.getElementById(diffId).style.color = "blue";
                    document.getElementById(perId).style.color = "blue";
                } else if(Number(diffAmt) > 0) {
                    document.getElementById(diffId).style.color = "red";
                    document.getElementById(perId).style.color = "red";
                } else {
                    document.getElementById(diffId).style.color = "black";
                    document.getElementById(perId).style.color = "black";
                }
                var startAmt = resultJson[i].startAmt;
                var nowAmt = resultJson[i].nowAmt;
                var diffAmt = resultJson[i].diffAmt;
                $("#" + exchngCd + "startAmt").text(startAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "nowAmt").text(nowAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "diffAmt").text(diffAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "per").text(((nowAmt - startAmt)/startAmt*100).toFixed(2) + " %");
            }
        },
        error: function(){
            alert("simpleWithObject err");
        }
    });
}