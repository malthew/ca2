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
    let b3 = "<button type=\"submit\" onclick=\"createInputFieldsCreatePersonWithInformation()\">Create Person With All Infomation</button>";
    linksDiv.innerHTML = b1 + b2 + b3;
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

function createInputFieldsCreatePersonWithInformation() {
    //form for adding phones
    let form1 = "<form><label for=\"hname\">Hobby name:</label><br>" +
            "<input type=\"text\" id=\"hname\" name=\"hname\"><br>" +
            "<label for=\"hdescription\">Hobby description:</label><br>" +
            "<input type=\"text\" id=\"hdescription\" name=\"hdescription\"><br>" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"addHobby()\">Add Hobby</button>" +
            "</form> ";
    //form for adding hobbies
    let form2 = "<form><label for=\"pnumber\">Phone number:</label><br>" +
            "<input type=\"text\" id=\"pnumber\" name=\"pnumber\"><br>" +
            "<label for=\"pdescription\">Phone description:</label><br>" +
            "<input type=\"text\" id=\"pdescription\" name=\"pdescription\"><br>" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"addPhone()\">Add Phone</button>" +
            "</form> ";

    let form3 =
            //Information to create Person
            "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<label for=\"email\">Email:</label><br>" +
            "<input type=\"text\" id=\"email\" name=\"email\"><br>" +
            "<label for=\"pid\">Person ID:</label><br>" +
            "<input type=\"text\" id=\"pid\" name=\"pid\" ><br><br>" +
            //Information to create Address
            "<label for=\"street\">Street:</label><br>" +
            "<input type=\"text\" id=\"street\" name=\"street\" ><br><br>" +
            "<label for=\"additionalinfo\">Additional Info about street:</label><br>" +
            "<input type=\"text\" id=\"additionalinfo\" name=\"additionalinfo\" ><br><br>" +
            //information to create CityInfo
            "<label for=\"zipcode\">Zipcode (must be a number):</label><br>" +
            "<input type=\"text\" id=\"zipcode\" name=\"zipcode\" ><br><br>" +
            "<label for=\"city\">City:</label><br>" +
            "<input type=\"text\" id=\"city\" name=\"city\" ><br><br>" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"createPersonWithInformation()\">Create Person (Only click when you have added phones and hobbies</button>" +
            "</form> ";
    let response = "<p id=\"creation_Response\"></p>";
    linksDiv.innerHTML = form1 + form2 + form3 + response;
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
    let b1 = "<button type=\"submit\" onclick=\"createInputFieldsFindAddress()\">Find Address</button>";
    let b2 = "<button type=\"submit\" onclick=\"createInputFieldsCreateAddress()\">Create Address</button>";
    let b3 = "<button type=\"submit\" onclick=\"createInputFieldsGetAllPersonsWithZip()\">Find People with Zip Code</button>";
    linksDiv.innerHTML = b1 + b2 + b3;
}

function createInputFieldsFindAddress() {
    let form = "<form><label for=\"aname\">Address street:</label><br>" +
            "<input type=\"text\" id=\"aname\" name=\"aname\"><br>" +
            "<button type=\"button\" onclick=\"findAddress()\" class=\"btn btn-primary\">Find Address</button>" +
            "</form> ";
    let response = "<p id=\"find_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsCreateAddress() {
    let form = "<form><label for=\"astreet\">Address street:</label><br>" +
            "<input type=\"text\" id=\"astreet\" name=\"astreet\"><br>" +
            "<label for=\"aaddress\">Additional information:</label><br>" +
            "<input type=\"text\" id=\"aaddress\" name=\"aaddress\"><br>" +
            "<button type=\"button\" onclick=\"createAddress()\" class=\"btn btn-primary\">Create Address</button>" +
            "</form> ";
    let response = "<p id=\"creation_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsGetAllPersonsWithZip() {
    let form = "<form><label for=\"zcode\">Zip Code:</label><br>" +
            "<input type=\"number\" id=\"zcode\" name=\"zcode\"><br>" +
            "<button type=\"button\" onclick=\"getAllPersonsWithZip()\" class=\"btn btn-primary\">Find People</button>" +
            "</form> ";
    let response = "<p id=\"zip_Response\"></p>";
    linksDiv.innerHTML = form + response;
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
    let b1 = "<button type=\"submit\" onclick=\"createInputFieldsFindHobby()\">Find Hobby</button>";
    let b2 = "<button type=\"submit\" onclick=\"createInputFieldsCreateHobby()\">Create Hobby</button>";
    let b3 = "<button type=\"submit\" onclick=\"createInputFieldsEditHobby()\">Edit Hobby</button>";
    let b4 = "<button type=\"submit\" onclick=\"createInputFieldsGetAllPersonsWithHobby()\">Find People with Hobby</button>";
    linksDiv.innerHTML = b1 + b2 + b3 + b4;
}

