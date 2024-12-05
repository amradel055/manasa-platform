<?php 
// Set Headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header('Content-Type: application/json');

// Set Directory
$docDir = 'documents';

if (!is_dir($docDir)) {
    mkdir($docDir);
}

// Check Request Type
if ($_SERVER['REQUEST_METHOD'] == "POST") {
    // Final Paths
    $docs = array(
        "documents" => array()
    );
    
    // Get The Video
    $docFile = $_FILES['LessonPDFs'];
    
    for ($i = 0; $i < count($docFile['name']); $i++) {
        $tmp = $docFile['tmp_name'][$i];
        $extension  = pathinfo( $docFile['name'][$i], PATHINFO_EXTENSION );
        $docName = str_replace(".".$extension, "", $docFile['name'][$i]). "__" . date("Ymd-his") . "__".($docFile['size'][$i]/1024)."__." . $extension;
        $fullPath = $docDir . '/' . $docName;
        $docs["documents"][] = $fullPath;
        
        move_uploaded_file($tmp, $fullPath);
    }

    echo json_encode($docs);
} else {
    echo json_encode(array(
        'message' => "غير مسموح بالوصول الى هنا"
    ));
}