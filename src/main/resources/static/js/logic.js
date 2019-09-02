//ColorPicker
//==============================================
var colorPickerWidth = $("#color-picker-container").width();

var colorPicker = new iro.ColorPicker('#color-picker-container', {
    width: colorPickerWidth
});
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
    seconds = inputSeconds.val();
    minutes = inputMinutes.val();
    hours = inputHours.val();
    days = inputDays.val();
}

function getTotalSeconds() {
    return seconds
        + minutes * 60
        + hours * 60 * 60
        + days * 60 * 60 * 24;
}

function updateDuration() {
    setDurationWithSeconds(getTotalSeconds());
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
    if(getTotalSeconds() / (60 * 60 * 24) > 0) {
        days --;
        updateDuration()
    }
}
//==============================================