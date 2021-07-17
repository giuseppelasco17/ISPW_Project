<%--
  Created by IntelliJ IDEA.
  User: Amministratore
  Date: 06/09/2018
  Time: 17:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8" />
  <title>Use Case 4</title>
    <style>
        body{font-family: sans-serif; margin: 4px;}

        .content{padding: 25px; width: 100%; box-sizing: border-box; margin: 0 auto;}
        #modulo{border-top: solid 4px #000; border-bottom: solid 4px #000; padding: 15px; background: #343434; color: #FFF;
            overflow: auto; margin-top: 20px;}
        #modulo h1{margin: 0; text-align: center;}
        #modulo select{padding: 2px; margin: 0; text-align: center;}
        #modulo ul{list-style: none; padding: 20px; margin: 0; width: 100%;  text-align: center;}
        #modulo ul li{padding: 10px 0; width: 100%; box-sizing: border-box; text-align: center;}
        #modulo ul li label{display: block;}
        #modulo ul li input{border: solid 2px #000; color: #000; text-align: center}
        #modulo ul li .bottom{color: #FFF; background: #1796ad; border: solid 2px #000; padding: 10px 25px;
            text-transform: uppercase; cursor: pointer; font-weight: bold;}
        #modulo ul li .bottom:hover{background: #000; color: #FFF; border: solid 2px #a3a3a3;}
    </style>
</head>

<body bgcolor= "#343434">
<section id="modulo">
  <div class="content">
    <h1></h1>

    <form action="/boundary.Servlet" method="post">
      <ul>
        <li>
          <select id = "list" name = "list" onchange="getSelectValue();">
            <option value = "esame">Esame</option>
            <option value = "conferenza">Conferenza</option>
          </select>
        </li>
        <li>
          <label for="course">Corso </label>
          <input type="text" name="course" id="course">
        </li>
        <li>
          <label for="session">Sessione </label>
          <input type="text" name="session" id="session">
        </li>
        <li>
          <label for="date">Data </label>
          <input type="date" name="date" id="date">
        </li>
        <li>
          <label for="begin">Ora inizio</label>
          <input type="text" name="begin" id="begin">
        </li>
        <li>
          <label for="end">Ora fine</label>
          <input type="text" name="end" id="end">
        </li>
        <li class="cinque">
          <input type="submit" value="Cerca" class="bottom">
        </li>
      </ul>

    </form>
  </div>
</section>
<script>
    function getSelectValue()
    {
        var x = document.getElementById("list").value;
        if(x === "conferenza")
        {
            document.getElementById('course').disabled = true;
            document.getElementById('session').disabled = true;
        }
        else
        {
            document.getElementById('course').disabled = false;
            document.getElementById('session').disabled = false;
        }
    }
</script>
</body>
</html>
