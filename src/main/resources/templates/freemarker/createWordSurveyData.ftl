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
        <h1>Completar los datos requeridos</h1>
    </div>


  <div class="center-container">


    <div class="row">

        <div class="col-md-8 order-md-1">
            <h5 class="mb-3 head-text">Seleccione la palabra con la que desea trabajar</h5>

            <form method="post" action="/surveys/survey-create" class="needs-validation"  novalidate>
                <div class="row">

                    <div class="col-md-12 mb-3">
                        <label for="individualName">Palabra</label>

                        <div class="input-group">
                            <select class="form-control" name="individualName" id="individualName" required onchange="definitionFilter()">
                                <#list words as word>
                                    <option value="${word.getLema()}">${word.getLema()}</option>
                                </#list>
                            </select>
                        </div>
                    </div>


                    <div class="col-md-12 mb-3">
                        <label for="definition">Definición</label>

                        <div class="input-group">
                            <select class="form-control" name="definition" id="definition" required>

                            </select>
                        </div>
                    </div>


                    <div class="col-md-12 mb-3">
                        <label for="individualNameRAE">Teniendo en cuenta la anterior palabra y su definición indique la palabra de la rae y su definicion correspondiente</label>
                        <input type="text" class="form-control" id="individualNameRAE" name="individualNameRAE" placeholder="Lema de la rae..." required>

                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="definitionRAE">Definicion de la rae</label>
                        <input type="text" class="form-control" id="definitionRAE" name="definitionRAE" placeholder="Definicion de la rae..." required>

                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="synonyms">Sinonimos</label>
                        <input type="text" class="form-control" id="synonyms" name="synonyms" placeholder="Sinonimo1, sinonimo2, etc..." >

                    </div>

                <div class="col-md-12 mb-3">
                    <label for="fatherClassName">Clase a la que pertenece</label>

                    <div class="input-group">
                        <select class="form-control" name="fatherClassName" required>
                            <#list classes as class>
                                <option value="${class}">${class}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <button class="btn btn-primary btn-lg btn-block my-buttons" type="submit">Guardar</button>
                <a class="btn btn-danger btn-lg btn-block my-buttons" href="/surveys/" role="button">Cancelar</a>
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


<script>

    function definitionFilter() {

        //Si le quito las variables var me falla el codigo
        var definitions = [];
        var lemmaSelected = document.querySelector("#individualName").value;


        <#list words as word>

        var wordLemma = "${word.getLema()}";

        if (lemmaSelected === wordLemma) {
            definitions.push({ id: "${word.getDefinicion()}", definition: "${word.getDefinicion()}" });
        }

        </#list>

        document.querySelector("#definition").innerHTML = "";

        for (var i = 0; i < definitions.length; i++) {
            document.querySelector("#definition").innerHTML += '<option value="' + definitions[i].id +'">' + definitions[i].definition +'</option>';
        }

        console.table(definitions);
    }

</script>

</html>
