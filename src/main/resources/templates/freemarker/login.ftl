<!DOCTYPE html>
<html>
<head>
    <title>Login</title>

    <link href="../../bootstrap-4.3.1/style/style-login.css" rel="stylesheet">
    <link href="../../font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet">
</head>
<body>

<form role="form" action="/login" method="post">

    <div class="login-box">
        <h1>Login</h1>

        <div class="textbox">
            <i class="fa fa-user" aria-hidden="true"></i>
            <input type="text" name="username" id="username" required placeholder="Username">
        </div>

        <div class="textbox">
            <i class="fa fa-lock" aria-hidden="true"></i>
            <input type="password" name="password" id="password" required placeholder="Password">
        </div>

        <input class="btn" type="submit" value="Log in">

    </div>

</form>

</body>
</html>