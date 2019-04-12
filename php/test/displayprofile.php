<?php
//host
$host = "134.209.13.62";
//user name
$username = "votacion1";
//database password
$pwd = "vOta-12.@9";
//database name.
$db = "votaciones2019";

$con=mysqli_connect($host,$username,$pwd,$db) or die("Unable to Connect");

if(mysqli_connect_error($con))
{
	echo "Failed to connect";
}

$query=mysqli_query($con,"SELECT * FROM profile");

if($query)
{
while($row=mysqli_fetch_array($query))
	{
	$flag[]=$row;
	}
print(json_encode($flag));
}
mysqli_close($con);
?>


