<?php 
$link = mysql_connect('postgrad.nmmu.ac.za','MoloUser','MoloUser1'); 
if (!$link) 
{ 
	die('Could not connect to MySQL: ' . mysql_error()); 
} 

if (!mysql_select_db('molo.db', $link)) 
{
    echo 'Could not select database';
    exit;
}

$result = mysql_query("Select * From match_exercise");

// check for empty result
if (mysql_num_rows($result) > 0) 
{
    $response["no"] = array();

    while ($row = mysql_fetch_array($result)) 
	{
        $no = array();
        $no["id"] = $row["id"];
		$no["english_1"] = $row["english_1"];
		$no["english_2"] = $row["english_2"];
		$no["english_3"] = $row["english_3"];
		$no["english_4"] = $row["english_4"];
		$no["isixhosa_1"] = $row["isixhosa_1"];
		$no["isixhosa_2"] = $row["isixhosa_2"];
		$no["isixhosa_3"] = $row["isixhosa_3"];
		$no["isixhosa_4"] = $row["isixhosa_4"];


        array_push($response["no"], $no);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} 
else 
{
    //nothing found
    $response["success"] = 0;
    $response["message"] = "No Activities found";
    
    // echo no users JSON
    echo json_encode($response);
}

mysql_close($link); 

?> 