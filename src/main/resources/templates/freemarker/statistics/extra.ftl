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

<#--            Mientras resuelvo lo de separar usuario sin log de usuario normal comentare varias cosas-->
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
                <li ><a href="/dashboard/"><i class="fa fa-search"></i> <span>Buscar</span></a></li>
                <li><a href="/rae/search-rae"><i class="fa fa-search"></i> <span>Buscar en la RAE</span></a></li>

                <li class="treeview">
                    <a href="#"><i class="fa fa-clipboard"></i> <span>Votación</span>
                        <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="/surveys/simple/"><i class="fa fa-clipboard"></i><span>Palabras propuestas</span></a></li>
                        <li><a href="/surveys/"><i class="fa fa-align-left"></i> <span>Votación final</span></a></li>
                    </ul>
                </li>

                <li class="treeview">
                    <a href="#"><i class="fa fa-align-left"></i> <span>Estadística</span>
                        <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="/dashboard/individuals/statistics-top"><i class="fa fa-align-left"></i> <span>Top votadas</span></a></li>
                        <li class="active"><a href="/dashboard/individuals/statistics-percentage"><i class="fa fa-align-left"></i> <span>Top porcentajes</span></a></li>
                        <li><a href="/dashboard/individuals/statistics-total"><i class="fa fa-align-left"></i> <span>Total palabras</span></a></li>
                        <li><a href="/dashboard/individuals/statistics-total-users"><i class="fa fa-align-left"></i> <span>Total usuarios</span></a></li>
                    </ul>
                </li>


                <li class="treeview">
                    <a href="#"><i class="fa fa-user"></i> <span>Admin</span>
                        <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
              </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="/dashboard/individuals">Administración</a></li>
                        <li><a href="/users/"><i class=""></i> <span>Usuarios</span></a></li>
                    </ul>
                </li>
            </ul>
            <!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
<#--        <form action="/dashboard/">-->
<#--            <input class="form-control form-control-dark w-100" type="text" name="sentence" placeholder="Buscar..." aria-label="Search">-->
<#--        </form>-->
<#--        <!-- Content Header (Page header) &ndash;&gt;-->
<#--        <section class="content-header">-->
<#--            <h1 class="text-center">-->

<#--                <strong>Resultados</strong>-->
<#--            </h1>-->
<#--&lt;#&ndash;            <a class="btn btn-primary" href="/surveys/survey-creation" role="button">Agregar</a>&ndash;&gt;-->
<#--        </section>-->

        <!-- Main content -->
        <section class="content container-fluid">

            <!--------------------------
              | Your Page Content Here |
              -------------------------->
            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

                    <canvas class="my-4" id="myChart" width="900" height="380"></canvas>

                    <h2>Información de los terminos</h2>
                    <div class="table-responsive">
                        <table class="table table-striped table-sm">
                            <thead class="thead-dark">
                            <tr>
                                <th>Lema</th>
                                <th>Definición</th>
                                <#--                        <th>Fecha</th>-->

                            </tr>
                            </thead>
                            <#list words as word>
                                <tbody>
                                <tr>
                                    <td>${word.lema}</td>
                                    <td>${word.definicion}</td>
                                    <#--                        <td>${data.totalConfirmed}</td>-->
                                </tr>

                                </tbody>
                            </#list>
                        </table>

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

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>


<script>

    let topPercentagesWords = [];
    let topPercentages = [];

    <#list words as word>

    //las comillas son necesarias aqui a pesar de que estoy enviando un string t
        topPercentagesWords.push("${word.getLema()}");

    </#list>

    <#list percentages as percentage>

        topPercentages.push(${percentage});

    </#list>


    // back ground color es para cambiar el color de lo que hay debajo de la linea y border color es para cambiar el color de la linea
    new Chart(document.getElementById("myChart"), {

        type: 'horizontalBar',
        data: {
            labels: topPercentagesWords,
            datasets: [
                {
                    label: "Porcentaje",
                    backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#e8b9c2"],
                    data: topPercentages
                }
            ]
        },
        options: {

            legend: { display: false },

            title: {
                display: true,
                text: 'Palabras con mayor porcentaje de acuerdo'
            }
        }
    });
</script>

</body>
</html>