<!doctype html>

<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${title}</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">


    <link href="../../bootstrap-4.3.1/style/dashboard.css" rel="stylesheet">

</head>

<body>
<form action="/dashboard/deaths">
    <nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0">
        <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="/dashboard/">Covid-19-Dashboard</a>
        <input class="form-control form-control-dark w-100" type="text" name="country" placeholder="Search..." aria-label="Search">
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
                        <a class="nav-link text-light" href="/dashboard/summary">
                            <span data-feather="layers"></span>
                            Countries Summary
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-light" href="/dashboard/recovered">
                            <span data-feather="layers"></span>
                            New Recovered Cases
                        </a>
                    </li>

                </ul>

                <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                    <span class="text-light">Worldwide report </span>
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

            <canvas class="my-4" id="myChart" width="900" height="380"></canvas>

            <h2>Data of the countries</h2>
            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead class="thead-dark">
                    <tr>
                        <th>Country</th>
                        <th>Total Deaths</th>
                        <th>New Deaths</th>
                    </tr>
                    </thead>
                    <#list datas as data>
                    <tbody>
                    <tr>
                        <td>${data.country}</td>
                        <td>${data.totalDeaths}</td>
                        <td>${data.newDeaths}</td>
                    </tr>

                    </tbody>
                    </#list>
                </table>

            </div>
        </main>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV" crossorigin="anonymous"></script>


<!-- Graphs -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
<script>


    let countriesNames = [];

    <#list graphics as graphic>

    countriesNames.push("${graphic.country}");

    </#list>

    let firstPlace = ${firstCountry};
    let secondPlace = ${secondCountry};
    let thirdPlace = ${thirdCountry};
    let fourthPlace = ${fourthCountry};

// back ground color es para cambiar el color de lo que hay debajo de la linea y border color es para cambiar el color de la linea
    new Chart(document.getElementById("myChart"), {
        type: 'polarArea',
        data: {
            labels: countriesNames,
            datasets: [
                {
                    label: "Total Deaths",
                    backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#e8c3b9"],
                    data: [firstPlace,secondPlace,thirdPlace,fourthPlace]
                }
            ]
        },
        options: {
            legend: { display: false },
            title: {
                display: true,
                text: 'Total Deaths Cases by Countries'
            }
        }
    });
</script>
</body>
</html>
