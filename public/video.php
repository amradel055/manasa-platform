<?php

if (isset($_SERVER['HTTP_REFERER'])) {
    $videoSRC = $_REQUEST['videoSRC'];

    // Set Headers
    header("Access-Control-Allow-Origin: *");
    header("Access-Control-Allow-Methods: *");
    header ("Content-type: video/mp4");
    header ("Content-length: " . filesize($videoSRC));
    readfile($videoSRC);
}