


$(document).ready(function() {
	$('#summernote').summernote({
		  height: 300,                 // 에디터 높이
		  minHeight: null,             // 최소 높이
		  maxHeight: null,             // 최대 높이
		  focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
		  lang: "ko-KR",					// 한글 설정
		  placeholder: '최대 2048자까지 쓸 수 있습니다'	//placeholder 설정
          
	});
});

document.addEventListener("DOMContentLoaded", function() {
    // 이미지 데이터를 숨겨진 입력 필드로 변환하는 함수
    function convertImageToHiddenInput(image) {
        // 숨겨진 입력 필드 생성
        const hiddenInput = document.createElement("input");
        hiddenInput.setAttribute("type", "hidden");
        hiddenInput.setAttribute("name", "img[]"); // 서버에서 배열로 받기 위해 이름을 'img[]'로 지정
        hiddenInput.value = image.src; // 이미지의 Base64 데이터를 값으로 설정
        
        // 폼에 숨겨진 입력 필드 추가
        document.getElementById("postForm").appendChild(hiddenInput);
    }

    // 모든 이미지를 숨겨진 입력 필드로 변환
    const images = document.querySelectorAll('img[name="img"]');
    images.forEach(convertImageToHiddenInput);
});