<?php

function IsNullOrEmptyString($s){
    return (!isset($s) || trim($s)==='');
}

/* gets the data from a URL */
function get_data($url) {
	$ch = curl_init();
	$timeout = 5;
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
	$data = curl_exec($ch);
	curl_close($ch);
	return $data;
}

if (isset($argv)) {
    $url    = ($argv[1]);
    $format = $argv[2];
}
else {
    $url    = ($_GET['url']);
 //   $params = str_replace("&#39;", "'", $_GET['params']); 
 //   $params = str_replace(" ", "%20", $params); 
 //   $format = $_GET['format'];
}

if (IsNullOrEmptyString($format))  {
    $format = "application/json"; 
}
 
header("Content-type: " . $format);

//echo $url . "&" . $params; 
if (!IsNullOrEmptyString($url)) {
    //echo get_data($url . "&" . ($params)); 
    echo get_data($url); 
}

?>
