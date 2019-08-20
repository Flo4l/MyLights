
function sendCommand(jsonCommand) {
    var jsonString = JSON.stringify(jsonCommand);
    $.post("http://" + window.location.hostname + "/command/set", {
        jsonData: jsonString
    },
    function(data, status) {

    });
}

function getCommandAsJson() {
    var groupId = 1;
    var mode = "s";
    var interval = parseInt($("#speed").val());
    var color = getColorAsJson(parseInt($("#red").val()),
                                parseInt($("#green").val()),
                                parseInt($("#blue").val()));
    var command = {
        "command": {
            "mode" : mode,
            "secondsToNextColor" : interval,
            "groupId" : groupId,
            "colors": []
        }
    };
    //TODO for each color
    command.command.colors.push(color);
    return command;
}

function getColorAsJson(red, green, blue) {
    return {
        "red" : red,
        "green" : green,
        "blue" : blue
    };
}

function updateEvent() {
    sendCommand(getCommandAsJson());
}