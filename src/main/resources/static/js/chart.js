//google.charts.load('current', {'packages':['corechart']});
//google.charts.setOnLoadCallback(drawChart);

function drawChart(id) {

    /* 분봉 차트 */
    var exchngCd = id.substring(0, 2);
    var tokenName = id.substring(2);
    console.log(exchngCd + " / " + tokenName);
    $("#chartTokenName").text(tokenName + " 1분 차트");


    setInterval(function(){
                       requestHttp(exchngCd, tokenName);
    }, 1000);


}


function requestHttp(exchngCd, tokenName) {

    var request = new XMLHttpRequest();
    var url = 'https://api.upbit.com/v1/candles/minutes/5?market=KRW-' + tokenName + '&count=100';

    request.open("GET", url, false);
    request.send();
    var jsonData = JSON.parse(request.responseText);
    console.log(jsonData);

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
            //,height: 750
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

    var chart = new ApexCharts(document.querySelector("#chart_div"), options);
    chart.render();
}