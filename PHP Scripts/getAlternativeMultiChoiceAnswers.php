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

$result = mysql_query("Select * From alt_multichoice_answers");

// check for empty result
if (mysql_num_rows($result) > 0) 
{
    $response["no"] = array();

    while ($row = mysql_fetch_array($result)) 
	{
        $no = array();
        $no["alt_answer_id"] = $row["alt_answer_id"];
		$no["1_isixhosa"] = $row["1_isixhosa"];
		$no["2_isixhosa"] = $row["2_isixhosa"];
		$no["3_isixhosa"] = $row["3_isixhosa"];
		$no["4_isixhosa"] = $row["4_isixhosa"];
		$no["1_english"] = $row["1_english"];
		$no["2_english"] = $row["2_english"];
		$no["3_english"] = $row["3_english"];
		$no["4_english"] = $row["4_english"];

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