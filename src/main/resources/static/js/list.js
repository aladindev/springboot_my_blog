 window.onload = function(){
    console.log("onload");
    const eventSource = new EventSource(`/api/subscribe?id=${Math.random()}`);

    eventSource.onopen = (e) => {
        console.log(e);
    };
}