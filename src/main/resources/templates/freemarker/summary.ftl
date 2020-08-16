<!doctype html>

<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>diccionario</title>

  <link href="../../bootstrap-4.3.1/dist/css/bootstrap.min.css" rel="stylesheet">

  <link href="../../bootstrap-4.3.1/style/dashboard.css" rel="stylesheet">

</head>

<body>
<form action="/words/">
  <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">home</a>
    <input class="form-control form-control-dark w-100" type="text" name="individualName" placeholder="Search..." aria-label="Search">
    <ul class="navbar-nav px-1">
      <li class="nav-item text-nowrap">
        <input type="submit" value="Search" class="btn btn-dark"/>
      </li>
    </ul>
  </nav>
</form>

<div class="container-fluid">
  <div class="row">
    <nav class="col-md-2 d-none d-md-block bg-dark sidebar">
      <div class="sidebar-sticky">
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link active" href="#">
              <span data-feather="home"></span>
              Test <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="/words/creation">
              <span data-feather="layers"></span>
              Add New Word
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="/words/individuals">
              <span data-feather="layers"></span>
              All Individuals
            </a>
          </li>

        </ul>

      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">

      </div>


      <h2>Resultados</h2>
      <div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead class="thead-dark">

          <tr>
            <th>Lema</th>
            <th>Definicion</th>
            <th>Ejemplo</th>
            <th>Locucion</th>
            <th>Marca Gramatical</th>
            <th>Marca Nivel Sociocultural</th>
            <th>Marca Variacion Estilistica</th>

          </tr>
          </thead>

            <tbody>
            <#list words as word >
              <tr>
                <td><b>${word.lema}</b></td>
                <td>${word.definicion}</td>

                <#if word.ejemplo??>
                    <td>${word.ejemplo}</td>
                  <#else>
                  <td>N/A</td>
                </#if>

                <#if word.locucion??>
                  <td>${word.locucion}</td>
                <#else>
                  <td>N/A</td>
                </#if>

                <#if word.marcaGramatical??>
                  <td>${word.marcaGramatical}</td>
                  <#else>
                    <td>N/A</td>
                </#if>

                <#if word.marcaNivelSocioCultural??>
                  <td>${word.marcaNivelSocioCultural}</td>
                <#else>
                  <td>N/A</td>
                </#if>

                <#if word.marcaVariacionEstilistica??>
                  <td>${word.marcaVariacionEstilistica}</td>
                <#else>
                  <td>N/A</td>
                </#if>
              </tr>
            </#list>

            </tbody>

        </table>


      </div>
    </main>
  </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script>window.jQuery || document.write('<script src="../../bootstrap-4.3.1/dist/js/jquery-3.2.1.slim.min.js"><\/script>')</script>
<script src="../../bootstrap-4.3.1/assets/js/vendor/popper.min.js"></script>
<script src="../../bootstrap-4.3.1/dist/js/bootstrap.min.js"></script>

<!-- Icons -->
<script src="https://unpkg.com/feather-icons/dist/feather.min.js"></script>
<script>
  feather.replace()
</script>
</body>
</html>
