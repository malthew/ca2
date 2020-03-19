let h1content = document.querySelector("#h1content");
let h3content = document.querySelector("#h3content");
let content = document.querySelector("#content");
let linksDiv = document.querySelector("#linksDiv");

let personButton = document.querySelector("#Person");
let addressButton = document.querySelector("#Address");
let hobbyButton = document.querySelector("#Hobby");
let phoneButton = document.querySelector("#Phone");

function makeOptions(method, body) {
    var opts = {
        method: method,
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        }
    }
    if (body) {
        opts.body = JSON.stringify(body);
    }
    return opts;
}

/*---------------------------------------------*/
/*------------- Begin Create Buttons ----------*/
/*---------------------------------------------*/
/*---------------------------------------------*/
/*--------- Begin Create Button Person --------*/
/*---------------------------------------------*/
personButton.onclick = function (e) {
    e.preventDefault();
    clearDivs();
    createPersonButtons();
};
function createPersonButtons() {
    let b1 = "<button type=\"submit\" onclick=\"createInputFieldsFindPerson()\">Find Person</button>";
    let b2 = "<button type=\"submit\" onclick=\"createInputFieldsCreatePerson()\">Create Person</button>";
    linksDiv.innerHTML = b1 + b2;
}

function createInputFieldsCreatePerson() {
    let form = "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<label for=\"email\">Email:</label><br>" +
            "<input type=\"text\" id=\"email\" name=\"email\"><br>" +
            "<label for=\"pid\">Person ID:</label><br>" +
            "<input type=\"text\" id=\"pid\" name=\"pid\" ><br><br>" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"createNewUser()\">Create Person</button>" +
            "</form> ";
    let response = "<p id=\"creation_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsFindPerson() {
    let form = "<form>" +
            "<label for=\"pid\">Person ID:</label><br>" +
            "<input type=\"text\" id=\"pid\" name=\"pid\" ><br><br>" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"findPerson()\">Find Person</button>" +
            "</form> ";
    let response = "<p id=\"find_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

/*---------------------------------------------*/
/*--------- End Create Button Person ----------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*-------- Begin Create Button Address --------*/
/*---------------------------------------------*/

addressButton.onclick = function (e) {
    e.preventDefault();
    clearDivs();
    createAddressButtons();
};
function createAddressButtons() {
    let b1 = "<button type=\"submit\" id=\"findAddress\">Find Address</button>";
    let b2 = "<button type=\"submit\" id=\"createAddress\">Create Address</button>";
    linksDiv.innerHTML = b1 + b2;
}

/*---------------------------------------------*/
/*-------- End Create Button Address ----------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*--------- Begin Create Button Hobby ---------*/
/*---------------------------------------------*/

hobbyButton.onclick = function (e) {
    e.preventDefault();
    clearDivs();
    createHobbyButtons();
};
function createHobbyButtons() {
    let b1 = "<button type=\"submit\" id=\"findHobby\">Find Hobby</button>";
    let b2 = "<button type=\"submit\" id=\"createHobby\">Create Hobby</button>";
    linksDiv.innerHTML = b1 + b2;
}

/*---------------------------------------------*/
/*--------- End Create Button Hobby -----------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*---------- Begin Create Button Phone --------*/
/*---------------------------------------------*/

phoneButton.onclick = function (e) {
    e.preventDefault();
    clearDivs();
    createPhoneButtons();
};
function createPhoneButtons() {
    let b1 = "<button type=\"submit\" id=\"findPhone\">Find Phone</button>";
    let b2 = "<button type=\"submit\" id=\"createPhone\">Create Phone</button>";
    let b3 = "<button type=\"submit\" onclick=\"createInputFieldsFindPhoneByName()\">Find Phones By Name</button>";
    linksDiv.innerHTML = b1 + b2 + b3;
}

function createInputFieldsFindPhoneByName() {
    let form = "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<button type=\"button\" onclick=\"findPhoneByName()\" class=\"btn btn-primary\">Find Phones</button>" +
            "</form> ";
    linksDiv.innerHTML = form;
}

/*---------------------------------------------*/
/*---------- End Create Button Phone ----------*/
/*---------------------------------------------*/
/*---------------------------------------------*/
/*------------- End Create Buttons ------------*/
/*---------------------------------------------*/




/*---------------------------------------------*/
/*------------ Begin Create Person ------------*/
/*---------------------------------------------*/

const createNewUser = function () {
    let firstname = document.getElementById("fname").value;
    let lastname = document.getElementById("lname").value;
    let email = document.getElementById("email").value;
    let personid = document.getElementById("pid").value;
    let newUser = {"firstName": firstname, "lastName": lastname, "email": email, "personid": personid};

    let options = makeOptions('POST', newUser);
    fetch('/api/person/createperson', options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("creation_Response").innerHTML = data.msg;
                } else if (data.code === 500) {
                    document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("creation_Response").innerHTML = firstname + " was added as " + data.id;
                }
            });
};

