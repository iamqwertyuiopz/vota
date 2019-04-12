<?php
 
include 'config.php';
 
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;
 
 $ImageData = $_POST['image_data'];
 
 $ImageName = $_POST['image_tag'];

 $DignidadCodigo = $_POST['dignidad_codigo'];

 $Genero = $_POST['genero'];

 $NumeroJunta = $_POST['numero_junta'];

	
                                                     
$nuevo  = str_replace(' ', '_', $ImageName);

 $ImagePath = "imagenes/$nuevo.png";
 
 $ServerURL = "http://134.209.13.62/$ImagePath";


 
 $InsertSQL = "INSERT INTO imageupload (image_path,image_name,dignidad_codigo,genero,numeroJunta) values('$ServerURL','$ImageName','$DignidadCodigo','$Genero','$NumeroJunta')";
 
 if(mysqli_query($conn, $InsertSQL)){
 
 file_put_contents($ImagePath,base64_decode($ImageData));
 
 echo "Completado";
 }
 
 mysqli_close($conn);
 }else{
 echo "No completado";
 }
 
?>