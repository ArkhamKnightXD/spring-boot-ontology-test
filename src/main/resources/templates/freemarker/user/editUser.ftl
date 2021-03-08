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
        <h1>Editar Usuario</h1>
    </div>


  <div class="center-container">


    <div class="row">

        <div class="col-md-8 order-md-1">
            <h5 class="mb-3 head-text">Digite la informacion del Usuario</h5>

            <form method="post" action="/users/edit?id=${user.getId()}" class="needs-validation"  novalidate>
                <div class="row">
                    <div class="col-md-12 mb-3">
                        <label for="username">Nombre de usuario</label>
                        <input type="text" class="form-control" value="${user.getUsername()}" id="username" name="username" placeholder="Nombre de usuario..."  required>

                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="password">Password</label>
                        <input type="password" class="form-control"  id="password" name="password" placeholder="Password...">

                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="email">Email</label>
                        <input type="text" class="form-control" value="${user.getEmail()}" id="email" name="email" placeholder="Email..."  required>

                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="idRol">Rol del usuario</label>

                        <div class="input-group">
                            <select class="form-control" name="idRol" required>
                                <#list roles as rol>
                                    <option value="${rol.getId()}">${rol.getRol()}</option>
                                </#list>
                            </select>
                        </div>
                    </div>

                <button class="btn btn-primary btn-lg btn-block my-buttons" type="submit">Guardar</button>
                <a class="btn btn-danger btn-lg btn-block my-buttons" href="/users/" role="button">Cancelar</a>
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