function createInputFieldsEditHobby() {
    let form = "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<label for=\"oldhobby\">Hobby to edit:</label><br>" +
            "<input type=\"text\" id=\"oldhobby\" name=\"oldhobby\"><br>" +
            "<label for=\"newhobby\">New Hobby (One word):</label><br>" +
            "<input type=\"text\" id=\"newhobby\" name=\"newhobby\"><br>" +
            "<label for=\"newdescription\">New Description (One word):</label><br>" +
            "<input type=\"text\" id=\"newdescription\" name=\"newdescription\"><br>" +
            "<button type=\"button\" onclick=\"editHobby()\" class=\"btn btn-primary\">Edit Hobby</button>" +
            "</form> ";
    let response = "<p id=\"edit_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsCreateHobby() {
    let form = "<form><label for=\"hname\">Hobby name:</label><br>" +
            "<input type=\"text\" id=\"hname\" name=\"hname\"><br>" +
            "<label for=\"hdesc\">Hobby Description:</label><br>" +
            "<input type=\"text\" id=\"hdesc\" name=\"hdesc\"><br>" +
            "<button type=\"button\" onclick=\"createHobby()\" class=\"btn btn-primary\">Create Hobby</button>" +
            "</form> ";
    let response = "<p id=\"creation_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsFindHobby() {
    let form = "<form><label for=\"hname\">Hobby name:</label><br>" +
            "<input type=\"text\" id=\"hname\" name=\"hname\"><br>" +
            "<button type=\"button\" onclick=\"findHobby()\" class=\"btn btn-primary\">Find Hobby</button>" +
            "</form> ";
    let response = "<p id=\"find_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsGetAllPersonsWithHobby() {
    let form = "<form><label for=\"hobname\">Name of Hobby:</label><br>" +
            "<input type=\"text\" id=\"hobname\" name=\"hobname\"><br>" +
            "<button type=\"button\" onclick=\"getAllPersonsWithHobby()\" class=\"btn btn-primary\">Find People</button>" +
            "</form> ";
    let response = "<p id=\"hobby_Response\"></p>";
    linksDiv.innerHTML = form + response;
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
    let b1 = "<button type=\"submit\" onclick=\"createInputFieldsFindPhone()\">Find Phone</button>";
    let b2 = "<button type=\"submit\" onclick=\"createInputFieldsCreatePhone()\">Create Phone</button>";
    let b3 = "<button type=\"submit\" onclick=\"createInputFieldsFindPhoneByName()\">Find Phones By Name</button>";
    let b4 = "<button type=\"submit\" onclick=\"createInputFieldsEditPhone()\">Edit Phone</button>";
    linksDiv.innerHTML = b1 + b2 + b3 + b4;
}

