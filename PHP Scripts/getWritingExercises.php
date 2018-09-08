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

$result = mysql_query("Select * From writing_exercise");

// check for empty result
if (mysql_num_rows($result) > 0) 
{
    $response["no"] = array();

    while ($row = mysql_fetch_array($result)) 
	{
        $no = array();
        $no["id"] = $row["id"];
        $no["phrase_english"] = $row["phrase_english"];
		$no["hint_english"] = $row["hint_english"];
		$no["correct_answer_english"] = $row["correct_answer_english"];
		$no["phrase_isixhosa"] = $row["phrase_isixhosa"];
		$no["hint_isixhosa"] = $row["hint_isixhosa"];
		$no["correct_answer_isixhosa"] = $row["correct_answer_isixhosa"];
		$no["photo"] = $row["photo"];
		
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