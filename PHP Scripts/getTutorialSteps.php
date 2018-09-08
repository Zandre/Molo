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

$result = mysql_query("Select * From tutorial_steps");

// check for empty result
if (mysql_num_rows($result) > 0) 
{
    $response["no"] = array();

    while ($row = mysql_fetch_array($result)) 
	{
        $no = array();
        $no["id"] = $row["id"];
        $no["Tut_ID"] = $row["Tut_ID"];
		$no["layout"] = $row["layout"];
		$no["exercise_id"] = $row["exercise_id"];
		
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