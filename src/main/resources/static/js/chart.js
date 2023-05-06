  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);

function drawChart() {

    /* 분봉 차트 */
    const request = new XMLHttpRequest();
    const url = 'https://api.upbit.com/v1/candles/minutes/5?market=KRW-GRS&count=100';

    request.open("GET", url, false);
    request.send();
    var obj = JSON.parse(request.responseText);
    console.log(obj);

var data = google.visualization.arrayToDataTable([
  ['Mon', 20, 28, 38, 45],
  ['Tue', 31, 38, 55, 66],
  ['Wed', 50, 55, 77, 80],
  ['Thu', 77, 77, 66, 50],
  ['Fri', 68, 66, 22, 15]
  // Treat first row as data as well.
], true);

var options = {
  legend:'none'
};

var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));

chart.draw(data, options);
}