function createInputFieldsFindPhoneByName() {
    let form = "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<button type=\"button\" onclick=\"findPhoneByName()\" class=\"btn btn-primary\">Find Phones</button>" +
            "</form> ";
    let response = "<p id=\"find_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsCreatePhone() {
    let form = "<form><label for=\"pnumber\">Phone number:</label><br>" +
            "<input type=\"number\" id=\"pnumber\" name=\"pnumber\"><br>" +
            "<label for=\"pdesc\">Phone Description:</label><br>" +
            "<input type=\"text\" id=\"pdesc\" name=\"pdesc\"><br>" +
            "<button type=\"button\" onclick=\"createPhone()\" class=\"btn btn-primary\">Create Phone</button>" +
            "</form> ";
    let response = "<p id=\"creation_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsFindPhone() {
    let form = "<form><label for=\"pnumber\">Phone number:</label><br>" +
            "<input type=\"number\" id=\"pnumber\" name=\"pnumber\"><br>" +
            "<button type=\"button\" onclick=\"findPhoneByNumber()\" class=\"btn btn-primary\">Find Phone</button>" +
            "</form> ";
    let response = "<p id=\"find_Response\"></p>";
    linksDiv.innerHTML = form + response;
}

function createInputFieldsEditPhone() {
    let form = "<form><label for=\"fname\">First name:</label><br>" +
            "<input type=\"text\" id=\"fname\" name=\"fname\"><br>" +
            "<label for=\"lname\">Last name:</label><br>" +
            "<input type=\"text\" id=\"lname\" name=\"lname\"><br>" +
            "<label for=\"oldnumber\">Number to edit:</label><br>" +
            "<input type=\"text\" id=\"oldnumber\" name=\"oldnumber\"><br>" +
            "<label for=\"newnumber\">New Number:</label><br>" +
            "<input type=\"text\" id=\"newnumber\" name=\"newnumber\"><br>" +
            "<label for=\"newdescription\">New Description (One word):</label><br>" +
            "<input type=\"text\" id=\"newdescription\" name=\"newdescription\"><br>" +
            "<button type=\"button\" onclick=\"editPhone()\" class=\"btn btn-primary\">Edit Phone</button>" +
            "</form> ";
    let response = "<p id=\"edit_Response\"></p>";
    linksDiv.innerHTML = form + response;
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
    fetch('/ca2/api/person/createnormal', options)
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
                    document.getElementById("creation_Response").innerHTML = firstname + " has been created";
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
    if (personid === "") {
        document.getElementById("find_Response").innerHTML = "<br><p>Please enter a ID in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
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
}

function personTable(person) {
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
/*--------------- Begin Add Phone -------------*/
/*---------------------------------------------*/

const createPhone = function () {
    let pnumber = document.getElementById("pnumber").value;
    let pdesc = document.getElementById("pdesc").value;
    let newPhone = {"number": pnumber, "description": pdesc};

    if (pnumber === "" && pdesc === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Please provide the information for the phone creation in the phone input fields.</p>";
    } else if (pdesc === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide a phone desription in the input field.</p>";
    } else if (pnumber === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide a phone number in the input field.</p>";
    } else {
    let options = makeOptions('POST', newPhone);
    fetch('/ca2/api/phone/add', options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("creation_Response").innerHTML = data.msg;
                } else if (data.code === 500) {
                    document.getElementById("creation_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("creation_Response").innerHTML = pnumber + " has been created";
                }
            });
        }
};

/*---------------------------------------------*/
/*--------------- End Add Phone ---------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*-------- Begin CreatePersonWithInfo ---------*/
/*---------------------------------------------*/
var phones = new Array();
var hobbies = new Array();

function addHobby() {
    let hobbyName = document.querySelector("#hname").value;
    let hobbyDescription = document.querySelector("#hdescription").value;
    let hobby = {"name": hobbyName, "description": hobbyDescription};
    hobbies.push(hobby);
    document.querySelector("#hname").value = "";
    document.querySelector("#hdescription").value = "";
}

function addPhone() {
    let phoneNumber = document.querySelector("#pnumber").value;
    let phoneDescription = document.querySelector("#pdescription").value;
    let phone = {"number": phoneNumber, "description": phoneDescription};
    phones.push(phone);
    document.querySelector("#pnumber").value = "";
    document.querySelector("#pdescription").value = "";
}

function createPersonWithInformation() {
    let firstname = document.querySelector("#fname").value;
    let lastname = document.querySelector("#lname").value;
    let email = document.querySelector("#email").value;
    let personid = document.querySelector("#pid").value;
    
    let zipCode = document.querySelector("#zipcode").value;
    let city = document.querySelector("#city").value;
    let cityInfo = {"zipCode": zipCode, "city": city};
    
    let street = document.querySelector("#street").value;
    let additionalInfo = document.querySelector("#additionalinfo").value;
    let address = {"street": street, "additionalInfo": additionalInfo, "cityInfo": cityInfo};
    
    let person = {"personid": personid, "email": email, "firstName": firstname, "lastName": lastname, "phones": phones, "address": address, "hobbies": hobbies};
    
    let options = makeOptions('POST', person);
    fetch('/ca2/api/person/create', options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("creation_Response").innerHTML = data.message;
                } else if (data.code === 500) {
                    document.getElementById("creation_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("creation_Response").innerHTML = firstname + " has been created";
                }
            });
            
            //resetting phones and hobbies arrays
            phones = [];
            hobbies = [];
}

