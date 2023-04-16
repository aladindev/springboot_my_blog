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

    console.log(reqUrl + queryStr);
    const eventSource = new EventSource(reqUrl + queryStr);

    eventSource.onopen = (e) => {
        console.log(e);
    };

    eventSource.onmessage = event => {
        console.log(event.data);
    };
}