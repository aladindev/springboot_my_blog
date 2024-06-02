


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
	const postForm = document.getElementById("postForm");

	// 폼 제출
	postForm.submit();
}