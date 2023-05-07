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
                console.log(tokenName + "  코인 새로 추가 로직 추가");
                console.log(eventJsonArr[i]);
                console.log('#' + tokenName + "Amt" + " //////   " + $('#' + tokenName + "Amt").text());
                addNewCoin(eventJsonArr[i].exchngCd, eventJsonArr[i].tokenName, eventJsonArr[i].coinAmount, eventJsonArr[i].nowAmt, eventJsonArr[i].positionSide);

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

    // 초기 차트 비트코인
    drawChart("02BTC");
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

            var totStartAmt = 0;
            var totNowAmt = 0;
            var totDiffAmt = 0;
            var totPercent = 0;
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
                var percent = ((nowAmt - startAmt)/startAmt*100).toFixed(2);

                totStartAmt += startAmt;
                totNowAmt += nowAmt;
                totDiffAmt += diffAmt;
                totPercent += percent;

                if(Number(totDiffAmt) < 0) {
                    document.getElementById("nulldiffAmt").style.color = "blue";
                    document.getElementById("nullper").style.color = "blue";
                } else if(Number(totDiffAmt) > 0) {
                    document.getElementById("nulldiffAmt").style.color = "red";
                    document.getElementById("nullper").style.color = "red";
                } else {
                    document.getElementById("nulldiffAmt").style.color = "black";
                    document.getElementById("nullper").style.color = "black";
                }


                $("#" + exchngCd + "startAmt").text(startAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "nowAmt").text(nowAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "diffAmt").text(diffAmt.toLocaleString('ko-KR') + " 원");
                $("#" + exchngCd + "per").text(percent + " %");
            }

            $("#nullStartAmt").text(totStartAmt.toLocaleString('ko-KR') + " 원");
            $("#nullNowAmt").text(totNowAmt.toLocaleString('ko-KR') + " 원");
            $("#nulldiffAmt").text(totDiffAmt.toLocaleString('ko-KR') + " 원");

            $("#nullPer").text(((totNowAmt - totStartAmt)/totStartAmt*100).toFixed(2) + " %");
        },
        error: function(){
            alert("simpleWithObject err");
        }
    });
}

function addNewCoin(exchngCd, tokenName, coinAmount, nowAmt, positionSide) {


var exchngCdNm = "";

if(exchngCd == "02") {
    exchngCdNm = '<strong style="color:black;">binance</strong></td>';
} else if(exchngCd == "01") {
    exchngCdNm = '<strong style="color:blue;">upbit</strong></td>';
}

var html = '';
html += '<tr>';
html +=    '<td>';
html +=     exchngCdNm;
html +=    '</td>';
html +=    '<td>';
html +=        '<a href="#">';
html +=            '<strong class="jb-large" id="' + exchngCd + tokenName + '"  text="' + tokenName + '"';
html +=                    'onclick="drawChart(this.id); "></strong>';
html +=        '</a>';
html +=    '</td>';
html +=    '<td>';
var coinCnt = coinAmount.toLocaleString();
html +=        '<p class="jb-large" value="' + tokenName + '" id="' + tokenName + 'Cnt">' + coinCnt + ' 개"</p>';
html +=    '</td>';
html +=    '<td>';
html +=        '<strong class="jb-large" value="' + nowAmt + '" id="' + tokenName + 'Amt" text="' + nowAmt.toLocaleString() + ' 원"></strong><span class=""></span>';
html +=    '</td>';
html +=    '<td>';
html +=        '<strong class="jb-large" id="newCoin' + tokenName + '" text="' + positionSide + '"></strong>';
html +=    '</td>';
html +='</tr>';

$('#coinTable > tbody:last').append(html);

$("#" + exchngCd + tokenName).text(tokenName);
$("#" + tokenName + 'Cnt').text(coinAmount.toLocaleString() + ' 개');
$("#" + tokenName + 'Amt').text(nowAmt.toLocaleString() + ' 원');
$("#" + "newCoin" + tokenName).text(positionSide);


}