$(document).ready(function() {
    $('#content').summernote({height: 400});
    $('.note-editor').attr({
        'data-toggle':'popover',
        'title':'popover',
        'data-content':'aaaa'
    });
    $('#article').on('submit', function(e) {
        if($('#content').summernote('isEmpty')) {
            e.preventDefault();
        } else {
            $('#article').submit();
        }
    })
    $("#thumbnail").change(function() {
        var size = $("#thumbnail")[0].files[0].size / 1024
        if(size > 400) {
            $("#thumbnail").val("");
            alert("File size is above 400KB. Please choose another file!")
        }
    })

    if($("#login-information").html() != "") $("#login-button").hide()
});

function login(){
    $('#login-form').show()
}

function logout(){
    var result = confirm("Are you sure you want to log out?");
    if(result) window.location = "/logout";
}
