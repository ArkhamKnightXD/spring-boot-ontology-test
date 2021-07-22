<!DOCTYPE html>

<html lang="eng">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Diccionario API</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <!--Con este link soluciono el error de favicon.ico en la consola que te carga el recurso favicon.ico pero como este recurso no existe da error 404
     Y esto es malo por que reduce el performance de la pagina-->
    <link rel="shortcut icon" href="#" />

    <link rel="stylesheet" href="../../bower_components/bootstrap/dist/css/bootstrap.min.css">
    
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

    <link type="text/css" rel="stylesheet" href="../../bootstrap-4.3.1/style/popup.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>


    <!-- Theme style -->
    <link rel="stylesheet" href="../../bootstrap-4.3.1/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect. -->
    <link rel="stylesheet" href="../../bootstrap-4.3.1/dist/css/skins/_all-skins.min.css">

</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Main Header -->
    <header class="main-header">

        <!-- Logo -->
        <a href="#" class="logo">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><b>A</b>PI</span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>Diccionario</b>API</span>
        </a>

        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>


            <!-- Navbar Right Menu -->
            <div class="navbar-custom-menu">

                <ul class="nav navbar-nav">
                    <!-- Messages: style can be found in dropdown.less-->

                    <!-- User Account Menu -->
                    <li class="dropdown user user-menu">

                        <!-- Menu Toggle Button -->
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <!-- The user image in the navbar-->
                            <img src="../../bootstrap-4.3.1/dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                            <!-- hidden-xs hides the username on small devices so only the image appears. -->
                            <span class="hidden-xs">${loggedUsername}</span>
                        </a>
                        <ul class="dropdown-menu">
                            <!-- The user image in the menu -->
                            <li class="user-header">
                                <img src="../../bootstrap-4.3.1/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">

                                <p>
                                     ${loggedUsername}<!--Aqui agrego el nombre del usuario logueado -->
                                </p>
                            </li>
                            <!-- Menu Body -->
                            <li class="user-body">

                                <!-- /.row -->
                            </li>
                            <!-- Menu Footer-->
                            <li class="user-footer">
                                <div class="pull-left">
                                    <a href="#" class="btn btn-primary">Profile</a>
                                </div>
                                <div class="pull-right">
                                    <a href="/logout" class="btn btn-danger">Sign out</a>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <!-- Control Sidebar Toggle Button -->

                </ul>
            </div>
        </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">

        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">

            <!-- Sidebar user panel (optional) -->
            <div class="user-panel">
                <div class="pull-left image">
                    <img src="../../bootstrap-4.3.1/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                </div>
                <div class="pull-left info">
                    <!--Aqui pongo el nombre del usuario tambien -->
                    <p>${loggedUsername}</p>
                    <!-- Status -->
                    <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                </div>
            </div>

            <!-- search form (Optional) -->

            <!-- /.search form -->

            <!-- Sidebar Menu -->
            <ul class="sidebar-menu" data-widget="tree">
                <!-- Optionally, you can add icons to the links -->
                <li><a href="/dashboard/"><i class="fa fa-search"></i> <span>Buscar</span></a></li>
                <li ><a href="/rae/search-rae"><i class="fa fa-search"></i> <span>Buscar en la RAE</span></a></li>
                <li class="active"><a href="#"><i class="fa fa-clipboard"></i><span>Palabras propuestas</span></a></li>
                <li><a href="/surveys/"><i class="fa fa-align-left"></i> <span>Votación final</span></a></li>
                <li class="treeview">
                    <a href="#"><i class="fa fa-user"></i> <span>Admin</span>
                        <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
                    </a>
                    <ul class="treeview-menu">
                        <li class=""><a href="/dashboard/individuals">All Individuals</a></li>
                        <li><a href="/users/"><i class=""></i> <span>Users</span></a></li>
                    </ul>
                </li>
            </ul>
            <!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
<#--        <form action="/surveys/">-->
<#--            <input class="form-control form-control-dark w-100" type="text" name="sentence" placeholder="Buscar..." aria-label="Search">-->
<#--        </form>-->
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1 class="text-center">

                <strong>Palabras propuestas a votación</strong>
            </h1>
            <a class="btn btn-primary" href="/surveys/simple-survey-creation" role="button">Proponer nueva palabra</a>
