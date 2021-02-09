<!doctype html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Diccionario API</title>

    <link rel="shortcut icon" href="#" />

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">

    <link href="../../bootstrap-4.3.1/style/creation-form.css" rel="stylesheet">

</head>

<body class="bg-light">
<div class="container">
    <div class="py-5 text-center">
        <h1>Editar lema</h1>
    </div>


  <div class="center-container">


    <div class="row">

        <div class="col-md-8 order-md-1">
            <h5 class="mb-3 head-text">Digite la informacion del lema</h5>

            <form method="post" action="/words/edit" class="needs-validation"  novalidate>
                <div class="row">
                    <!--Aqui siempre mando el nombre original del individual para poder hacer la edicion de manera efectiva-->
                        <input type="hidden" class="form-control" value="${lema}" id="name" name="originalIndividualName" >

                    <div class="col-md-12 mb-3">
                        <label for="individualName">Lema</label>
                        <input type="text" class="form-control" value="${lema}" id="name" name="individualName" placeholder="Lema..."  required>

                    </div>

                    <#if lemmaRAE??>
                    <div class="col-md-12 mb-3">
                        <label for="individualNameRAE">Lema de la rae</label>
                        <input type="text" class="form-control" value="${lemmaRAE}" id="individualNameRAE" name="individualNameRAE" placeholder="Lema de la rae...">

                    </div>
                    </#if>

                        <div class="col-md-12 mb-3">
                            <label for="definition">Definicion</label>
                            <input type="text" class="form-control" value="${definicion}" id="definition" name="definition" placeholder="Definicion..."  required>

                        </div>

                    <#if ejemplo??>
                        <div class="col-md-12 mb-3">
                            <label for="ejemplo">Ejemplo</label>
                            <input type="text" class="form-control" value="${ejemplo}" id="example" name="example" placeholder="Ejemplo..." >
                        </div>
                    </#if>

                    <#if sinonimos??>
                    <div class="col-md-12 mb-3">
                        <label for="synonyms">Sinonimos</label>
                        <input type="text" class="form-control" value="${sinonimos}" id="synonyms" name="synonyms" placeholder="Sinonimo1, sinonimo2, etc..." >

                    </div>
                    </#if>


                    <div class="col-md-12 mb-3">
                        <label for="fatherClassName">Clase a la que pertenece</label>

                        <div class="input-group">
                            <select class="form-control" name="fatherClassName">
                                <option value="${fatherClass}">${fatherClass}</option>
                                <#list classes as class>
                                    <option value="${class}">${class}</option>
                                </#list>
                            </select>
                        </div>
                    </div>

                <button class="btn btn-primary btn-lg btn-block my-buttons" type="submit">Guardar</button>
                <a class="btn btn-danger btn-lg btn-block my-buttons" href="/words/individuals" role="button">Cancelar</a>
                </div>
            </form>
    </div>
  </div>


</div>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script>window.jQuery || document.write('<script src="../../bootstrap-4.3.1/dist/js/jquery-3.2.1.slim.min.js"><\/script>')</script><script src="../../bootstrap-4.3.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="../../bootstrap-4.3.1/dist/js/form-validation.js"></script>
</body>

</html>
