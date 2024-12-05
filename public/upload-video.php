<?php 
// Set Headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header('Content-Type: application/json');

// Set Directory
$videoDir = 'videos';

if (!is_dir($videoDir)) {
    mkdir($videoDir);
}

// Check Request Type
if ($_SERVER['REQUEST_METHOD'] == "POST") {
    // Get The Video
    $videoFile = $_FILES['LessonVideo'];
    $tmp = $videoFile['tmp_name'];
    $extension  = pathinfo( $videoFile['name'], PATHINFO_EXTENSION );
    $videoName = str_replace(".".$extension, "", $videoFile['name']). "_" . date("Ymd_his") . "." . $extension;
    $fullPath = $videoDir . '/' . $videoName;

    move_uploaded_file($tmp, $fullPath);

    echo json_encode(array(
        "video_url" => $fullPath
    ));
} else {
    echo json_encode(array(
        'message' => "غير مسموح بالوصول الى هنا"
    ));
}