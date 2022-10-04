$.getJSON("https://api.github.com/repos/FIRST-Tech-Challenge/FtcRobotController/releases").done(function (json) {
    var downloadURL = json[0].assets[0].browser_download_url;
    $("#data").attr({
        "href": downloadURL
    })
})
