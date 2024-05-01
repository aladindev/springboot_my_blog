async function kakaoLogin() {
    try {
        // `/api/get/url` 엔드포인트에 비동기적으로 GET 요청을 보냅니다.
        const response = await fetch('/login/kakao/get-url');
        
        // 응답을 JSON 형태로 변환
        const data = await response.json();
        
        // 변수에 카카오 클라이언트 ID와 콜백 URL을 저장
        const restapi_key = data.restapi_key;
        const redirect_url = data.redirect_url;

        // 얻은 정보를 콘솔에 출력해 볼 수 있습니다.
        console.log(`카카오 클라이언트 ID: ${restapi_key}`);
        console.log(`콜백 URL: ${redirect_url}`);

        // 필요한 추가 작업을 여기에 수행할 수 있습니다.
        // 예: 클라이언트 ID와 콜백 URL을 가지고 카카오 로그인 URL을 구성하거나,
        // 사용자에게 보여줄 수 있는 어떤 형태의 인터페이스를 구성할 수 있습니다.

    } catch (error) {
        // 에러 처리: 네트워크 요청 실패 혹은 응답 변환 중 오류가 발생할 경우
        console.error('카카오 설정 정보를 가져오는 데 실패했습니다:', error);
    }
}