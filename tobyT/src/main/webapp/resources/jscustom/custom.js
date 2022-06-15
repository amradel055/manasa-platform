firebase.initializeApp({
  apiKey: "AIzaSyDNSng2f5Ayub8_5jGG_g2irSHq_qLG0M8",
    authDomain: "isag-5a27c.firebaseapp.com",
    databaseURL: "https://igma-5a27c.firebaseio.com",
    projectId: "isag-5a27c",
    storageBucket: "isag-5a27c.appspot.com",
    messagingSenderId: "196855886898",
    appId: "1:196855886898:web:2794a0cceb6a1b7816382b",
    measurementId: "G-09P1D2GWBF"
});


$(document).ready(function () {

    document.getElementById("mySidenav").style.width = "260px";
    document.getElementById("content").style.width = "80%";
    document.getElementById("closebtn").style.display = "block";
    document.getElementById("openbtn").style.display = "none";
    document.getElementById("closebtn1").style.display = "block";
    document.getElementById("openbtn1").style.display = "none";


});
function openNav() {
    document.getElementById("mySidenav").style.width = "260px";
    document.getElementById("content").style.width = "80%";
    document.getElementById("closebtn").style.display = "block";
    document.getElementById("openbtn").style.display = "none";
    document.getElementById("closebtn1").style.display = "block";
    document.getElementById("openbtn1").style.display = "none";

}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("content").style.width = "100%";
    document.getElementById("openbtn").style.display = "block";
    document.getElementById("closebtn").style.display = "none";
    document.getElementById("openbtn1").style.display = "block";
    document.getElementById("closebtn1").style.display = "none";
}

$(document).ready(function () {

    $(".viewButton").click(function () {

        $(".firstset").slideToggle();

    });

});
