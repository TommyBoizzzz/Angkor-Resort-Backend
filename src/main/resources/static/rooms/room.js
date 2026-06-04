const API_URL = "http://localhost:8080/api/rooms";

document.addEventListener("DOMContentLoaded", loadRooms);

document.getElementById("roomForm").addEventListener("submit", async function(e){
    e.preventDefault();

    const room = {
        roomNumber: document.getElementById("roomNumber").value,
        roomType: document.getElementById("roomType").value,
        price: document.getElementById("price").value,
        status: document.getElementById("status").value
    };

    await fetch(API_URL,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(room)
    });

    document.getElementById("roomForm").reset();

    loadRooms();
});

async function loadRooms(){

    const response = await fetch(API_URL);
    const rooms = await response.json();

    const table = document.getElementById("roomTable");

    table.innerHTML = "";

    rooms.forEach(room => {

        table.innerHTML += `
        <tr>
            <td>${room.id}</td>
            <td>${room.roomNumber}</td>
            <td>${room.roomType}</td>
            <td>$${room.price}</td>
            <td>${room.status}</td>
            <td>
                <button onclick="deleteRoom(${room.id})">
                    Delete
                </button>
            </td>
        </tr>
        `;
    });
}

async function deleteRoom(id){

    await fetch(`${API_URL}/${id}`,{
        method:"DELETE"
    });

    loadRooms();
}