<?php

$servername = "134.209.13.62";
$username = "votacion1";
$password = "vOta-12.@9";
$dbname = "votaciones2019";

try {
    	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
catch(PDOException $e)
    {
    	die("OOPs hubo un error");
    }

?>