/*---------------------------------------------*/
/*--------- End CreatePersonWithInfo ----------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*----------- Begin Create Address ------------*/
/*---------------------------------------------*/

const createAddress = function () {
    let astreet = document.getElementById("astreet").value;
    let aaddress = document.getElementById("aaddress").value;
    let newAddress = {"street": astreet, "additionalInfo": aaddress};

    if (astreet === "" && aaddress === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Please provide the information for the address creation in the address input fields.</p>";
    } else if (aaddress === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide additional desription in the input field.</p>";
    } else if (astreet === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide a street in the input field.</p>";
    } else {
    let options = makeOptions('POST', newAddress);
    fetch('/ca2/api/address/add', options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("creation_Response").innerHTML = data.msg;
                } else if (data.code === 500) {
                    document.getElementById("creation_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("creation_Response").innerHTML = "Address has been created";
                }
            });
        }
};

/*---------------------------------------------*/
/*------------ End Create Address -------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------ Begin Find Address -------------*/
/*---------------------------------------------*/

const findAddress = function () {
    let aname = document.getElementById("aname").value;
    let url = "/ca2/api/address/" + aname;
    if (aname === "") {
        document.getElementById("find_Response").innerHTML = "<br><p>Please enter a address street in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
                    if (data.code === 400) {
                        document.getElementById("find_Response").innerHTML = "<br><p>No address was found with this street name</p>";
                    } else if (data.code === 500) {
                        document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                    } else {
                        console.log("data", data);
                        linksDiv.innerHTML = addressTable(data);
                    }
                });
    }
}

function addressTable(address) {
    var tableinfo = "<table id=\"indextable\" class=\"table\">" +
            "<tr><th>Street</th>" +
            "<th>Additional Info</th>" +
            "</tr>" +
            "<tr><td>" + address.street + "</td><td>" + address.additionalInfo + "</td><td>" + "</tr></table>";
    return tableinfo;

}


/*---------------------------------------------*/
/*------------- End Find Address --------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------- Begin Get All Persons w/ Zip --------*/
/*---------------------------------------------*/

const getAllPersonsWithZip = function () {
    let zipCode = document.getElementById("zcode").value;
    let url = "/ca2/api/address/persons/" + zipCode;
    if (zipCode === "") {
        document.getElementById("zip_Response").innerHTML = "<br><p>Please enter a Zip Code in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
                    if (data.code === 400) {
                        document.getElementById("zip_Response").innerHTML = "<br><p>No person was found with this Zip Code</p>";
                    } else if (data.code === 500) {
                        document.getElementById("zip_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                    } else {
                        console.log("data", data);
                        linksDiv.innerHTML = personsTable(data);
                    }
                });
    }
};

function personsTable(persons) {
      let tableInfo = persons.map(persons => `<tr><td>${persons.personid}</td><td>${persons.email}</td><td>${persons.firstName}</td><td>${persons.lastName}</td></tr>`);
      tableInfo.unshift("<table id=\"indextable\" class=\"table\"><tr><th>Person ID</th><th>Email</th><th>First Name</th><th>Last Name</th></tr>");
      tableInfo.push("</table>");
    return tableInfo.join('');

}

/*---------------------------------------------*/
/*--------- End Get All Persons w/ Zip --------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------- Begin Find Hobby --------------*/
/*---------------------------------------------*/