/*---------------------------------------------*/
/*------------- End Create Person -------------*/
/*---------------------------------------------*/



/*---------------------------------------------*/
/*------------- Begin Find Person -------------*/
/*---------------------------------------------*/

const findPerson = function () {
    let personid = document.getElementById("pid").value;
    let url = "/ca2/api/person/" + personid;
    fetch(url)
            .then(res => res.json())
            .then(data => {
//                if (data.status) {
//                    console.error('Fail:', data);
                if (data.code === 400) {
                    document.getElementById("find_Response").innerHTML = "<br><p>No person was found with this ID</p>";
                } else if (data.code === 500) {
                    document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log("data", data);
                    linksDiv.innerHTML = personTable(data);
                }
            });
}

function personTable(person) {
//    //var tableinfo = person.map(x => `<tr><td>  ${x.personid} </td><td> ${x.email} </td><td> ${x.firstName} </td>
//    //<td>  ${x.lastName} </tr>`);
    var tableinfo = "<table id=\"indextable\" class=\"table\">" +
            "<tr><th>Person ID</th>" +
            "<th>Email</th>" +
            "<th>First Name</th>" +
            "<th>Last Name</th></tr>" +
            "<tr><td>" + person.personid + "</td><td>" + person.email + "</td><td>" + person.firstName + "</td>" +
            "<td>" + person.lastName + "</tr></table>";
    return tableinfo;

}

/*---------------------------------------------*/
/*-------------- End Find Person --------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------ Begin Find Address -------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*------------- End Find Address --------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------- Begin Find Hobby --------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*-------------- End Find Hobby ---------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------- Begin Find Phone --------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*-------------- End Find Phone ---------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*----------- Begin Find Phone By Name --------*/
/*---------------------------------------------*/



/*---------------------------------------------*/
/*----------- End Find Phone By Name ----------*/
/*---------------------------------------------*/

function phonesToTable(phones) {
    var tableinfo = phones.map(x => `<tr><td>  ${x.number} </td><td> ${x.description} </td></tr>`);

    tableinfo.unshift("<table id=\"indextable\" class=\"table\">\n\
    <tr><th>Phone Number</th>\n\
    <th>Description</th></tr>");

    tableinfo.push("</table>");
    return tableinfo.join('');
}

function findPhoneByName() {
    let firstName = document.querySelector("#fname").value;
    let lastName = document.querySelector("#lname").value;
    let url = "/ca2/api/person/phones/" + firstName + "/" + lastName;
    fetch(url)
            .then(res => res.json())
            .then(data => {
                console.log("data", data);
                //if the data has property "code" it's en error and we can't pass it to the function
                if (data.hasOwnProperty("code")) {
                    content.innerHTML = JSON.stringify(data);
                } else {
                    content.innerHTML = phonesToTable(data);
                }
            });
}

/*---------------------------------------------*/
/*-------------- Begin Add Hobby --------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*--------------- End Add Hobby ---------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*--------------- Begin Add City --------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*---------------- End Add City ---------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*------------- Begin Get ZipCodes ------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*-------------- End Get ZipCodes -------------*/
/*---------------------------------------------*/



/*---------------------------------------------*/
/*-------------- Begin Clear Divs -------------*/
/*---------------------------------------------*/

function clearDivs() {
    h1content.innerHTML = "";
    h3content.innerHTML = "";
    content.innerHTML = "";
    linksDiv.innerHTML = "";
}

/*---------------------------------------------*/
/*--------------- End Clear Divs --------------*/
/*---------------------------------------------*/