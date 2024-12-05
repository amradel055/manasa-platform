<?php 
// Set Headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header('Content-Type: application/json');

// Set Directory
$profileDir = 'profile';

if (!is_dir($profileDir)) {
    mkdir($profileDir);
}

// Check Request Type
if ($_SERVER['REQUEST_METHOD'] == "POST") {
    // Get The Thumb
    $profileImg = $_FILES['profile'];
    $tmp = $profileImg['tmp_name'];
    $extension  = pathinfo( $profileImg['name'], PATHINFO_EXTENSION );
    $profileName = str_replace(".".$extension, "", $profileImg['name']). "_" . date("Ymd_his") . "." . $extension;
    $fullPath = $profileDir . '/' . $profileName;

    move_uploaded_file($tmp, $fullPath);

    echo json_encode(array(
        "pp" => $fullPath
    ));
} else {
    echo json_encode(array(
        'message' => "غير مسموح بالوصول الى هنا"
    ));
}