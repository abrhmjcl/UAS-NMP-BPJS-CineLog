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
    $title = $_POST['title'] ?? '';
    $genre = $_POST['genre'] ?? '';
    $platform = $_POST['platform'] ?? '';
    $ageRating = $_POST['age_rating'] ?? 'SU';
    $releaseYear = (int) ($_POST['release_year'] ?? 2024);
    $rating = (float) ($_POST['rating'] ?? 0.0);
    $url = $_POST['url'] ?? '';
    $trailerUrl = $_POST['trailer_url'] ?? '';
    $review = $_POST['review'] ?? '';
    $isAvailable = (int) ($_POST['is_available'] ?? 1);
    $hasSpoilers = (int) ($_POST['has_spoilers'] ?? 0);
    $isFavorite = (int) ($_POST['is_favorite'] ?? 0);

    if ($id <= 0) {
        echo json_encode(array("result" => "ERROR", "message" => "id is required"));
        die();
    }

    $sql = "UPDATE movies SET title=?, genre=?, platform=?, age_rating=?, release_year=?, rating=?, url=?, trailer_url=?, review=?, is_available=?, has_spoilers=?, is_favorite=? WHERE id=?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("ssssidsssiiii", $title, $genre, $platform, $ageRating, $releaseYear, $rating, $url, $trailerUrl, $review, $isAvailable, $hasSpoilers, $isFavorite, $id);

    if ($stmt->execute()) {
        echo json_encode(array("result" => "SUCCESS", "message" => "movie updated"));
    } else {
        echo json_encode(array("result" => "ERROR", "message" => $stmt->error));
    }
    $stmt->close();
}
$c->close();
?>