//Scrollable lists
//==============================================
function initListSizes() {
    var verticals = $(".list-vertical");
    var horizontals = $(".list-horizontal");
    verticals.css("max-height", verticals.height());
    horizontals.css("max-width", horizontals.width());
}
//==============================================


//Overlay Panel
//==============================================
var overlay = $("#overlay");
var overlayOkButtons = overlay.find(".button-ok");
var overlayInput = $("#overlay-input");
var overlayNewGroup = $("#overlay-newGroup");
var overlayDeleteGroup = $("#overlay-deleteGroup");
var overlayAffirmationGroup = $("#overlay-affirmationGroup");
var overlayInputGroup = $("#overlay-inputGroup");
var overlayConfirmGroup = $("#overlay-confirmGroup");
var overlayGroupName = $(".overlay-groupName");
var overlayErrorGroup = $("#overlay-errorGroup");


function closeOverlay() {
    overlay.hide();
    overlayInput.val("");
    overlayInputGroup.hide();
    overlayAffirmationGroup.hide();
    overlayConfirmGroup.hide();
    overlayNewGroup.hide();
    overlayDeleteGroup.hide();
    overlayErrorGroup.hide();
}

function showErrorOverlay() {
    overlayErrorGroup.show();
    overlayConfirmGroup.show();
    overlay.show();
}

function showCreateGroupOverlay() {
    overlayOkButtons.attr("onclick", "createGroup()");
    overlayNewGroup.show();
    overlayInputGroup.show();
    overlay.show();
}

function showDeleteGroupOverlay(group) {
    setActiveGroupIndex($(group).parent().parent().attr("data-id"));
    overlayOkButtons.attr("onclick", "deleteGroup()");
    overlayGroupName.text(groups[activeGroup].groupName);
    overlayDeleteGroup.show();
    overlayAffirmationGroup.show();
    overlay.show();
}
//==============================================

closeOverlay();
initListSizes();