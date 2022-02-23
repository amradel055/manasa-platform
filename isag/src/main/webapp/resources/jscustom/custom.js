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
