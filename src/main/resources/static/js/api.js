//Send
//==============================================
function sendCommand(JSONCommandString) {
    var url = "http://" + window.location.hostname + "/command/set";
    var success = false;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            jsonData: JSONCommandString
        },
        async: false,
        success: function() {
            success = true;
        }
    });
    return success;
}

function sendCreateGroup(groupName) {
    var url = "http://" + window.location.hostname + "/group/create";
    var success = false;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            groupName: groupName
        },
        async: false,
        success: function() {
            success = true;
        }
    });
    return success;
}

function sendUpdateGroup(groupId, newGroupName) {
    var url = "http://" + window.location.hostname + "/group/update";
    var success = false;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            groupId: groupId,
            groupName: newGroupName
        },
        async: false,
        success: function() {
            success = true;
        }
    });
    return success;
}

function sendDeleteGroup(groupId) {
    var url = "http://" + window.location.hostname + "/group/delete";
    var success = false;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            groupId: groupId
        },
        async: false,
        success: function() {
            success = true;
        }
    });
    return success;
}

function sendAssignModule(moduleId, groupId) {
    var url = "http://" + window.location.hostname + "/stripe/set/group";
    var success = false;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            stripeId: moduleId,
            groupId: groupId
        },
        async: false,
        success: function() {
            success = true;
        }
    });
    return success;
}
//==============================================


//Retrieve
//==============================================
function fetchAllGroups() {
    var url = "http://" + window.location.hostname + "/group/get/all";
    var groups = [];
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        success: function(data) {
            groups = JSON.parse(data).groups;
        }
    });
    return groups;
}

function fetchAllModules() {
    var url = "http://" + window.location.hostname + "/stripe/get/all";
    var modules = [];
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        success: function(data) {
            modules = JSON.parse(data).stripes;
        }
    });
    return modules;
}

function fetchModulesByGroup(groupId) {
    var url = "http://" + window.location.hostname + "/stripe/get/group";
    var modules = [];
    $.ajax({
        url: url,
        type: "POST",
        data: {
            groupId: groupId
        },
        async: false,
        success: function(data) {
            modules = JSON.parse(data).stripes;
        }
    });
    return modules;
}

function fetchUnassignedModules() {
    var url = "http://" + window.location.hostname + "/stripe/get/unassigned";
    var modules = [];
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        success: function(data) {
            modules = JSON.parse(data).stripes;
        }
    });
    return modules;
}

function fetchAllCommands() {
    var url = "http://" + window.location.hostname + "/command/get/all";
    var commands = [];
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        success: function(data) {
            commands = JSON.parse(data).commands;
        }
    });
    return commands;
}

function fetchCommandByGroup(groupId) {
    var url = "http://" + window.location.hostname + "/command/get/group";
    var command = null;
    $.ajax({
        url: url,
        type: "POST",
        data: {
            groupId: groupId
        },
        async: false,
        success: function(data) {
            command = JSON.parse(data).command;
        }
    });
    return command;
}
//==============================================
