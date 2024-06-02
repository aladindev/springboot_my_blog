


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
	const submitBtn = document.getElementById("submitBtn");
	const uploadForm = document.getElementById("uploadForm");

	// 폼 제출 버튼 클릭 이벤트 리스너
	submitBtn.addEventListener("click", function() {
		// 기존의 숨겨진 입력 필드 제거
		const existingInputs = uploadForm.querySelectorAll('input[type="hidden"]');
		existingInputs.forEach(input => input.remove());

		// 모든 이미지를 숨겨진 입력 필드로 변환
		const images = imagePreview.querySelectorAll('img[name="img"]');
		images.forEach(image => {
			const hiddenInput = document.createElement("input");
			hiddenInput.setAttribute("type", "hidden");
			hiddenInput.setAttribute("name", "img[]");
			hiddenInput.value = image.src;
			uploadForm.appendChild(hiddenInput);
		});

		// 폼 제출
		uploadForm.submit();
	});
});

function doSubmit() {


	if($("#title").val() == '') {
		alert("제목을 입력해주세요!");
		return;
	}
	if($("#summernote").val() == '') {
		alert("내용을 입력해주세요!");
		return;
	}

	const submitBtn = document.getElementById("submitBtn");
	const uploadForm = document.getElementById("uploadForm");

	// 기존의 숨겨진 입력 필드 제거
	const existingInputs = uploadForm.querySelectorAll('input[type="hidden"]');
	existingInputs.forEach(input => input.remove());

	// 모든 이미지를 숨겨진 입력 필드로 변환
	const images = imagePreview.querySelectorAll('img[name="img"]');
	images.forEach(image => {
		const hiddenInput = document.createElement("input");
		hiddenInput.setAttribute("type", "hidden");
		hiddenInput.setAttribute("name", "img[]");
		hiddenInput.value = image.src;
		uploadForm.appendChild(hiddenInput);
	});

	// 폼 제출
	uploadForm.submit();
}