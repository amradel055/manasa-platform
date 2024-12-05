<?php 
// Set Headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header('Content-Type: application/json');

// Set Directory
$thumbDir = 'thumb';

if (!is_dir($thumbDir)) {
    mkdir($thumbDir);
}

// Check Request Type
if ($_SERVER['REQUEST_METHOD'] == "POST") {
    // Get The Thumb
    $thumbFile = $_FILES['thumb'];
    $tmp = $thumbFile['tmp_name'];
    $extension  = pathinfo( $thumbFile['name'], PATHINFO_EXTENSION );
    $thumbName = str_replace(".".$extension, "", $thumbFile['name']). "_" . date("Ymd_his") . "." . $extension;
    $fullPath = $thumbDir . '/' . $thumbName;

    move_uploaded_file($tmp, $fullPath);

    echo json_encode(array(
        "thumb_url" => $fullPath
    ));
} else {
    echo json_encode(array(
        'message' => "غير مسموح بالوصول الى هنا"
    ));
}