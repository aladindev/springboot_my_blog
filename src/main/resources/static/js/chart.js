  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    /* 분봉 차트 */
    const request = new XMLHttpRequest();
    const url = 'https://api.upbit.com/v1/candles/minutes/5?market=KRW-GRS&count=50';

    request.open("GET", url, false);
    request.send();
    var jsonData = JSON.parse(request.responseText);
    var array = [];
    array.push(['LineName', 'Car', 'Bus','Motorcycle', 'Person']);
    jsonData.forEach((item, i)=>{
      var hd = i % 10 == 0 ? item.candle_date_time_kst : ' ';

      array.push([hd, item.high_price, item.opening_price, item.trade_price, item.low_price]);
    })

    var data = google.visualization.arrayToDataTable(array);

    var options = {
      legend:'none'
    };

    var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));

    chart.draw(data, options);
}