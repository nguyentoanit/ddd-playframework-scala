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
});