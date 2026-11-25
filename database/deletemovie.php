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

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $id = (int) ($_POST['id'] ?? 0);

    if ($id <= 0) {
        echo json_encode(array("result" => "ERROR", "message" => "id is required"));
        die();
    }

    $sql = "DELETE FROM movies WHERE id = ?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("i", $id);

    if ($stmt->execute()) {
        echo json_encode(array("result" => "SUCCESS", "message" => "movie deleted"));
    } else {
        echo json_encode(array("result" => "ERROR", "message" => $stmt->error));
    }
    $stmt->close();
}
$c->close();
?>