<#--            <a class="btn btn-primary" href="/surveys/simple-survey-complete-creation" role="button">Votar definición</a>-->
        </section>

        <!-- Main content -->
        <section class="content container-fluid">

            <!--------------------------
              | Your Page Content Here |
              -------------------------->
            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <div class="table-responsive">
                        <table class="table table-striped table-condensed table-hover">
                            <thead class="thead-dark">

                            <th>Palabra</th>
                            <th>Definición</th>
                            <th>Cantidad de votos</th>
                            <th>Opciones</th>
<#--                            <th>Cantidad de Respuestas</th>-->

                            </thead>

                            <tbody>
                            <#list words as word >
                                <tr>
                                    <td><b>${word.getWord()}</b></td>

                                    <td>${word.getWordDefinition()}</td>
                                    <td>${word.getVotesQuantity()}</td>

                                    <td>
                                        <#if word.getWordDefinition() == "N/A">

                                            <button class="btn btn-primary" type="button" onclick="window.location.href='/surveys/simple-survey-edition?id=${word.getId()}'">Agregar definición</button>
                                        <#else>

                                            <button class="btn btn-info" type="button" onclick="showAlert()" >Agregar definición</button>
                                        </#if>

                                        <#if word.isPassTheVote()>

                                            <button class="btn btn-info" type="button" onclick="showAlertVote()">Votar</button>
                                            <a class="btn btn-primary" href="/surveys/" target="_blank" role="button">Ver estado</a>

                                        </#if>

                                        <#if word.isUserAlreadyVote()>

                                            <button class="btn btn-info" type="button" onclick="showAlertVote()" disabled>Votar</button>
                                        </#if>

                                        <#if word.getWordDefinition() == "N/A" >

                                            <button class="btn btn-info" type="button" onclick="showAlertVote()" disabled>Votar</button>
                                        </#if>

                                        <#if word.getWordDefinition() != "N/A" && !word.isUserAlreadyVote() && !word.isPassTheVote()>

                                            <button class="btn btn-primary" type="button" onclick="window.location.href='/surveys/simple-survey-vote?id=${word.getId()}'">Votar</button>

                                        </#if>

                                    </td>

                                </tr>

                            </#list>
                            </tbody>


                        </table>
<#--                        Esto tambien es parte del popup-->
                        <div class="alert hide">
                            <span class="fas fa-exclamation-circle"></span>
                            <span class="msg">Esta palabra ya cuenta con una definición</span>
                            <div class="close-btn">
                                <span class="fas fa-times"></span>
                            </div>
                        </div>

                        <div class="alert-vote hide">
                            <span class="fas fa-exclamation-circle"></span>
                            <span class="msg">Esta palabra ya pasó a la votación final, click en "Ver estado" para continuar con el proceso</span>
                            <div class="close-btn">
                                <span class="fas fa-times"></span>
                            </div>
                        </div>

                    </div>
                </div>

            </div>

        </section>
        <!-- /.content -->
    </div>

    <!-- /.content-wrapper -->

    <!-- Main Footer -->
    <footer class="main-footer">
        <!-- To the right -->
        <!-- Default to the left -->
        <strong>Copyright &copy; 2020 <a href="#">Words</a>.</strong> All rights reserved.
    </footer>

    <!-- Control Sidebar -->
     <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
    immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->

<!-- jQuery 3 -->
<script src="../../bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>
<!-- AdminLTE App -->
<script src="../../bootstrap-4.3.1/dist/js/adminlte.min.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>


<script>

    function showAlert(){

        $('.alert').addClass("show");
        $('.alert').removeClass("hide");
        $('.alert').addClass("showAlert");

        setTimeout(function(){

            $('.alert').removeClass("show");
            $('.alert').addClass("hide");
        },5000);

    };

    $('.close-btn').click(function(){

        $('.alert').removeClass("show");
        $('.alert').addClass("hide");
    });



function showAlertVote(){

    $('.alert-vote').addClass("show");
    $('.alert-vote').removeClass("hide");
    $('.alert-vote').addClass("showAlert");

    setTimeout(function(){

        $('.alert-vote').removeClass("show");
        $('.alert-vote').addClass("hide");
    },5000);

};

$('.close-btn').click(function(){

    $('.alert-vote').removeClass("show");
    $('.alert-vote').addClass("hide");
});

</script>

</body>
</html>