google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

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


    console.log(exchngCd + " / " + tokenName);
    var request = new XMLHttpRequest();
    var url = 'https://api.upbit.com/v1/candles/minutes/5?market=KRW-' + tokenName + '&count=30';
    request.open("GET", url, false);
        request.send();
        var jsonData = JSON.parse(request.responseText);
        var array = [];
        var series = {};
        array.push(['LineName', 'Car', 'Bus','Motorcycle', 'Person']);

        console.log(jsonData.length);
        for(var i = jsonData.length-1 ; i >= 0 ; i--) {
          var hd = i % 10 == 0 ? jsonData[i].candle_date_time_kst : ' ';

          array.push(['1min', jsonData[i].high_price, jsonData[i].opening_price
                            , jsonData[i].trade_price, jsonData[i].low_price]);
        }

        var data = google.visualization.arrayToDataTable(array);

        var options = {
          legend:'none',
          series: {
                      0: { color: '#a561bd' },
                      1: { color: '#c784de' },
                      2: { color: '#f1ca3a' },
                      3: { color: '#2980b9' },
                      4: { color: '#e67e22' }
          }
        };

        var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));

        chart.draw(data, options);
}