//ColorPicker
//==============================================
var colorPickerWidth = $("#color-picker-container").width();

var colorPicker = new iro.ColorPicker('#color-picker-container', {
    width: colorPickerWidth
});

colorPicker.on("color:change", updateSelectedColor);
//==============================================


//Color List
//==============================================
var colorList = $("#colorlist");
var colorAdder = $("#addColor");

var activeColor = 0;
var colors = [];

function addColor() {
    var hex = colorPicker.color.hexString;
    var rgb  = colorPicker.color.rgb;
    var color = getHTMLColor(hex);
    colorAdder.before(color);
    colors.push({"red":rgb.r, "green":rgb.g, "blue":rgb.b});
    setActiveIndex($(".color").length - 1);
}

function addColorFromJSON(JSONColor) {
    var rgbColor = "rgb(" + JSONColor.red + ", " +
                            JSONColor.green + ", " +
                            JSONColor.blue + ")";
    var color = getHTMLColor(rgbColor);
    colorAdder.before(color);
    setActiveIndex($(".color").length - 1);
}

function removeColor(color) {
    var c = $(color).parent();
    c.addClass("deleted");
    if(activeColor === colors.length - 1) {
        setActiveIndex(activeColor - 1);
    }
    colors.splice(parseInt(c.attr("data-id")), 1);
    color.parentNode.remove();
    updateColorIds();
}

function updateSelectedColor() {
    var rgb  = colorPicker.color.rgb;
    $(".color[data-id=" + activeColor + "]")
        .css("background-color", colorPicker.color.hexString);
    colors[activeColor] = {"red":rgb.r, "green":rgb.g, "blue":rgb.b};
}

function updateUIColorList() {
    clearColorList();
    colors.forEach(function(val) {
       addColorFromJSON(val);
    });
    setActiveIndex(0);
}

function setActive(color) {
    var c = $(color);
    if(!c.hasClass("deleted")) {
        setActiveIndex(parseInt(c.attr("data-id")));
    }
}

function setActiveIndex(colorIndex) {
    activeColor = colorIndex;
    var c = $(".color[data-id=" + activeColor + "]");
    colorPicker.color.rgbString = c.css("background-color");
    $(".color").removeClass("color-highlight");
    c.addClass("color-highlight");
}

function updateColorIds() {
    $(".color").each(function(index, val) {
        $(val).attr("data-id", index);
    });
}

function clearColorList() {
    $(".color").remove();
}

function getHTMLColor(colorString) {
    var id = $(".color").length;
    return "<div class=\"color\n" +
        "                box" +
        "                col-1\"\n" +
        "        data-id=\"" + id + "\"\n" +
        "        style=\"background-color: " + colorString + "\"\n" +
        "        onclick=\"setActive(this)\">\n" +
        "       <div class=\"remove\n" +
        "                    mt-1\n" +
        "                    p-0\"\n" +
        "            onclick=\"removeColor(this)\">\n" +
        "           <img src=\"/img/remove.svg\">\n" +
        "       </div>\n" +
        "   </div>";
}
//==============================================


//Mode Pane
//==============================================
var togglerFadeing = $("#isFading");

var isFading = false;

function fadingToggled() {
    isFading = togglerFadeing.prop("checked");
}
//==============================================


//Duration Pane
//==============================================
var inputSeconds = $("#seconds");
var inputMinutes = $("#minutes");
var inputHours = $("#hours");
var inputDays = $("#days");

var seconds = 0;
var minutes = 0;
var hours = 0;
var days = 0;

//initialises duration pane
function setDurationWithSeconds(secs) {
    seconds = secs;
    var tmp = 60 * 60 * 24;
    days = Math.floor(seconds / tmp);
    seconds -= days * tmp;
    tmp /= 24;
    hours = Math.floor(seconds / tmp);
    seconds -= hours * tmp;
    tmp /= 60;
    minutes = Math.floor(seconds / tmp);
    seconds -= minutes * tmp;

    inputDays.val(days);
    inputHours.val(hours);
    inputMinutes.val(minutes);
    inputSeconds.val(seconds);
}

function fetchValues() {
    seconds = parseInt(inputSeconds.val());
    minutes = parseInt(inputMinutes.val());
    hours = parseInt(inputHours.val());
    days = parseInt(inputDays.val());
}

function getTotalSeconds() {
    return seconds
        + minutes * 60
        + hours * 60 * 60
        + days * 60 * 60 * 24;
}

function updateDuration() {
    if(getTotalSeconds() <= 0) {
        setDurationWithSeconds(1);
    }
    else {
        setDurationWithSeconds(getTotalSeconds());
    }
}

function incrementSecs() {
    seconds ++;
    updateDuration()
}

function decrementSecs() {
    if(getTotalSeconds() > 1) {
        seconds --;
        updateDuration()
    }
}

function incrementMins() {
    minutes ++;
    updateDuration()
}

function decrementMins() {
    if(getTotalSeconds() / 60 > 0) {
        minutes --;
        updateDuration()
    }
}

function incrementHours() {
    hours ++;
    updateDuration()
}

function decrementHours() {
    if(getTotalSeconds() / (60 * 60) > 0) {
        hours --;
        updateDuration()
    }
}

function incrementDays() {
    days ++;
    updateDuration()
}

function decrementDays() {
    if(getTotalSeconds() / (60 * 60 * 24) >= 1) {
        days --;
        updateDuration()
    }
}

setDurationWithSeconds(1);
//==============================================


//Accept
//==============================================
var acceptButton = $(".button-ok");

function executeCommand() {
    sendCommand(
        JSON.stringify(
            generateJSONCommand()));
}

function generateJSONCommand() {
    var mode = ($(".color").length < 2) ? 's' :
        isFading ? 'f' : 'm';
    var groupId = 1;
    return {
        "command":
        {
            "mode": mode,
            "secondsToNextColor": getTotalSeconds(),
            "groupId": groupId,
            "colors": colors
        }
    }
}
//==============================================


//Module list
//==============================================
var modules = [];
//==============================================


//Group list
//==============================================
var selectedGroup = 0;
var groups = [];
var command = [];

function loadGroupValues() {
    modules = fetchModulesByGroup(groups[selectedGroup].groupId);
    command = fetchCommandByGroup(groups[selectedGroup].groupId);
    setDurationWithSeconds(command.secondsToNextColor);
    colors = command.colors;
    updateUIColorList();
}
//==============================================


//Initialising panel
//==============================================
groups = fetchAllGroups();
loadGroupValues();
//==============================================