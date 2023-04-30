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

    eleClick("tab1");
}

function eleClick(chkId) {
    var eventSource;

  switch(chkId) {
    case 'tab1' :
        var url = "/api/v3/sse/subscribe/today?";
        var qStr = "exchngCd=";
        for(var i = 0 ; i < document.getElementsByName("exchngCdArr").length ; i++) {
                if(i == document.getElementsByName("exchngCdArr").length-1) {
                    qStr += document.getElementsByName("exchngCdArr")[i].value;
                    break;
                }
                qStr += document.getElementsByName("exchngCdArr")[i].value;
                qStr += "&exchngCd=";
        }

        eventSource = new EventSource(url + qStr);

        $("#thCol2").text("당일시작가");
        $("#thCol3").text("당일현재가");
        $("#thCol4").text("손익금액");
        $("#thCol5").text("%");

        eventSource.onopen = (e) => {
            console.log("hist sse open");
        };

        eventSource.onmessage = event => {
           var eventJsonArr = JSON.parse(event.data);

           for(var i = 0 ; i < eventJsonArr.length ; i++) {
              var startAmt = Math.floor(eventJsonArr[i].startAmt);
              var nowAmt = Math.floor(eventJsonArr[i].nowAmt);
              var diffAmt = Math.floor(eventJsonArr[i].diffAmt);
              var percent = (nowAmt - startAmt) / startAmt;
              var exchngCd = eventJsonArr[i].exchngCd;
              percent = percent.toFixed(2);

              $("#" + exchngCd + "startAmt").text(startAmt.toLocaleString() + " 원");
              $("#" + exchngCd + "nowAmt").text(nowAmt.toLocaleString() + " 원");
              $("#" + exchngCd + "diffAmt").text(diffAmt.toLocaleString() + " 원");
              $("#" + exchngCd + "per").text(percent + " %");


              if(startAmt > nowAmt) {
                  document.getElementById("#" + exchngCd + "nowAmt").style.color = "blue";
                  document.getElementById("#" + exchngCd + "diffAmt").style.color = "blue";
                  document.getElementById("#" + exchngCd + "per").style.color = "blue";
              } else if(Number(bfAmt) < Number(afAmt)) {
                  document.getElementById("#" + exchngCd + "nowAmt").style.color = "red";
                  document.getElementById("#" + exchngCd + "diffAmt").style.color = "red";
                  document.getElementById("#" + exchngCd + "per").style.color = "red";
              } else {
                  document.getElementById("#" + exchngCd + "nowAmt").style.color = "block";
                  document.getElementById("#" + exchngCd + "diffAmt").style.color = "block";
                  document.getElementById("#" + exchngCd + "per").style.color = "block";
              }
              $('#' + amtId).text(afAmt.toLocaleString() + " 원");
           }
        };

        break;
    case 'tab2' :
        console.log(chkId);
        $("#thCol2").text("전일종료가");
        $("#thCol3").text("당일현재가");
        $("#thCol4").text("손익금액");
        $("#thCol5").text("%");
        break;
    default : break;
  }
}