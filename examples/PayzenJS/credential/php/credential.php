<?php
//Get json
$json = json_decode(file_get_contents('php://input'), true);

//Sort params by key
ksort($json); 

//Generate string to sign
$signature_content = "";
foreach ($json as $key => $value)
{
	if(substr($key,0,5) == 'vads_') {
	    $signature_content .= $value."+";
	}
}
//Add private key
$signature_content .= "00000000000";

echo '{"signature":"'.sha1($signature_content).'"}';
?>
