<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=utf-8");

$c = new mysqli("localhost", "root", "", "cinelogdb");
if ($c->connect_errno) {
    $info = array(
        "result" => "ERROR",
        "message" => $c->connect_error
    );
    echo json_encode($info);
    die();
}

$sql = "SELECT id, title, genre, platform, age_rating, release_year, rating, url, trailer_url, review, is_available, has_spoilers, is_favorite FROM movies ORDER BY id DESC";
$stmt = $c->prepare($sql);
$stmt->execute();
$result = $stmt->get_result();
$array = array();

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $array[] = $row;
    }
    echo json_encode(array('result' => 'SUCCESS', 'data' => $array));
} else {
    echo json_encode(array('result' => 'SUCCESS', 'data' => array()));
}

$stmt->close();
$c->close();
?>