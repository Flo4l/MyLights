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
    setActiveColorIndex($(".color").length - 1);
}

function addColorFromJSON(JSONColor) {
    var rgbColor = "rgb(" + JSONColor.red + ", " +
                            JSONColor.green + ", " +
                            JSONColor.blue + ")";
    var color = getHTMLColor(rgbColor);
    colorAdder.before(color);
    setActiveColorIndex($(".color").length - 1);
}

function removeColor(color) {
    var c = $(color).parent();
    c.addClass("deleted");
    if(activeColor === colors.length - 1) {
        setActiveColorIndex(activeColor - 1);
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
    setActiveColorIndex(0);
}

function setActiveColor(color) {
    var c = $(color);
    if(!c.hasClass("deleted")) {
        setActiveColorIndex(parseInt(c.attr("data-id")));
    }
}

function setActiveColorIndex(colorIndex) {
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
        "                col-lg-1" +
        "                col-sm-2\"\n" +
        "        data-id=\"" + id + "\"\n" +
        "        style=\"background-color: " + colorString + "\"\n" +
        "        onclick=\"setActiveColor(this)\">\n" +
        "       <div class=\"color-remove\n" +
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

function setIsFading(boolean) {
    isFading = boolean;
    togglerFadeing.prop("checked", boolean);
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
    var mode = isFading ? 'f' : ($(".color").length < 2) ? 's' : 'm';
    return {
        "command":
        {
            "mode": mode,
            "secondsToNextColor": getTotalSeconds(),
            "groupId": groups[activeGroup].groupId,
            "colors": colors
        }
    }
}
//==============================================


//Module list
//==============================================
var activeModule = 0;
var modules = [];

function clearModuleList() {
    $(".module").remove();
}

function updateModuleList() {
    modules = fetchModulesByGroup(groups[activeGroup].groupId);
    clearModuleList();
    modules.forEach(function(val) {
       addModuleFromJSON(val);
    });
}

function updateModule() {
    var moduleName = overlayInput.val();
    var moduleId = modules[activeModule].stripeId;
    closeOverlay();
    if(!sendUpdateModule(moduleId, moduleName)) {
        showErrorOverlay();
    }
    updateModuleList();
}

function unassignModule() {
    var moduleId = modules[activeModule].stripeId;
    closeOverlay();
    if(!sendUnassignModule(moduleId)) {
        showErrorOverlay();
    }
    updateModuleList();
}

function addModuleFromJSON(JSONModule) {
    var mod = getHTMLModule(JSONModule.stripeName);
    $("#addModule").before(mod);
}

function getHTMLModule(moduleName) {
    var moduleId = $(".module").length;
    return "<div data-id=\"" + moduleId + "\"\n" +
        "         class=\"box\n" +
        "                module\n" +
        "                row\">\n" +
        "        <div class=\"col-lg-9\n" +
        "                    col-sm-12\n" +
        "                    module-name\n" +
        "                    p-lg-0\n" +
        "                    p-sm-1\n" +
        "                    order-sm-last\n" +
        "                    order-lg-first\n" +
        "                    mt-sm-0\n" +
        "                    mt-lg-3\">\n" +
        "            " + moduleName + "\n" +
        "        </div>\n" +
        "        <div class=\"d-block " +
        "                     d-lg-none\">\n" +
        "            <div class=\"box\n" +
        "                        btn-remove\n" +
        "                        col-sm-6\"\n" +
        "                 onclick=\"showUnassignModuleOverlay(this)\">\n" +
        "                <img src=\"/img/remove.svg\">\n" +
        "            </div>\n" +
        "            <div class=\"box\n" +
        "                        btn-rename\n" +
        "                        col-sm-6\" " +
        "                 onclick=\"showUpdateModuleOverlay(this)\">\n" +
        "                <img src=\"/img/pencil.svg\">\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"d-none " +
        "                     d-lg-block\n" +
        "                     col-lg-3" +
        "                     p-0\">\n" +
        "            <div class=\"box\n" +
        "                        btn-remove\n" +
        "                        pb-lg-0\n" +
        "                        pb-sm-3\"\n" +
        "                 onclick=\"showUnassignModuleOverlay(this)\">\n" +
        "                <img src=\"/img/remove.svg\">\n" +
        "            </div>\n" +
        "            <div class=\"box\n" +
        "                        btn-rename\n" +
        "                        pb-lg-1\n" +
        "                        pb-sm-2\" onclick=\"showUpdateModuleOverlay(this)\">\n" +
        "                <img src=\"/img/pencil.svg\">\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>";
}
//==============================================


//Assign module List
//==============================================
var unassignedModules = [];

function clearUnassignedModuleList() {
    $(".module-unassigned").remove();
}

function loadUnassignedModules() {
    unassignedModules = fetchUnassignedModules();
    clearUnassignedModuleList();
    unassignedModules.forEach(function(val) {
       var mod = getHTMLUnassignedModule(val.stripeName);
       $("#assign-modulelist").append(mod);
    });
}

function checkModule(mod) {
    var check = $(mod).find("[data-id=check]");
    if(check.hasClass("unchecked")) {
        check.removeClass("unchecked");
        check.addClass("checked");
    } else {
        check.removeClass("checked");
        check.addClass("unchecked");
    }
}

function assignModules() {
    closeOverlay();
    $(".checked").each(function(index, val) {
        var num = parseInt($(val).parent().parent().attr("data-id"));
        var moduleId = unassignedModules[num].stripeId;
        var groupId = groups[activeGroup].groupId;
        if(!sendAssignModule(moduleId, groupId)) {
            showErrorOverlay();
        }
    });
    updateModuleList();
}

function getHTMLUnassignedModule(moduleName) {
    var moduleId = $(".module-unassigned").length;
    return "<div data-id=\"" + moduleId + "\"\n" +
        "        class=\"module-unassigned\n" +
        "                row\n" +
        "                box\"\n" +
        "        onclick=\"checkModule(this)\">\n" +
        "       <div class=\"col-2\n" +
        "                    p-1\n" +
        "                    ml-2\n" +
        "                    mt-sm-0\n" +
        "                    mt-lg-2\">\n" +
        "           <img src=\"/img/accept.svg\"\n" +
        "                data-id=\"check\"\n" +
        "                class=\"unchecked\">\n" +
        "       </div>\n" +
        "       <div class=\"module-name\n" +
        "                    headline\n" +
        "                    col-9\n" +
        "                    py-2\n" +
        "                    px-0\">\n" +
        "           " + moduleName + "\n" +
        "       </div>\n" +
        "   </div>";
}
//==============================================


//Group list
//==============================================
var activeGroup = 0;
var groups = [];
var command = [];

function loadGroupValues() {
    command = fetchCommandByGroup(groups[activeGroup].groupId);
    $("#group-name-headline").text(groups[activeGroup].groupName);
    if(command == null) {
        setDurationWithSeconds(1);
        clearColorList();
        colors = [];
        addColor();
    } else {
        setDurationWithSeconds(command.secondsToNextColor);
        setIsFading(command.mode == "f");
        colors = command.colors;
    }
    updateModuleList();
    updateUIColorList();
}

function setActiveGroup(group) {
    if(!$(group).hasClass("deleted")) {
        setActiveGroupIndex(parseInt($(group).attr("data-id")));
    }
}

function setActiveGroupIndex(index) {
    activeGroup = index;
    var g = $(".group[data-id=" + activeGroup + "]");
    $(".group").removeClass("group-highlight");
    g.addClass("group-highlight");
    loadGroupValues();
}

function updateUIGroupList() {
    clearGroupList();
    groups.forEach(function(val) {
        addGroupFromJSON(val);
    });
    setActiveGroupIndex(0);
}

function clearGroupList() {
    $(".group").remove();
}

function addGroupFromJSON(JSONGroup) {
    var group = getHTMLGroup(JSONGroup.groupName);
    $("#addGroup").before(group);
    setActiveGroupIndex($(".group").length - 1);
}

function createGroup() {
    var groupName = overlayInput.val();
    closeOverlay();
    if(sendCreateGroup(groupName)) {
        groups = fetchAllGroups();
        updateUIGroupList();
        setActiveGroupIndex($(".group").length - 1);
    } else {
        showErrorOverlay();
    }
}

function updateGroup() {
    var groupName = overlayInput.val();
    var groupId = groups[activeGroup].groupId;
    var active = activeGroup
    closeOverlay();
    if(sendUpdateGroup(groupId, groupName)) {
        groups = fetchAllGroups();
        updateUIGroupList();
        setActiveGroupIndex(active);
    } else {
        showErrorOverlay();
    }
}

function deleteGroup() {
    closeOverlay();
    if(sendDeleteGroup(groups[activeGroup].groupId)) {
        groups = fetchAllGroups();
        updateUIGroupList();
        if(activeGroup >= $(".group").length) {
            setActiveGroupIndex($(".group").length - 1);
        }
        else {
            setActiveGroupIndex(activeGroup);
        }
    } else {
        showErrorOverlay();
    }
}

function getHTMLGroup(groupName) {
    var id = $(".group").length;
    return "<div data-id=\"" + id + "\"\n" +
        "         class=\"box\n" +
        "                group\n" +
        "                row\"\n" +
        "         onclick=\"setActiveGroup(this)\">\n" +
        "        <div class=\"col-lg-9\n" +
        "                    col-sm-12\n" +
        "                    group-name\n" +
        "                    order-sm-last\n" +
        "                    order-lg-first\n" +
        "                    p-lg-0\n" +
        "                    p-sm-1\n" +
        "                    mt-sm-0\n" +
        "                    mt-lg-3\">\n" +
        "                " + groupName + "\n" +
        "        </div>\n" +
        "        <div class=\"d-block " +
        "                     d-lg-none\">\n" +
        "            <div class=\"box\n" +
        "                        btn-remove\n" +
        "                        col-sm-6\"\n" +
        "                 onclick=\"showDeleteGroupOverlay(this)\">\n" +
        "                <img src=\"/img/remove.svg\">\n" +
        "            </div>\n" +
        "            <div class=\"box\n" +
        "                        btn-rename\n" +
        "                        col-sm-6\" " +
        "                 onclick=\"showUpdateGroupOverlay(this)\">\n" +
        "                <img src=\"/img/pencil.svg\">\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"d-none " +
        "                     d-lg-block\n" +
        "                     col-lg-3" +
        "                     p-0\">\n" +
        "            <div class=\"box\n" +
        "                        btn-remove\n" +
        "                        pb-lg-0\n" +
        "                        pb-sm-3\"\n" +
        "                 onclick=\"showDeleteGroupOverlay(this)\">\n" +
        "                <img src=\"/img/remove.svg\">\n" +
        "            </div>\n" +
        "            <div class=\"box\n" +
        "                        btn-rename\n" +
        "                        pb-lg-1\n" +
        "                        pb-sm-2\" onclick=\"showUpdateGroupOverlay(this)\">\n" +
        "                <img src=\"/img/pencil.svg\">\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>";
}

//==============================================


//JQuery events
//==============================================
$(document).keyup(function(e) {
    //Enter
    if (e.keyCode === 13) {
        if(overlay.is(":hidden")) {
            $("#button-send-command").click();
        } else {
            overlayOkButtons[0].click();
        }
    }
    //Right
    if(e.keyCode === 39) {
        if(overlay.is(":hidden")) {
            setActiveColorIndex((activeColor + 1) % colors.length);
        }
    }
    //Left
    if(e.keyCode === 37) {
        if(overlay.is(":hidden")) {
            var nextColor = (activeColor === 0) ? colors.length - 1 : activeColor - 1;
            setActiveColorIndex(nextColor);
        }
    }
    //Space
    if(e.keyCode === 32) {
        if(overlay.is(":hidden")) {
            colorAdder.click();
        }
    }
    //Escape
    if(e.keyCode === 27) {
        if(overlay.is(":visible")) {
            overlayDeclineButtons.click();
        }
    }
});

$(document).scroll(function() {
   overlay.offset({top: $(window).scrollTop(),left: $(window).scrollLeft()});
});
//==============================================


//Initialising panel data
//==============================================
groups = fetchAllGroups();
updateUIGroupList();
loadGroupValues();
//==============================================