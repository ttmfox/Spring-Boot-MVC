<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                    <meta name="description" content="Hỏi Dân IT - Dự án laptopshop" />
                    <meta name="author" content="Hỏi Dân IT" />
                    <title>Dashboard - Hỏi Dân IT</title>
                    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
                        rel="stylesheet" />
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <div class="container-fluid px-4">
                                    <h1 class="mt-4">Order</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item"><a href="/admin/order">Order</a></li>
                                        <li class="breadcrumb-item active">View detail</li>
                                    </ol>
                                    <div class="container mt-5">
                                        <div class="row">
                                            <div class="col-12 mx-auto">
                                                <div>
                                                    <h3>Order detail with id = ${id}</h3>
                                                </div>
                                                <hr />
                                                <div class="table-responsive">
                                                    <table class="table">
                                                        <thead>
                                                            <tr>
                                                                <th scope="col">Product</th>
                                                                <th scope="col">Name</th>
                                                                <th scope="col">Price</th>
                                                                <th scope="col">Quantity</th>
                                                                <th scope="col">Total</th>
    
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="orderDetail" items="${orderDetails}">
                                                                <tr>
                                                                    <th scope="row">
                                                                        <div class="d-flex align-items-center">
                                                                            <img src="/images/product/${orderDetail.product.image}"                                              
                                                                                class="img-fluid me-5 rounded-circle"
                                                                                style="width: 80px; height: 80px;" alt="">
                                                                        </div>
                                                                    </th>
                                                                    <td>
                                                                        <p class="mb-0 mt-4">
                                                                            <a  target="_blank">
                                                                                ${orderDetail.product.name}
                                                                            </a>
                                                                        </p>
                                                                    </td>
                                                                    <td>
                                                                        <p class="mb-0 mt-4">
                                                                            <fmt:formatNumber type="number" value="${orderDetail.price}" /> vnd
                                                                        </p>
                                                                    </td>
                                                                    <td>
                                                                        <div class="input-group quantity mt-4" style="width: 100px;">
                                                                            <input type="text"
                                                                                class="form-control form-control-sm text-center border-0"
                                                                                value="${orderDetail.quantity}">
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <p class="mb-0 mt-4" >
                                                                            <fmt:formatNumber type="number"
                                                                                value="${orderDetail.price * orderDetail.quantity}" /> đ
                                                                        </p>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
    
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <a href="/admin/order" class="btn btn-success mt-3">Back</a>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </main>
                            <jsp:include page="../layout/footer.jsp" />
                        </div>
                    </div>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                        crossorigin="anonymous"></script>
                    <script src="js/scripts.js"></script>
                    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                        crossorigin="anonymous"></script>
                    <script src="js/chart-area-demo.js"></script>
                    <script src="js/chart-bar-demo.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
                        crossorigin="anonymous"></script>
                    <script src="js/datatables-simple-demo.js"></script>
                </body>

                </html>