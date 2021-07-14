<!DOCTYPE html>
<!-- Created By CodingNepal -->
<html lang="en" dir="ltr">
<head>
    <meta charset="utf-8">
    <title>Warning Alert Notification | CodingNepal</title>
    <link type="text/css" rel="stylesheet" href="../../bootstrap-4.3.1/style/popup.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
</head>
<body>
<button>Show Alert</button>
<div class="alert hide">
    <span class="fas fa-exclamation-circle"></span>
    <span class="msg">Warning: This is a warning alert!</span>
    <div class="close-btn">
        <span class="fas fa-times"></span>
    </div>
</div>
<script>
    $('button').click(function(){
        $('.alert').addClass("show");
        $('.alert').removeClass("hide");
        $('.alert').addClass("showAlert");
        setTimeout(function(){
            $('.alert').removeClass("show");
            $('.alert').addClass("hide");
        },5000);
    });
    $('.close-btn').click(function(){
        $('.alert').removeClass("show");
        $('.alert').addClass("hide");
    });
</script>
</body>
</html>