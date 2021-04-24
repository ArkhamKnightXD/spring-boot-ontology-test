<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags-->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Colorlib Templates">
    <meta name="author" content="Colorlib">
    <meta name="keywords" content="Colorlib Templates">

    <!-- Title Page-->
    <title>Registro</title>

    <link rel="icon" type="image/png" href="../../bootstrap-4.3.1/dist/img/icons/favicon.ico"/>
    <!-- Icons font CSS-->
    <link href="../../forms/vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

    <!-- Font special for pages-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">

    <!-- Vendor CSS-->
    <link href="../../forms/vendor/select2/select2.min.css" rel="stylesheet" media="all">
    <link href="../../forms/vendor/datepicker/daterangepicker.css" rel="stylesheet" media="all">

    <!-- Main CSS-->
    <link href="../../forms/css/main.css" rel="stylesheet" media="all">
</head>

<body>
    <div class="page-wrapper bg-gra-03 p-t-45 p-b-50">
        <div class="wrapper wrapper--w790">
            <div class="card card-5">
                <div class="card-heading">
                    <h2 class="title">Formulario de registro</h2>
                </div>
                <div class="card-body">
                    <form method="post" action="/sing-up">
                        <div class="form-row">
                            <div class="name">Nombre de usuario</div>
                            <div class="value">
                                <div class="input-group">
                                    <input class="input--style-5" type="text" name="username" required>
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="name">Email</div>
                            <div class="value">
                                <div class="input-group">
                                    <input class="input--style-5" type="text" name="email" required>
                                </div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="name">Contraseña</div>
                            <div class="value">
                                <div class="input-group">
                                    <input class="input--style-5" type="password" name="password" required>
                                </div>
                            </div>
                        </div>

                        <div>
                            <button class="btn btn--radius-2 btn--red" type="submit">Registrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Jquery JS-->
    <script src="../../bootstrap-4.3.1/vendor/jquery/jquery-3.2.1.min.js"></script>
    <!-- Vendor JS-->
    <script src="../../forms/vendor/select2/select2.min.js"></script>
    <script src="../../forms/vendor/datepicker/moment.min.js"></script>
    <script src="../../forms/vendor/datepicker/daterangepicker.js"></script>

    <!-- Main JS-->
    <script src="../../forms/js/global.js"></script>

</body><!-- This templates was made by Colorlib (https://colorlib.com) -->

</html>
<!-- end document-->