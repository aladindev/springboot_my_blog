async function kakaoLogin() {
    try {
        // `/api/get/url` 엔드포인트에 비동기적으로 GET 요청을 보냅니다.
        const response = await fetch('/login/kakao/');
        
        // 응답을 JSON 형태로 변환
        const data = await response.json();
        
        // 변수에 카카오 클라이언트 ID와 콜백 URL을 저장
        const client_id = data.client_id;
        const redirect_uri = data.redirect_uri;
        

        // 필요한 추가 작업을 여기에 수행할 수 있습니다.
        // 예: 클라이언트 ID와 콜백 URL을 가지고 카카오 로그인 URL을 구성하거나,
        // 사용자에게 보여줄 수 있는 어떤 형태의 인터페이스를 구성할 수 있습니다.
        const request_url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;

        // 조합된 URL로 페이지를 변경하여 카카오 로그인 페이지로 이동
        window.location.href = request_url;

    } catch (error) {
        // 에러 처리: 네트워크 요청 실패 혹은 응답 변환 중 오류가 발생할 경우
        console.error('카카오 설정 정보를 가져오는 데 실패했습니다:', error);
    }
}

function logout() {
    try {
        // fetch API를 사용하여 로그아웃 요청을 보냅니다.
        fetch('/login/logout', {
            method: 'GET', // HTTP 메소드 지정
            credentials: 'include' // 쿠키를 포함시키기 위해 credentials 옵션을 'include'로 설정
        })
        .then(response => {
            // 서버로부터의 응답을 처리합니다.
            // 로그아웃 성공 시 로그인 페이지 또는 홈페이지로 리다이렉트 할 수 있습니다.
            if (response.ok) {
                response.json().then(data => {
                    alert(data.resultMsg); // 서버로부터 받은 resultMsg를 출력
                    window.location.href = '/index';
                });
            } else {
                // 서버로부터 오류 응답을 받은 경우, 사용자에게 알립니다.
                alert('로그아웃 실패!');
            }
        })
        .catch(error => {
            // 네트워크 오류 등의 이유로 요청이 실패한 경우
            console.error('로그아웃 요청 중 오류 발생:', error);
            alert('로그아웃 중 문제가 발생했습니다.');
        });
    } catch (error) {
        // try 블록 내 코드 실행 중 발생한 예외를 처리합니다.
        console.error('로그아웃 처리 중 예외 발생:', error);
        alert('로그아웃 처리 중 오류가 발생했습니다.');
    }
}

function write_post() {

    try {
        // CSRF 토큰을 메타 태그에서 가져옵니다.
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        // fetch API를 사용하여 로그아웃 요청을 보냅니다.
        fetch('/login/isLogin', {
            method: 'POST', // HTTP 메소드 지정
            credentials: 'include',// 쿠키를 포함시키기 위해 credentials 옵션을 'include'로 설정
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가합니다.
            }
        })
        .then(response => {
            // 서버로부터의 응답을 처리합니다.
            if (response.ok) {
                response.json().then(data => {
                    if(data.isLogin) {
                        window.location.href = '/board/write';
                    } else {
                        alert("로그인이 필요합니다.");
                        return;
                    }
                    
                });
            } else {
                // 서버로부터 오류 응답을 받은 경우, 사용자에게 알립니다.
                alert('세션 유효성 검증 실패!');
            }
        })
        .catch(error => {
            // 네트워크 오류 등의 이유로 요청이 실패한 경우
            console.error('세션 유효성 검증 요청 중 오류 발생:', error);
            alert('세션 유효성 검증  중 문제가 발생했습니다.');
        });
    } catch (error) {
        // try 블록 내 코드 실행 중 발생한 예외를 처리합니다.
        console.error('세션 유효성 검증 처리 중 예외 발생:', error);
        alert('세션 유효성 검증 처리 중 오류가 발생했습니다.');
    }
}

