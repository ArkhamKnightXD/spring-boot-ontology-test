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
<form action="/find/">
  <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="/words/">words</a>
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
              Total Death Cases <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="/words/creation">
              <span data-feather="layers"></span>
              Add New Word
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-light" href="#">
              <span data-feather="layers"></span>
              All Individuals
            </a>
          </li>

        </ul>

        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
          <span class="text-light">text </span>
          <a class="d-flex align-items-center text-muted text-light" href="#">
            <span data-feather="plus-circle"></span>
          </a>
        </h6>
        <ul class="nav flex-column mb-2">
          <li class="nav-item">
            <a class="nav-link text-light" href="/dashboard/worldwide">
              <span data-feather="file-text"></span>
              Total cases report
            </a>
          </li>
        </ul>
        <ul class="nav flex-column mb-2">
          <li class="nav-item">
            <a class="nav-link text-light" href="/dashboard/new">
              <span data-feather="file-text"></span>
              New cases report
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">

      </div>


      <h2>Words</h2>
      <div class="table-responsive">
        <table class="table table-striped table-sm">
          <thead class="thead-dark">
          <tr>
            <th>Lema</th>
            <th>Options</th>

          </tr>
          </thead>
            <tbody>
            <#list individuals as individual>
              <tr>
                <td>${individual}</td>

                <td>
                  <a class="btn btn-outline-dark my-2 my-sm-0" href="/words/edition?individualName=${individual}" role="button">Editar</a>
                  <a class="btn btn-outline-danger my-2 my-sm-0" href="/words/delete?individualName=${individual}" role="button">Eliminar</a>
                </td>
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
