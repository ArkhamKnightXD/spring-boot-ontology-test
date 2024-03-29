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

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4" crossorigin="anonymous"></script>

    <!-- Main CSS-->
    <link href="../../forms/css/main.css" rel="stylesheet" media="all">


</head>

<body>
<div class="page-wrapper bg-gra-03 p-t-45 p-b-50">
    <div class="wrapper wrapper--w790">
        <div class="card card-5">
            <div class="card-heading">
                <h2 class="title">${word.getLemma()}</h2>
            </div>
            <div class="card-body">

                <div class="form-row">
                    <div class="name">Definición Dominicana</div>
                    <div class="value">
                        <div class="input-group">
                            <input class="input--style-5" type="text" value="${word.getDefinition()}" name="synonyms" readonly >
                        </div>
                    </div>
                </div>

                <form action="/surveys/survey-edition">

                    <div class="form-row">
                        <div class="name">Digite la palabra a buscar </div>
                        <div class="value">
                            <div class="input-group">
                                <input class="input--style-5" type="text" name="sentence" placeholder="Buscar..." required>
                            </div>
                        </div>
                    </div>

                    <input hidden class="form-control" type="number" name="id" value="${word.getId()}" >

                    <div>
                        <button class="btn btn--black" type="submit" hidden>Buscar</button>
                    </div>
                </form>

                <form method="post" action="/surveys/survey-edit?id=${word.getId()}">

                    <div class="form-row">

                        <div class="name">Palabra de la RAE</div>

                        <div class="value">
                            <label for="individualNameRAE">
                                <select class="form-select" name="individualNameRAE" id="individualNameRAE" required onchange="definitionFilter()">

                                    <option value="0">Seleccione</option>

<#--                                    <#list raeWords as raeWord>-->
                                    <option value="${raeWord}">${raeWord}</option>
<#--                                    </#list>-->
                                </select>
                            </label>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="name">Definición de la RAE</div>
                        <div class="value">
                            <div class="input-group">

                                <select class="form-select" name="definitionRAE" id="definitionRAE" required onchange="fatherClassFilter()">

                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="name">Clase a la que pertenece</div>
                        <div class="value">
                            <div class="input-group">

                                <select class="form-select" name="fatherClassName" id="fatherClassName" required>

                                </select>
                            </div>
                        </div>
                    </div>


<#--                    <div class="form-row">-->
<#--                        <div class="name">Sinonimos</div>-->
<#--                        <div class="value">-->
<#--                            <div class="input-group">-->
<#--                                <input class="input--style-5" type="text" name="synonyms" >-->
<#--                            </div>-->
<#--                        </div>-->
<#--                    </div>-->

<#--                    <div class="form-row">-->
<#--                        <div class="name">Clase a la que pertenece</div>-->

<#--                        <div class="value">-->
<#--                            <label for="fatherClassName">-->
<#--                                <select class="form-select" name="fatherClassName" required>-->
<#--                                    <#list classes as class>-->
<#--                                        <option class="input--style-5" value="${class}">${class}</option>-->
<#--                                    </#list>-->
<#--                                </select>-->
<#--                            </label>-->
<#--                        </div>-->

<#--                    </div>-->

                    <div>
                        <button class="btn btn--radius-2 btn--black" type="submit">Registrar</button>
                    </div>

                    <div>
                        <a class="btn-cancel btn--radius-2 btn--black" href="/surveys/" role="button"> Cancelar</a>
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

<script>
//
    function definitionFilter() {

        let definitions = [];

        <#list definitions as definition>

            definitions.push({ id: "${definition.getDefinition()}", definition: "${definition.getDefinition()}" });
        </#list>

        document.querySelector("#definitionRAE").innerHTML = "";

        for (let i = 0; i < definitions.length; i++) {

            document.querySelector("#definitionRAE").innerHTML += '<option value="' + definitions[i].id +'">' + definitions[i].definition +'</option>';
        }
    }


    function fatherClassFilter() {

        let fatherClasses = [];
        let selectedDefinition = document.querySelector("#definitionRAE").value;

        <#list definitions as definition>

            var actualDefinition = "${definition.getDefinition()}";

            if (actualDefinition === selectedDefinition)
                fatherClasses.push({ id: "${definition.getWordClassType()}", fatherClass: "${definition.getWordClassType()}" });
        </#list>

        document.querySelector("#fatherClassName").innerHTML = "";

        for (let i = 0; i < fatherClasses.length; i++) {

            document.querySelector("#fatherClassName").innerHTML += '<option value="' + fatherClasses[i].id +'">' + fatherClasses[i].fatherClass +'</option>';
        }
    }

</script>

</body><!-- This templates was made by Colorlib (https://colorlib.com) -->

</html>
<!-- end document-->
