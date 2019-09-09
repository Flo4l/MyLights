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
var overlayDeclineButtons = overlay.find(".button-decline");
var overlayInput = $("#overlay-input");
var overlayNewGroup = $("#overlay-newGroup");
var overlayUpdateGroup = $("#overlay-updateGroup");
var overlayDeleteGroup = $("#overlay-deleteGroup");
var overlayAffirmationGroup = $("#overlay-affirmationGroup");
var overlayInputGroup = $("#overlay-inputGroup");
var overlayConfirmGroup = $("#overlay-confirmGroup");
var overlayGroupName = $(".overlay-groupName");
var overlayErrorGroup = $("#overlay-errorGroup");
var overlayAssignModuleGroup = $("#overlay-assignModule");
var overlayUpdateModuleGroup = $("#overlay-updateModule");
var overlayModuleName = $(".overlay-moduleName");
var overlayUnassignModuleGroup = $("#overlay-unassignModule");


function closeOverlay() {
    overlay.hide();
    overlayInput.val("");
    overlayInputGroup.hide();
    overlayAffirmationGroup.hide();
    overlayConfirmGroup.hide();
    overlayNewGroup.hide();
    overlayUpdateGroup.hide();
    overlayDeleteGroup.hide();
    overlayErrorGroup.hide();
    overlayAssignModuleGroup.hide();
    overlayUpdateModuleGroup.hide();
    overlayUnassignModuleGroup.hide();
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
    overlayInput.focus();
}

function showUpdateGroupOverlay(group) {
    setActiveGroupIndex($(group).parent().parent().attr("data-id"));
    overlayGroupName.text(groups[activeGroup].groupName);
    overlayOkButtons.attr("onclick", "updateGroup()");
    overlayUpdateGroup.show();
    overlayInputGroup.show();
    overlay.show();
    overlayInput.focus();
}

function showDeleteGroupOverlay(group) {
    setActiveGroupIndex($(group).parent().parent().attr("data-id"));
    overlayGroupName.text(groups[activeGroup].groupName);
    overlayOkButtons.attr("onclick", "deleteGroup()");
    overlayDeleteGroup.show();
    overlayAffirmationGroup.show();
    overlay.show();
}

function showAssignModuleOverlay() {
    loadUnassignedModules();
    overlayOkButtons.attr("onclick", "assignModules()");
    overlayAssignModuleGroup.show();
    overlayAffirmationGroup.show();
    overlay.show();
}

function showUnassignModuleOverlay(mod) {
    activeModule = parseInt($(mod).parent().parent().attr("data-id"));
    overlayModuleName.text(modules[activeModule].stripeName);
    overlayOkButtons.attr("onclick", "unassignModule()");
    overlayUnassignModuleGroup.show();
    overlayAffirmationGroup.show();
    overlay.show();
}

function showUpdateModuleOverlay(mod) {
    activeModule = parseInt($(mod).parent().parent().attr("data-id"));
    overlayModuleName.text(modules[activeModule].stripeName);
    overlayOkButtons.attr("onclick", "updateModule()");
    overlayUpdateModuleGroup.show();
    overlayInputGroup.show();
    overlay.show();
    overlayInput.focus();
}
//==============================================

closeOverlay();
initListSizes();