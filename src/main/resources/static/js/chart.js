function chartRefresh() {
    drawChart($("#coinInfo").val());
}

function drawChart(id) {

    /* 분봉 차트 */
    $("#chart_div").empty();
    var exchngCd = id.substring(0, 2);
    var tokenName = id.substring(2);
    $("#chartTokenName").text(tokenName + " 1분 차트");
    $("#coinInfo").val(id);

    requestHttp(exchngCd, tokenName);

}


function requestHttp(exchngCd, tokenName) {

    $("#chart_div").empty();
    var request = new XMLHttpRequest();
    var url = '';

    if(exchngCd == "02") { // upbit
        url = 'https://api.upbit.com/v1/candles/minutes/5?market=KRW-' + tokenName + '&count=100';
    } else if(exchngCd == "01") {
        url = 'https://binance.com/api/v3/klines?symbol=BTCUSDT&interval=12h';
        //TODO CORS
    }

    request.open("GET", url, false);
    request.send();
    var jsonData = JSON.parse(request.responseText);

    var array = [];
    for(var i = jsonData.length-1 ; i >= 0 ; i--) {

        var tDate = jsonData[i].candle_date_time_kst;
        var obj = {
            "x" : new Date(tDate),
            "y" : [jsonData[i].opening_price, jsonData[i].high_price, jsonData[i].low_price, jsonData[i].trade_price]
        }
        array.push(obj);
    }

    var options = {
        series: [{
            data:array
        }],
        chart: {
            type: 'candlestick'
            ,height: 350
        },
        title: {
            text: 'CandleStick Chart',
            align: 'left'
        },
        xaxis: {
            type: 'datetime'
        },
        yaxis: {
            tooltip: {
                enabled: true
            }
        }
    };

    var chart = new ApexCharts(document.getElementById("chart_div"), options);
    chart.render();
}