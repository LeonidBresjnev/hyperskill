let taskList = document.getElementById("task-list");
let addTaskButtonClick = document.getElementById("add-task-button");
addTaskButtonClick.addEventListener("click", addTaskButton);
let k = 0;

let tasks = JSON.parse(localStorage.getItem("list")) || [];
for (x in tasks) {
    taskList.innerHTML +=
        '<li>' +
        '<input type="checkbox"  id="checkbox' + k + '">' + ' ' +
        '<span class="task" id = "task' + k + '">' +
        document.getElementById("input-task").value +
        '</span>' + ' ' +
        '<button onclick="this.parentElement.remove(), onDelete(k)" class="delete-btn"></button>' +
        '</li>'
    tasks.push(document.getElementById("input-task").value);
    document.getElementById("input-task").value = '';
}

function addTaskButton() {
    k++;
    taskList.innerHTML +=
        '<li>' +
        '<input type="checkbox"  id="checkbox' + k + '">' + ' ' +
        '<span class="task" id = "task' + k + '">' +
        document.getElementById("input-task").value +
        '</span>' + ' ' +
        '<button onclick="this.parentElement.remove(), onDelete(k)" class="delete-btn"></button>' +
        '</li>'
    tasks.push(document.getElementById("input-task").value);
    document.getElementById("input-task").value = '';
    console.log(tasks);
    localStorage.setItem("list", JSON.stringify(tasks));

    console.log(JSON.parse(localStorage.getItem("list")).length);
}



function onDelete(k) {
    tasks.splice(k);
    localStorage.setItem("list", JSON.stringify(tasks));
    console.log(JSON.parse(localStorage.getItem("list")).length);
}

