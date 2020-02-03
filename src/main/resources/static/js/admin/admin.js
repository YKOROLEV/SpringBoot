import {RestUserService} from "./rest/rest-user.js";

const userRestService = new RestUserService();

let stateCreateNewUser = false;

let userTable;
let userData;

window.addEventListener('load', function () {
    userTable = $("#userTable > tbody");
    userTableRefresh();
});

window.addEventListener('click', function (event) {
    if (event.target.id === 'table-action-button') {
        let userId = event.target.dataset.userId;
        let type = event.target.dataset.type;
        let tableRowId = event.target.dataset.rowId;

        if (type === 'edit') {
            let user = JSON.parse(decodeURIComponent(event.target.dataset.user));
            userActionEdit(user);
        } else if (type === 'delete') {
            let dialogDeleteConfirm = $("#dialogUserTableDelete");
            let login = userData
                .filter(user => user.id.toString() === userId)
                .map(user => user.login);

            dialogDeleteConfirm.find('.modal-body > p').html('Delete user under <mark>' + login + '</mark> login?');
            dialogDeleteConfirm.find('#button-confirm-delete').attr('data-user-id', userId);
            dialogDeleteConfirm.find('#button-confirm-delete').attr('data-row-id', tableRowId);
            dialogDeleteConfirm.modal('show');
        }
    } else if (event.target.id === 'button-confirm-delete') {
        let dialogDeleteConfirm = $("#dialogUserTableDelete");
        let userId = event.target.dataset.userId;
        let tableRowId = event.target.dataset.rowId;

        userDelete(userId, tableRowId);
        dialogDeleteConfirm.modal('hide');
    } else if (event.target.id === 'button-user-create') {
        userCreate();
    }
});

// при переходе на таб 'Users table' запросить новые данные и вывести их
$('.nav-tabs').on('show.bs.tab', function (event) {
    if (event.target.text === 'Users table') {
        if (stateCreateNewUser) {
            userTableRefresh();
            stateCreateNewUser = false;
        }
    }
});

function userTableAddItem(user) {
    userTable.append(`<tr id="table-row-index-` + user.id + `" >
                            <th scope="row">` + user.id + `</th>
                            <td>` + getRoles(user) + `</td>
                            <td>` + user.login + `</td>
                            <td>` + user.name + `</td>
                            <td>
                                <div class="btn-group w-100" role="group">
                                    <button id="table-action-button" type="button" class="btn btn-warning" data-user="` + encodeURIComponent(JSON.stringify(user)) + `" data-type="edit">Edit</button>
                                    <button id="table-action-button" type="button" class="btn btn-danger" data-row-id="#table-row-index-` + user.id + `" data-user-id="` + user.id + `" data-type="delete" ` + ((principal === user.login) ? 'disabled' : '') + `>Delete</button>
                                </div>
                            </td>
                        </tr>`);
}

function userTableRefresh() {
    userTable.empty();
    userRestService.getAll()
        .then(response => response.json())
        .then(users => {
            userData = users;
            users.forEach(user => {
                userTableAddItem(user);
            });
        });
}

function userActionEdit(user) {
    let dialog = $('#dialogUserUpdate');

    let inputLogin = $('#inputUpdateLogin');
    let inputName = $('#inputUpdateName');
    let inputPassword = $('#inputUpdatePassword');
    let inputRole = $('#inputUpdateRole');

    inputLogin.val(user.login);
    inputName.val(user.name);

    let hasAdmin = user.roles
        .map(role => role.name)
        .some(role => role === 'ADMIN');

    if (hasAdmin) {
        inputRole.val("ADMIN").change();
    } else {
        inputRole.val("USER").change();
    }

    dialog.find('.modal-title').text('Update ' + user.login);
    dialog.modal('show');
}

function userDelete(id, tableRowId) {
    userRestService.deleteById(id).then(response => {
        if (response.ok) {
            $(tableRowId).fadeTo('slow', 0.4, function () {
                $(this).remove();
            });
        }
    });
}

function userCreate() {
    let informationContainer = $('#informationCreateUserContainer');
    let information = $('#informationCreateUser');
    let login = $('#inputNewLogin').val().trim();
    let name = $('#inputNewName').val().trim();
    let password = $('#inputNewPassword').val().trim();
    let role = $('#inputNewRole').val().trim();

    if (login.length === 0 || name.length === 0 || password.length < 3) {
        information.removeClass('alert-success');
        information.addClass('alert-danger');
        information.html('<strong>Attention</strong> some fields are incorrect or empty.');
        informationContainer.css('display', 'block');
        return;
    }

    let user = {
        login: login,
        name: name,
        password: password,
        roles: [
            {
                name: role
            }
        ]
    };

    userRestService.create(user).then(response => {
        if (response.status === 400) {
            information.removeClass('alert-success');
            information.addClass('alert-danger');
            information.html('<strong>Attention</strong> an error occurred while creating the record.');
            informationContainer.css('display', 'block');
        } else if (response.ok) {
            response.json().then(user => {
                information.removeClass('alert-danger');
                information.addClass('alert-success');
                information.html('<strong>Attention</strong> user added to database by id ' + user.id);
                informationContainer.css('display', 'block');

                stateCreateNewUser = true;
            });
        }
    });
}

function getRoles(user) {
    return user.roles.map(role => role.name).join(', ');
}