<?php

     include 'config.inc.php';
	 
	 // Android pregunta	
     if(isset($_POST['username']) && isset($_POST['password']))
     {
		  $result='';
	   	  $username = $_POST['username'];
          $password = $_POST['password'];
		  
		  // Existe o no
          $sql = 'SELECT * FROM users2 WHERE  email = :username AND password = :password';
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':username', $username, PDO::PARAM_STR);
          $stmt->bindParam(':password', $password, PDO::PARAM_STR);
          $stmt->execute();
          if($stmt->rowCount())
          {
			 $result="true";	
          }  
          elseif(!$stmt->rowCount())
          {
			  	$result="false";
          }
		  
		  // Esto recibe la app
   		  echo $result;
  	}
	
?>