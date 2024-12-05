<?php 
// Set Headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header('Content-Type: application/json');

// Set Directory
$mediaDir = 'media';

if (!is_dir($mediaDir)) {
    mkdir($mediaDir);
}

// Check Request Type
if ($_SERVER['REQUEST_METHOD'] == "POST") {
    // Get The Video
    $mediaFile = $_FILES['media'];
    $tmp = $mediaFile['tmp_name'];
    $extension = pathinfo( $mediaFile['name'], PATHINFO_EXTENSION );
    $dim = strpos($mediaFile['type'], "image") === 0 ? getimagesize($tmp) : "";
    $mediaName = str_replace(".".$extension, "", $mediaFile['name']). "__" . date("Ymd_his") . ((strpos($mediaFile['type'], "image") === 0) ? "__[".$dim[0].", ".$dim[1]."]__." : "__.") . $extension;
    $fullPath = $mediaDir . '/' . $mediaName;

    move_uploaded_file($tmp, $fullPath);

    echo json_encode(array(
        "media_url" => $fullPath
    ));
} else {
    echo json_encode(array(
        'message' => "غير مسموح بالوصول الى هنا"
    ));
}