const findHobby = function () {
    let hname = document.getElementById("hname").value;
    let url = "/ca2/api/hobby/" + hname;
    if (hname === "") {
        document.getElementById("find_Response").innerHTML = "<br><p>Please enter a hobby name in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
                    if (data.code === 400) {
                        document.getElementById("find_Response").innerHTML = "<br><p>No hobby was found with this name</p>";
                    } else if (data.code === 500) {
                        document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                    } else {
                        console.log("data", data);
                        linksDiv.innerHTML = hobbyTable(data);
                    }
                });
    }
}

function hobbyTable(hobby) {
    var tableinfo = "<table id=\"indextable\" class=\"table\">" +
            "<tr><th>Hobby name</th>" +
            "<th>Description</th>" +
            "</tr>" +
            "<tr><td>" + hobby.name + "</td><td>" + hobby.description + "</td><td>" + "</tr></table>";
    return tableinfo;

}


/*---------------------------------------------*/
/*-------------- End Find Hobby ---------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------- Begin Edit Hobby --------------*/
/*---------------------------------------------*/

function editHobby() {
    let firstName = document.querySelector("#fname").value;
    let lastName = document.querySelector("#lname").value;
    let oldName = document.querySelector("#oldhobby").value;
    let newName = document.querySelector("#newhobby").value;
    let newDescription = document.querySelector("#newdescription").value;
    let user = {"firstName": firstName, "lastName": lastName};
    let options = makeOptions('PUT', user);
    let url = "/ca2/api/person/hobby/" + oldName + "/" + newName + "/" + newDescription;

    fetch(url, options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("edit_Response").innerHTML = data.message;
                } else if (data.code === 500) {
                    document.getElementById("edit_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("edit_Response").innerHTML = "Hobby with name: " + newName + " was added to "
                            + firstName + " " + lastName + " and hobby with name: " + oldName + " was removed.";
                }
            });
}

/*---------------------------------------------*/
/*-------------- End Edit Hobby ---------------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------ Begin Get All Persons w/ Hobby -------*/
/*---------------------------------------------*/

const getAllPersonsWithHobby = function () {
    let hobbyName = document.getElementById("hobname").value;
    let url = "/ca2/api/hobby/persons/" + hobbyName;
    if (hobbyName === "") {
        document.getElementById("hobby_Response").innerHTML = "<br><p>Please enter the name of a hobby in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
                    if (data.code === 400) {
                        document.getElementById("hobby_Response").innerHTML = "<br><p>No person was found with this hobby name</p>";
                    } else if (data.code === 500) {
                        document.getElementById("hobby_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                    } else {
                        console.log("data", data);
                        linksDiv.innerHTML = persons2Table(data);
                    }
                });
    }
};

function persons2Table(persons) {
      let tableInfo = persons.map(persons => `<tr><td>${persons.personid}</td><td>${persons.email}</td><td>${persons.firstName}</td><td>${persons.lastName}</td></tr>`);
      tableInfo.unshift("<table id=\"indextable\" class=\"table\"><tr><th>Person ID</th><th>Email</th><th>First Name</th><th>Last Name</th></tr>");
      tableInfo.push("</table>");
//        var tableInfo = "<table id=\"indextable\" class=\"table\">" +
//            "<tr><th>Person ID</th>" +
//            "<th>Email</th>" +
//            "<th>First Name</th>" +
//            "<th>Last Name</th></tr>" +
//            "<tr><td>" + persons.personid + "</td><td>" + persons.email + "</td><td>" + persons.firstName + "</td>" +
//            "<td>" + persons.lastName + "</tr></table>";
    return tableInfo.join('');

}

/*---------------------------------------------*/
/*------- End Get All Persons w/ Hobby --------*/
/*---------------------------------------------*/

/*---------------------------------------------*/
/*------------- Begin Find Phone --------------*/
/*---------------------------------------------*/

const findPhoneByNumber = function () {
    let pnumber = document.getElementById("pnumber").value;
    let url = "/ca2/api/phone/" + pnumber;
    if (pnumber === "") {
        document.getElementById("find_Response").innerHTML = "<br><p>Please enter a phone number in the input field.</p>";
    } else {
        fetch(url)
                .then(res => res.json())
                .then(data => {
                    if (data.code === 400) {
                        document.getElementById("find_Response").innerHTML = "<br><p>No phone was found with this number</p>";
                    } else if (data.code === 500) {
                        document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                    } else {
                        console.log("data", data);
                        linksDiv.innerHTML = phoneTable(data);
                    }
                });
    }
}

function phoneTable(phone) {
    var tableinfo = "<table id=\"indextable\" class=\"table\">" +
            "<tr><th>Phone Number</th>" +
            "<th>Description</th>" +
            "<th>First Name</th>" +
            "<th>Last Name</th></tr>" +
            "<tr><td>" + phone.number + "</td><td>" + phone.description + "</td><td>" + phone.person.firstName + "</td>" +
            "<td>" + phone.person.lastName + "</tr></table>";
    return tableinfo;

}


/*---------------------------------------------*/
/*-------------- End Find Phone ---------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*----------- Begin Find Phone By Name --------*/
/*---------------------------------------------*/

function phonesToTable(phones) {
    var tableinfo = phones.map(x => `<tr><td>  ${x.number} </td><td> ${x.description} </td></tr>`);

    tableinfo.unshift("<table id=\"indextable\" class=\"table\">\n\
    <tr><th style=\"text-align:center\">Phone Number</th>\n\
    <th style=\"text-align:center\">Description</th></tr>");

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
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("find_Response").innerHTML = data.message;
                } else if (data.code === 500) {
                    document.getElementById("find_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    document.getElementById("find_Response").innerHTML = phonesToTable(data);
                }
            });
}

/*---------------------------------------------*/
/*----------- End Find Phone By Name ----------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*------------- Begin Edit Phone --------------*/
/*---------------------------------------------*/

function editPhone() {
    let firstName = document.querySelector("#fname").value;
    let lastName = document.querySelector("#lname").value;
    let oldNumber = document.querySelector("#oldnumber").value;
    let newNumber = document.querySelector("#newnumber").value;
    let newDescription = document.querySelector("#newdescription").value;
    let user = {"firstName": firstName, "lastName": lastName};
    let options = makeOptions('PUT', user);
    let url = "/ca2/api/person/phone/" + oldNumber + "/" + newNumber + "/" + newDescription;

    fetch(url, options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("edit_Response").innerHTML = data.message;
                } else if (data.code === 500) {
                    document.getElementById("edit_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("edit_Response").innerHTML = "Phone with number: " + newNumber + " was added to "
                            + firstName + " " + lastName + " and phone with number: " + oldNumber + " was removed.";
                }
            });
}

/*---------------------------------------------*/
/*-------------- End Edit Phone ---------------*/
/*---------------------------------------------*/


/*---------------------------------------------*/
/*-------------- Begin Add Hobby --------------*/
/*---------------------------------------------*/

const createHobby = function () {
    let hname = document.getElementById("hname").value;
    let hdesc = document.getElementById("hdesc").value;
    let newHobby = {"name": hname, "description": hdesc};

    if (hdesc === "" && hname === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Please provide the information for the hobby creation in the hobby input fields.</p>";
    } else if (hdesc === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide a hobby desription in the input field.</p>";
    } else if (hname === "") {
        document.getElementById("creation_Response").innerHTML = "<br><p>Plase provide a hobby name in the input field.</p>";
    } else {
    let options = makeOptions('POST', newHobby);
    fetch('/ca2/api/hobby/createhobby', options)
            .then(function (response) {
                return response.json();
            })
            .then(function (data) {
                if (data.code === 400) {
                    console.error('Fail:', data);
                    document.getElementById("creation_Response").innerHTML = data.msg;
                } else if (data.code === 500) {
                    document.getElementById("creation_Response").innerHTML = "<br><p>An error has occured, please try again at a later time.</p>";
                } else {
                    console.log('Success:', data);
                    document.getElementById("creation_Response").innerHTML = hname + " has been created";
                }
            });
        }
};


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